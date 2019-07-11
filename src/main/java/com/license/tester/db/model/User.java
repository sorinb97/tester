package com.license.tester.db.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class User {

    @Id
    private UUID id;

    private String name;

    private String email;

    private String password;

    public User() {
        this.id = UUID.randomUUID();
    }

    public User(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String password) {
        this();
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
