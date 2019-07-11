package com.license.tester.transfer.mapping;

import com.license.tester.db.model.Assignment;
import com.license.tester.transfer.dto.AssignmentDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AssignmentMapper {

    public AssignmentMapper() {
    }

    public AssignmentDto convertAssignToDto(Assignment assignment) {
        AssignmentDto assignmentDto = new AssignmentDto(assignment.getId().toString(), assignment.getName(), assignment.getInterfaceFileId(), assignment.getJUnitFileId());
        assignmentDto.setInterfaceCode(assignment.getInterfaceFileId().replace(".class", ".java"));
        return assignmentDto;
    }

    public Assignment convertToAssignmentEntity(AssignmentDto dto) {
        Assignment assignment = new Assignment(dto.getName(), dto.getInterfaceFileId(), dto.getjUnitFileId());
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            assignment.setId(UUID.fromString(dto.getId()));
        }
        return assignment;

    }
}
