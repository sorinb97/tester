package com.license.tester.db.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Teacher extends User {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teacher")
    private final List<SubGroup> subGroups;

    public Teacher() {
        subGroups = new ArrayList<>();
    }

    public Teacher(String name, String email) {
        super(name, email);
        this.subGroups = new ArrayList<>();
    }

    public Teacher(String name, String email, String password) {
        super(name, email, password);
        this.subGroups = new ArrayList<>();
    }

    public List<SubGroup> getSubGroups() {
        return subGroups;
    }

}
