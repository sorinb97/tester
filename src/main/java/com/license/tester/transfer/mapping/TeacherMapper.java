package com.license.tester.transfer.mapping;

import com.license.tester.db.model.Teacher;
import com.license.tester.transfer.dto.TeacherDto;
import com.license.tester.transfer.dto.UserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public TeacherDto convertTeacherToDto(Teacher teacher) {
        return new TeacherDto(teacher.getId().toString(), teacher.getName(), teacher.getEmail());
    }

    public Teacher convertToTeacherEntity(TeacherDto dto) {
        return new Teacher(dto.getName(), dto.getEmail());
    }


    public Teacher convertToTeacherEntity(UserDto dto) {
        String passwordNoCrypt = dto.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordCrypt = encoder.encode(passwordNoCrypt);
        return new Teacher(dto.getName(), dto.getEmail(), passwordCrypt);
    }
}
