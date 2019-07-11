package com.license.tester.db.repo;

import com.license.tester.db.model.SubGroup;
import com.license.tester.db.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubGroupRepository extends JpaRepository<SubGroup, UUID> {
    List<SubGroup> findAllByTeacher(Teacher teacher);
}
