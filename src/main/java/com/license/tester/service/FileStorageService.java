package com.license.tester.service;

import com.license.tester.config.FileStorageProperties;
import com.license.tester.service.exception.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath()
                .normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            logger.warn("Could not create the directory for storing upload files.");
        }
    }

    public String storeFile(MultipartFile file) {
        return storeFile(file, "");
    }

    public String storeFile(MultipartFile file, String dirPrefix) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path targetLocation = computePath(fileName, dirPrefix);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            logger.error("Error occurred copying the file " + file.getName() + " to disk.", e);
        }
        throw new IllegalStateException();
    }

    public void removeFile(String fileName, String dirPrefix) {
        try {
            Path targetLocation = computePath(fileName, dirPrefix);
            Files.deleteIfExists(targetLocation);

        } catch (IOException e) {
            logger.error("Error occurred copying the file " + fileName + " to disk.", e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = getAssignmentFilePath(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found");
            }
        } catch (IOException e) {
            throw new FileNotFoundException("File not found", e);
        }
    }

    public void removeFile(String fileName) {
        removeFile(fileName, "");
    }

    private Path computePath(String fileName, String dirPrefix) throws IOException {
        Path targetLocation = this.fileStorageLocation;
        if (dirPrefix != null && !dirPrefix.isEmpty()) {
            targetLocation = targetLocation.resolve(dirPrefix);
        }
        Files.createDirectories(targetLocation);
        return targetLocation.resolve(fileName);
    }

    private Path getAssignmentFilePath(String assignmentFileId) throws IOException {
        Optional<Path> assignmentFilePath = Files.walk(this.fileStorageLocation)
                .filter(path -> !path.toAbsolutePath().toString().contains("temp"))
                .filter(path -> path.toAbsolutePath().toString().endsWith(assignmentFileId))
                .findFirst();
        return assignmentFilePath.isPresent() ? assignmentFilePath.get() : null;
    }
}
