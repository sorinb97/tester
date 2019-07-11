package com.license.tester.transfer.dto;

import javax.validation.constraints.NotNull;

public class AssignmentDto {

    private String id;

    @NotNull
    private String name;

    private String interfaceFileId;

    private String interfacePackage;

    private String jUnitFileId;

    private String jUnitPackage;

    private String interfaceCode;

    public AssignmentDto() {
    }

    public AssignmentDto(@NotNull String name) {
        this.name = name;
    }

    public AssignmentDto(@NotNull String name, String interfaceFileId, String jUnitFileId) {
        this.name = name;
        this.interfaceFileId = interfaceFileId;
        this.jUnitFileId = jUnitFileId;
    }

    public AssignmentDto(String id, @NotNull String name, String interfaceFileId, String jUnitFileId) {
        this.id = id;
        this.name = name;
        this.interfaceFileId = interfaceFileId;
        this.jUnitFileId = jUnitFileId;
    }

    public AssignmentDto(@NotNull String name, String interfaceFileId, String interfacePackage, String jUnitFileId, String jUnitPackage) {
        this.name = name;
        this.interfaceFileId = interfaceFileId;
        this.interfacePackage = interfacePackage;
        this.jUnitFileId = jUnitFileId;
        this.jUnitPackage = jUnitPackage;
    }

    public AssignmentDto(String id, @NotNull String name, String interfaceFileId, String interfacePackage, String jUnitFileId, String jUnitPackage, String interfaceCode) {
        this.id = id;
        this.name = name;
        this.interfaceFileId = interfaceFileId;
        this.interfacePackage = interfacePackage;
        this.jUnitFileId = jUnitFileId;
        this.jUnitPackage = jUnitPackage;
        this.interfaceCode = interfaceCode;
    }

    public String getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public String getInterfaceFileId() {
        return interfaceFileId;
    }

    public String getjUnitFileId() {
        return jUnitFileId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInterfaceFileId(String interfaceFileId) {
        this.interfaceFileId = interfaceFileId;
    }

    public void setjUnitFileId(String jUnitFileId) {
        this.jUnitFileId = jUnitFileId;
    }

    public String getInterfacePackage() {
        return interfacePackage;
    }

    public String getJUnitPackage() {
        return jUnitPackage;
    }

    public void setInterfacePackage(String interfacePackage) {
        this.interfacePackage = interfacePackage;
    }

    public void setJUnitPackage(String jUnitPackage) {
        this.jUnitPackage = jUnitPackage;
    }

    public String getInterfaceCode() {
        return interfaceCode;
    }

    public void setInterfaceCode(String interfaceCode) {
        this.interfaceCode = interfaceCode;
    }
}
