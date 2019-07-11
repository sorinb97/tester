package com.license.tester.controller;

import com.license.tester.db.model.Assignment;
import com.license.tester.db.model.Grade;
import com.license.tester.db.model.Student;
import com.license.tester.db.repo.AssignmentRepository;
import com.license.tester.db.repo.GradeRepository;
import com.license.tester.db.repo.StudentRepository;
import com.license.tester.db.repo.TeacherRepository;
import com.license.tester.service.FileStorageService;
import com.license.tester.service.GradingService;
import com.license.tester.transfer.dto.GradeDto;
import com.license.tester.transfer.dto.GradeShowDto;
import com.license.tester.transfer.mapping.GradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grade")
@CrossOrigin
public class GradeController {

    public static final String GRADING_DIR = "temp";
    private final GradeRepository gradeRepository;
    private final GradeMapper gradeMapper;
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;
    private final FileStorageService storageService;
    private final GradingService gradingService;
    private final TeacherRepository teacherRepository;

    @Autowired
    public GradeController(GradeRepository gradeRepository, GradeMapper gradeMapper, StudentRepository studentRepository,
                           AssignmentRepository assignmentRepository, FileStorageService storageService,
                           GradingService gradingService, TeacherRepository teacherRepository) {

        this.gradeRepository = gradeRepository;
        this.gradeMapper = gradeMapper;
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
        this.storageService = storageService;
        this.gradingService = gradingService;
        this.teacherRepository = teacherRepository;
    }

    @RequestMapping("/all")
    public List<GradeDto> getAll() {
        List<Grade> grades = gradeRepository.findAll();

        return grades.stream()
                .map(grade -> gradeMapper.convertGradeToDto(grade))
                .collect(Collectors.toList());
    }

    @GetMapping("/byStudent/{studentId}")
    public List<GradeDto> getAllByStudent(@PathVariable String studentId) {
        Optional<Student> student = studentRepository.findById(UUID.fromString(studentId));
        if (student.isPresent()) {
            List<Grade> grades = gradeRepository.findAllByStudent(student.get());
            return grades.stream()
                    .map(grade -> gradeMapper.convertGradeToDto(grade))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    @GetMapping("/byAssignment/{assignmentId}/{teacherId}")
    public List<GradeShowDto> getAllByAssignment(@PathVariable String assignmentId, @PathVariable String teacherId) {
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(UUID.fromString(assignmentId));
        if (assignmentOpt.isPresent()) {
            List<Grade> grades = gradeRepository.findAllByAssignment(assignmentOpt.get());
            List<Grade> filteredGrades = grades.stream()
                    .filter(g -> g.getStudent().getSubGroup().getTeacher().getId().toString().equals(teacherId))
                    .collect(Collectors.toList());
            return filteredGrades.stream()
                    .map(grade -> gradeMapper.convertToGradeInfoDto(grade))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @GetMapping("/allInfo/{teacherId}")
    public List<GradeShowDto> getAllInfo(@PathVariable String teacherId) {
        List<Grade> grades = gradeRepository.findAll();
        List<Grade> filteredGrades = grades.stream()
                .filter(g -> g.getStudent().getSubGroup().getTeacher().getId().toString().equals(teacherId))
                .collect(Collectors.toList());
        return filteredGrades.stream()
                .map(grade -> gradeMapper.convertToGradeInfoDto(grade))
                .collect(Collectors.toList());
    }

    @PostMapping("/create")
    public GradeDto create(GradeDto dto, @RequestParam("file") MultipartFile file) {
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(UUID.fromString(dto.getAssignmentId()));
        Optional<Student> studentOpt = studentRepository.findById(UUID.fromString(dto.getStudentId()));
        if (assignmentOpt.isPresent() && studentOpt.isPresent()) {
            if (gradeRepository.findByStudentAndAssignment(studentOpt.get(), assignmentOpt.get()) == null) {
                String studentSpecificPrefix = studentOpt.get().getId().toString();
                String zipFile = storageService.storeFile(file, GRADING_DIR + "/" + studentSpecificPrefix);
                Assignment assignment = assignmentOpt.get();
                double grade = gradingService.grade(zipFile, assignment, studentSpecificPrefix);
                storageService.removeFile(zipFile, GRADING_DIR + "/" + studentSpecificPrefix);
                Grade gradeEntity = new Grade(grade, studentOpt.get(), assignment);
                if (dto.getId() != null && !dto.getId().isEmpty()) {
                    gradeEntity.setId(UUID.fromString(dto.getId()));
                }
                gradeRepository.save(gradeEntity);
                return gradeMapper.convertGradeToDto(gradeEntity);
            } else {
                throw new IllegalStateException("You have already taken this test.");
            }
        }
        throw new IllegalStateException("Please provide a valid assignment and student");
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        gradeRepository.deleteById(UUID.fromString(id));
    }

}
