package com.license.tester.db.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Assignment {

    @Id
    private UUID id;

    private String name;

    private String interfaceFileId;

    private String jUnitFileId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignment", cascade = CascadeType.ALL)
    private final List<Grade> grades;

    public Assignment() {
        this.id = UUID.randomUUID();
        this.grades = new ArrayList<>();
    }

    public Assignment(String name, String interfaceFileId, String jUnitFileId) {
        this();
        this.name = name;
        this.interfaceFileId = interfaceFileId;
        this.jUnitFileId = jUnitFileId;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getInterfaceFileId() {
        return interfaceFileId;
    }

    public String getJUnitFileId() {
        return jUnitFileId;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setInterfaceFileId(String interfaceFileId) {
        this.interfaceFileId = interfaceFileId;
    }

    public void setJUnitFileId(String jUnitFileId) {
        this.jUnitFileId = jUnitFileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
