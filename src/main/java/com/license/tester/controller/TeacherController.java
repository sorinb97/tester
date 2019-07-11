package com.license.tester.controller;

import com.license.tester.db.model.Teacher;
import com.license.tester.db.repo.TeacherRepository;
import com.license.tester.service.exception.EmailExistsException;
import com.license.tester.transfer.dto.TeacherDto;
import com.license.tester.transfer.dto.UserDto;
import com.license.tester.transfer.mapping.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teacher")
@CrossOrigin
public class TeacherController {

    public final TeacherRepository teacherRepository;
    public final TeacherMapper teacherMapper;

    @Autowired
    public TeacherController(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping("/all")
    public List<TeacherDto> getAll() {
        List<Teacher> teachers = teacherRepository.findAll();
        System.out.println(teachers.size());
        return teachers.stream()
                .map(teacher -> teacherMapper.convertTeacherToDto(teacher))
                .collect(Collectors.toList());
    }

    @GetMapping("/byId/{id}")
    public TeacherDto getById(@PathVariable String id) {
        Teacher teacher = teacherRepository.findById(UUID.fromString(id)).orElse(null);
        if (teacher == null) {
            throw new EntityNotFoundException("Could not find in the database the teacher with id " + id);
        }
        return teacherMapper.convertTeacherToDto(teacher);
    }

    @PostMapping("/login")
    public TeacherDto login(@RequestBody UserDto dto) {
        Teacher teacher = teacherRepository.findByEmail(dto.getEmail());
        if (teacher == null) {
            throw new EntityNotFoundException("Could not find in the database the teacher with email " + dto.getEmail());
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(dto.getPassword(), teacher.getPassword())) {
            return teacherMapper.convertTeacherToDto(teacher);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @PostMapping("/create")
    public TeacherDto save(@RequestBody UserDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("An error occurred during validation");
        }
        Teacher existingTeacher = teacherRepository.findByEmail(dto.getEmail());
        if (existingTeacher != null) {
            throw new EmailExistsException();
        }
        Teacher teacher = teacherMapper.convertToTeacherEntity(dto);

        teacherRepository.save(teacher);

        return teacherMapper.convertTeacherToDto(teacher);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        teacherRepository.deleteById(UUID.fromString(id));
    }


}
