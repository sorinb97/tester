package com.license.tester.transfer.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class TeacherDto {

    private String id;

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    public TeacherDto() {
    }

    public TeacherDto(@NotNull String name, @NotNull @Email String email) {
        this.name = name;
        this.email = email;
    }

    public TeacherDto(String id, @NotNull String name, @NotNull @Email String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
