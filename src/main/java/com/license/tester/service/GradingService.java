package com.license.tester.service;

import com.license.tester.config.FileStorageProperties;
import com.license.tester.db.model.Assignment;
import com.license.tester.service.exception.BadRequestException;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class GradingService {

    private static final Logger logger = LoggerFactory.getLogger(GradingService.class);
    private final Path fileStorageLocation;
    private final Path uploadDir;

    @Autowired
    public GradingService(FileStorageProperties fileStorageProperties) {
        this.uploadDir = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath()
                .normalize()
                .resolve("temp");
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            logger.warn("Could not create the directory for storing upload files.");
        }
    }

    public double grade(String studentPrjZip, Assignment assignment, String studentSpecificPrefix) {
        String sourceLocation = fileStorageLocation.resolve(studentSpecificPrefix).toAbsolutePath().toString() + "/" + studentPrjZip;
        String destinationLocation = fileStorageLocation.resolve(studentSpecificPrefix).resolve("grading").toAbsolutePath().toString();

        unzip(sourceLocation, destinationLocation);

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URLClassLoader assignmentClassLoader = new URLClassLoader(new URL[]{new File(uploadDir.toString()).toURI().toURL()}, classLoader);
            Class<?> implementingClass = findImplementingClass(destinationLocation, assignment.getInterfaceFileId(), assignmentClassLoader);
            Class<?> jUnitTestClass = findAssignmentClass(assignment.getJUnitFileId(), assignmentClassLoader);

            if (implementingClass == null || jUnitTestClass == null) {
                cleanUpDir(destinationLocation);
                throw new BadRequestException();
            }
            Class<?> interfaceClass = findAssignmentClass(assignment.getInterfaceFileId(), assignmentClassLoader);
            Grader grader = new Grader(interfaceClass, implementingClass, jUnitTestClass);
            double result = grader.run();
            if (result == -1) {
                throw new BadRequestException();
            }
            return result;
        } catch (MalformedURLException e) {
            logger.error("Could not get the interface file location", e);
            throw new BadRequestException();
        } catch (IOException e) {
            logger.error("Got an IO error", e);
            throw new BadRequestException();
        } catch (ClassNotFoundException e) {
            logger.error("Could not load class from name", e);
            throw new BadRequestException();
        } finally {
            cleanUpDir(destinationLocation);
        }
    }

    private Class<?> findAssignmentClass(String assignmentFileId, URLClassLoader assignmentClassLoader) throws IOException, ClassNotFoundException {
        Path assignmentPath = getAssignmentFilePath(assignmentFileId);
        if (assignmentPath == null) {
            return null;
        }
        return assignmentClassLoader.loadClass(getQualifiedName(assignmentPath, "uploads"));
    }

    private Class<?> findImplementingClass(String projectDir, String interfaceFileId, URLClassLoader assignmentClassLoader) throws IOException, ClassNotFoundException {

        String binDir = computeBinDir(projectDir);
        if (binDir == null) {
            return null;
        }
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new File(binDir).toURI().toURL()}, assignmentClassLoader);
        Class<?> interfaceClass = findAssignmentClass(interfaceFileId, assignmentClassLoader);
        if (interfaceClass == null) {
            return null;
        }
        List<Path> classesInPrj = Files.walk(Paths.get(binDir))
                .filter(path -> path.toAbsolutePath().toString().endsWith(".class"))
                .collect(Collectors.toList());
        for (Path path : classesInPrj) {
            String fullClassName = getQualifiedName(path, "bin");
            if (fullClassName == null) {
                return null;
            }
            Class<?> aClass = urlClassLoader.loadClass(fullClassName);
            if (interfaceClass.isAssignableFrom(aClass) && !interfaceClass.equals(aClass)) {
                return aClass;
            }
        }

        return null;
    }

    private Path getAssignmentFilePath(String assignmentFileId) throws IOException {
        Optional<Path> assignmentFilePath = Files.walk(uploadDir)
                .filter(path -> !path.toAbsolutePath().toString().contains("temp"))
                .filter(path -> path.toAbsolutePath().toString().endsWith(assignmentFileId))
                .findFirst();
        return assignmentFilePath.isPresent() ? assignmentFilePath.get() : null;
    }

    private String computeBinDir(String projectDir) throws IOException {
        Optional<Path> binPath = Files.walk(Paths.get(projectDir))
                .filter(path -> path.toAbsolutePath().toString().endsWith("bin"))
                .findFirst();
        return binPath.isPresent() ? binPath.get().toAbsolutePath().toString() : null;
    }

    private String getQualifiedName(Path path, String sourceFolder) {

        for (int i = 0; i < path.getNameCount(); i++) {
            if (path.getName(i).toString().endsWith(sourceFolder)) {
                Path subpath = path.subpath(i + 1, path.getNameCount());
                String pathString = subpath.toString();
                String packageString = pathString.replaceAll("/", ".");
                return packageString.substring(0, packageString.lastIndexOf('.'));
            }
        }
        return null;
    }

    private void cleanUpDir(String destinationLocation) {
        try {
            Files.walk(Paths.get(destinationLocation))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            logger.warn("Failed to delete temporary grading folder", e);
        }
    }

    private void unzip(String sourceLocation, String destinationLocation) {
        try {
            ZipFile zipFile = new ZipFile(sourceLocation);
            zipFile.extractAll(destinationLocation);
        } catch (ZipException e) {
            logger.error("Could not create zip file from provided path", e);
        }
    }

}
