package com.license.tester.db.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class Grade {

    @Id
    private UUID id;

    private double grade;

    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    private Assignment assignment;

    public Grade() {
        this.id = UUID.randomUUID();
    }


    public Grade(Student student, Assignment assignment) {
        this();
        this.student = student;
        this.assignment = assignment;
    }

    public Grade(double grade, Student student, Assignment assignment) {
        this();
        this.grade = grade;
        this.student = student;
        this.assignment = assignment;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public Student getStudent() {
        return student;
    }

    public Assignment getAssignment() {
        return assignment;
    }
}
