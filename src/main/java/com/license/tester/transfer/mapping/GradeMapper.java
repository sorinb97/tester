package com.license.tester.transfer.mapping;

import com.license.tester.db.model.Assignment;
import com.license.tester.db.model.Grade;
import com.license.tester.db.model.Student;
import com.license.tester.db.repo.AssignmentRepository;
import com.license.tester.db.repo.StudentRepository;
import com.license.tester.transfer.dto.GradeDto;
import com.license.tester.transfer.dto.GradeShowDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GradeMapper {

    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;

    public GradeMapper(StudentRepository studentRepository, AssignmentRepository assignmentRepository) {
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public GradeDto convertGradeToDto(Grade grade) {
        GradeDto gradeDto = new GradeDto(grade.getId().toString(), grade.getGrade(), grade.getStudent().getId().toString(), grade.getAssignment().getId().toString());
        gradeDto.setAssignmentName(grade.getAssignment().getName());
        return gradeDto;
    }

    public Grade convertToGradeEntity(GradeDto dto) {
        Student student = studentRepository.findById(UUID.fromString(dto.getStudentId())).get();
        Assignment assignment = assignmentRepository.findById(UUID.fromString(dto.getAssignmentId())).get();
        Grade grade = new Grade(student, assignment);
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            grade.setId(UUID.fromString(dto.getId()));
        }
        return grade;
    }

    public GradeShowDto convertToGradeInfoDto(Grade grade) {
        return new GradeShowDto(grade.getId().toString(), grade.getStudent().getName(), grade.getGrade());
    }
}
