package com.license.tester.controller;

import com.license.tester.db.model.SubGroup;
import com.license.tester.db.model.Teacher;
import com.license.tester.db.repo.SubGroupRepository;
import com.license.tester.db.repo.TeacherRepository;
import com.license.tester.transfer.dto.SubGroupDto;
import com.license.tester.transfer.mapping.SubGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group")
@CrossOrigin
public class GroupController {

    private final SubGroupRepository subGroupRepository;
    private final TeacherRepository teacherRepository;
    private final SubGroupMapper subGroupMapper;

    @Autowired
    public GroupController(SubGroupRepository subGroupRepository, TeacherRepository teacherRepository, SubGroupMapper subGroupMapper) {
        this.subGroupRepository = subGroupRepository;
        this.teacherRepository = teacherRepository;
        this.subGroupMapper = subGroupMapper;
    }

    @GetMapping("/all")
    public List<SubGroupDto> getAll() {
        List<SubGroup> subGroups = subGroupRepository.findAll();

        return subGroups.stream()
                .map(group -> subGroupMapper.convertSubGroupToDto(group))
                .collect(Collectors.toList());
    }

    @GetMapping("/byId/{id}")
    public SubGroupDto getById(@PathVariable String id) {
        SubGroup subGroup = subGroupRepository.findById(UUID.fromString(id)).orElse(null);
        if (subGroup == null) {
            throw new EntityNotFoundException("Could not find in the database the subGroup with id " + id);
        }
        return subGroupMapper.convertSubGroupToDto(subGroup);
    }

    @GetMapping("/byTeacher/{teacherId}")
    public List<SubGroupDto> getByTeacher(@PathVariable String teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(UUID.fromString(teacherId));
        if (teacher.isPresent()) {
            List<SubGroup> subGroups = subGroupRepository.findAllByTeacher(teacher.get());
            return subGroups.stream()
                    .map(group -> subGroupMapper.convertSubGroupToDto(group))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @PostMapping("/create")
    public SubGroupDto save(@RequestBody SubGroupDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("An error occurred during validation");
        }
        SubGroup subGroup = subGroupMapper.convertToSubGroupEntity(dto);

        subGroupRepository.save(subGroup);

        return subGroupMapper.convertSubGroupToDto(subGroup);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        subGroupRepository.deleteById(UUID.fromString(id));
    }

}
