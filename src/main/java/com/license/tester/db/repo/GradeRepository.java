package com.license.tester.db.repo;

import com.license.tester.db.model.Assignment;
import com.license.tester.db.model.Grade;
import com.license.tester.db.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GradeRepository extends JpaRepository<Grade, UUID> {

    List<Grade> findAllByStudent(Student student);

    List<Grade> findAllByAssignment(Assignment assignment);

    Grade findByStudentAndAssignment(Student student, Assignment assignment);
}
