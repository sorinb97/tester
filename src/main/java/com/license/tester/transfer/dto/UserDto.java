package com.license.tester.transfer.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UserDto {

    private String id;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Min(6)
    private String password;

    @NotNull
    private String name;

    private String groupId;

    private String role;

    public UserDto() {
    }

    public UserDto(@NotNull String name, @NotNull @Email String email, @NotNull @Min(6) String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserDto(@NotNull String name, @NotNull @Email String email, @NotNull @Min(6) String password, String groupId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.groupId = groupId;
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

    public String getId() {
        return id;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
