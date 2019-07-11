package com.license.tester.db.repo;

import com.license.tester.db.model.Student;
import com.license.tester.db.model.SubGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    List<Student> findAllBySubGroup(SubGroup subGroup);

    Student findByEmail(String eMail);
}
