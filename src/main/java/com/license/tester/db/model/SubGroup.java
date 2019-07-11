package com.license.tester.db.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class SubGroup {

    @Id
    private UUID id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subGroup", cascade = CascadeType.ALL)
    private final List<Student> students;

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    public SubGroup() {
        this.id = UUID.randomUUID();
        this.students = new ArrayList<>();
    }

    public SubGroup(String name, Teacher teacher) {
        this();
        this.name = name;
        this.teacher = teacher;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
