package com.license.tester.db.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    private SubGroup subGroup;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private List<Grade> grades;

    public Student() {
        this.grades = new ArrayList<>();
    }

    public Student(String name, String email, SubGroup subGroup) {
        super(name, email);
        this.grades = new ArrayList<>();
        this.subGroup = subGroup;
    }

    public Student(String name, String email, String password, SubGroup subGroup) {
        super(name, email, password);
        this.subGroup = subGroup;
        this.grades = new ArrayList<>();
    }

    public SubGroup getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(SubGroup subGroup) {
        this.subGroup = subGroup;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}
