package com.license.tester.transfer.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class StudentDto {

    private String id;

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String groupId;

    public StudentDto() {
    }

    public StudentDto(@NotNull String name, @NotNull @Email String email, @NotNull String groupId) {
        this.name = name;
        this.email = email;
        this.groupId = groupId;
    }

    public StudentDto(String id, @NotNull String name, @NotNull @Email String email, @NotNull String groupId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.groupId = groupId;
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

    public String getGroupId() {
        return groupId;
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

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
