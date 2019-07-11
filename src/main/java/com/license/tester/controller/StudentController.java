package com.license.tester.controller;

import com.license.tester.db.model.Student;
import com.license.tester.db.model.SubGroup;
import com.license.tester.db.model.Teacher;
import com.license.tester.db.repo.StudentRepository;
import com.license.tester.db.repo.SubGroupRepository;
import com.license.tester.db.repo.TeacherRepository;
import com.license.tester.service.exception.EmailExistsException;
import com.license.tester.transfer.dto.StudentDto;
import com.license.tester.transfer.dto.UserDto;
import com.license.tester.transfer.mapping.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
@CrossOrigin
public class StudentController {
    private final StudentRepository studentRepository;
    private final SubGroupRepository subGroupRepository;
    private final TeacherRepository teacherRepository;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentController(StudentRepository studentRepository, SubGroupRepository subGroupRepository, TeacherRepository teacherRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.subGroupRepository = subGroupRepository;
        this.teacherRepository = teacherRepository;
        this.studentMapper = studentMapper;
    }

    @GetMapping("/all")
    public List<StudentDto> getAll() {
        List<Student> students = studentRepository.findAll();

        return students.stream()
                .map(student -> studentMapper.convertStudentToDto(student))
                .collect(Collectors.toList());
    }

    @GetMapping("/byId/{id}")
    public StudentDto getById(@PathVariable String id) {
        Student student = studentRepository.findById(UUID.fromString(id)).orElse(null);
        if (student == null) {
            throw new EntityNotFoundException("Could not find in the database the student with id " + id);
        }
        return studentMapper.convertStudentToDto(student);
    }

    @GetMapping("/byTeacherId/{id}")
    public List<StudentDto> getByTeacher(@PathVariable String id) {
        Teacher teacher = teacherRepository.findById(UUID.fromString(id)).orElse(null);
        if (teacher == null) {
            throw new EntityNotFoundException("Could not find in the database the student with id " + id);
        }
        List<SubGroup> subGroups = teacher.getSubGroups();
        List<Student> allStuds = new ArrayList<>();
        for (SubGroup subGroup : subGroups) {
            allStuds.addAll(subGroup.getStudents());
        }
        return allStuds.stream()
                .map(student -> studentMapper.convertStudentToDto(student))
                .collect(Collectors.toList());
    }

    @GetMapping("/byGroup/{groupId}")
    public List<StudentDto> getByGroup(@PathVariable String groupId) {
        Optional<SubGroup> group = subGroupRepository.findById(UUID.fromString(groupId));
        if (group.isPresent()) {
            List<Student> students = studentRepository.findAllBySubGroup(group.get());
            return students.stream()
                    .map(student -> studentMapper.convertStudentToDto(student))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @PostMapping("/login")
    public StudentDto login(@RequestBody UserDto dto) {
        Student student = studentRepository.findByEmail(dto.getEmail());
        if (student == null) {
            throw new EntityNotFoundException("Could not find in the database the student with email " + dto.getEmail());
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(dto.getPassword(), student.getPassword())) {
            return studentMapper.convertStudentToDto(student);
        }
        throw new EntityNotFoundException();
    }

    @PostMapping("/create")
    public StudentDto save(@RequestBody UserDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("An error occurred during validation");
        }
        Student existingStudent = studentRepository.findByEmail(dto.getEmail());
        if (existingStudent != null) {
            throw new EmailExistsException();
        }
        Student student = studentMapper.convertToStudentEntity(dto);

        studentRepository.save(student);

        return new StudentDto(dto.getName(), dto.getEmail(), dto.getGroupId());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        studentRepository.deleteById(UUID.fromString(id));
    }
}
