package com.license.tester.transfer.mapping;

import com.license.tester.db.model.Student;
import com.license.tester.db.model.SubGroup;
import com.license.tester.db.repo.SubGroupRepository;
import com.license.tester.transfer.dto.StudentDto;
import com.license.tester.transfer.dto.UserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StudentMapper {
    private final SubGroupRepository subGroupRepository;

    public StudentMapper(SubGroupRepository subGroupRepository) {
        this.subGroupRepository = subGroupRepository;
    }

    public StudentDto convertStudentToDto(Student student) {
        return new StudentDto(student.getId().toString(), student.getName(), student.getEmail(), student.getSubGroup().getId().toString());
    }

    public Student convertToStudentEntity(StudentDto dto) {

        SubGroup subGroup = subGroupRepository.findById(UUID.fromString(dto.getGroupId())).get();
        return new Student(dto.getName(), dto.getEmail(), subGroup);
    }

    public Student convertToStudentEntity(UserDto dto) {

        SubGroup subGroup = subGroupRepository.findById(UUID.fromString(dto.getGroupId())).get();
        String passwordNoCrypt = dto.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordCrypt = encoder.encode(passwordNoCrypt);
        Student student = new Student(dto.getName(), dto.getEmail(), passwordCrypt, subGroup);
        return student;
    }

}
