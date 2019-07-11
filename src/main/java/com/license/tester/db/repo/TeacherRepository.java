package com.license.tester.db.repo;

import com.license.tester.db.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    Teacher findByEmail(String eMail);
}
