package com.license.tester.transfer.dto;

import javax.validation.constraints.NotNull;

public class SubGroupDto {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String teacherId;

    public SubGroupDto() {
    }

    public SubGroupDto(String id, @NotNull String name, @NotNull String teacherId) {
        this.id = id;
        this.name = name;
        this.teacherId = teacherId;
    }

    public SubGroupDto(@NotNull String name, @NotNull String teacherId) {
        this.name = name;
        this.teacherId = teacherId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}
