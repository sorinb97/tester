package com.license.tester.controller;

import com.license.tester.db.model.Assignment;
import com.license.tester.db.model.Grade;
import com.license.tester.db.model.Student;
import com.license.tester.db.repo.AssignmentRepository;
import com.license.tester.db.repo.StudentRepository;
import com.license.tester.service.FileStorageService;
import com.license.tester.transfer.dto.AssignmentDto;
import com.license.tester.transfer.mapping.AssignmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/assignment")
@CrossOrigin
public class AssignmentController {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    private final AssignmentRepository assignmentRepository;
    private final AssignmentMapper assignmentMapper;
    private final FileStorageService storageService;
    private final StudentRepository studentRepository;

    @Autowired
    public AssignmentController(AssignmentRepository assignmentRepository, AssignmentMapper assignmentMapper,
                                FileStorageService storageService, StudentRepository studentRepository) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
        this.storageService = storageService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/all")
    public List<AssignmentDto> getAll() {
        List<Assignment> assignments = assignmentRepository.findAll();

        return assignments.stream()
                .map(assign -> assignmentMapper.convertAssignToDto(assign))
                .collect(Collectors.toList());
    }

    @GetMapping("/allAvailableFor/{studentId}")
    public List<AssignmentDto> getNotTakenForStudent(@PathVariable String studentId) {
        Optional<Student> studentOpt = studentRepository.findById(UUID.fromString(studentId));
        List<Assignment> assignments = assignmentRepository.findAll();

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            List<Assignment> takenAssignments = getAllTakenAssignments(student);
            assignments = assignments.stream().filter(assignment -> !takenAssignments.contains(assignment)).collect(Collectors.toList());
        }

        return assignments.stream()
                .map(assign -> assignmentMapper.convertAssignToDto(assign))
                .collect(Collectors.toList());
    }

    private List<Assignment> getAllTakenAssignments(Student student) {
        List<Grade> grades = student.getGrades();
        return grades.stream().map(g -> g.getAssignment()).collect(Collectors.toList());
    }

    @GetMapping("/byId/{id}")
    public AssignmentDto getById(@PathVariable String id) {
        Assignment assignment = assignmentRepository.findById(UUID.fromString(id)).orElse(null);
        if (assignment == null) {
            throw new EntityNotFoundException("Could not find in the database the assignment with id " + id);
        }

        return assignmentMapper.convertAssignToDto(assignment);
    }

    @PostMapping("/create")
    public AssignmentDto create(AssignmentDto dto, @RequestParam("interfaceFile") MultipartFile interfaceFile,
                                @RequestParam("jUnitFile") MultipartFile jUnitFile,
                                @RequestParam("interfaceCodeFile") MultipartFile interfaceCode) {

        Assignment assignment = new Assignment();
        assignment.setName(dto.getName());
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            assignment.setId(UUID.fromString(dto.getId()));
        }
        String interfacePackage = dto.getInterfacePackage().replaceAll("\\.", "/");
        String interfaceId = storageService.storeFile(interfaceFile, interfacePackage);
        storageService.storeFile(interfaceCode, interfacePackage);
        assignment.setInterfaceFileId(interfaceId);
        String jUnitPackage = dto.getJUnitPackage().replaceAll("\\.", "/");
        String jUnitId = storageService.storeFile(jUnitFile, jUnitPackage);
        assignment.setJUnitFileId(jUnitId);
        assignmentRepository.save(assignment);
        return assignmentMapper.convertAssignToDto(assignment);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id, HttpServletRequest request) {
        Resource resource = storageService.loadFileAsResource(id);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            logger.warn("Could not determine file type");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        assignmentRepository.deleteById(UUID.fromString(id));
    }

}
