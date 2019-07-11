package com.license.tester.transfer.mapping;

import com.license.tester.db.model.SubGroup;
import com.license.tester.db.model.Teacher;
import com.license.tester.db.repo.TeacherRepository;
import com.license.tester.transfer.dto.SubGroupDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SubGroupMapper {

    private final TeacherRepository teacherRepository;

    public SubGroupMapper(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public SubGroupDto convertSubGroupToDto(SubGroup subGroup) {
        return new SubGroupDto(subGroup.getId().toString(), subGroup.getName(), subGroup.getTeacher().getId().toString());
    }

    public SubGroup convertToSubGroupEntity(SubGroupDto dto) {
        Teacher teacher = teacherRepository.findById(UUID.fromString(dto.getTeacherId())).get();
        SubGroup subGroup = new SubGroup(dto.getName(), teacher);
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            subGroup.setId(UUID.fromString(dto.getId()));
        }
        return subGroup;
    }

}
