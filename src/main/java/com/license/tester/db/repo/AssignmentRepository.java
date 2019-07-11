package com.license.tester.db.repo;

import com.license.tester.db.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
    public Assignment findByInterfaceFileId(String interfaceFileId);

    public Assignment findByJUnitFileId(String jUnitFileId);
}
