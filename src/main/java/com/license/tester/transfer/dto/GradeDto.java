package com.license.tester.transfer.dto;

import javax.validation.constraints.NotNull;

public class GradeDto {

    private String id;

    private double grade;

    @NotNull
    private String studentId;

    @NotNull
    private String assignmentId;

    private String assignmentName;

    public GradeDto() {
    }

    public GradeDto(@NotNull String studentId, @NotNull String assignmentId) {
        this.studentId = studentId;
        this.assignmentId = assignmentId;
    }

    public GradeDto(double grade) {
        this.grade = grade;
    }

    public GradeDto(double grade, @NotNull String studentId, @NotNull String assignmentId) {
        this.grade = grade;
        this.studentId = studentId;
        this.assignmentId = assignmentId;
    }

    public GradeDto(String id, double grade, @NotNull String studentId, @NotNull String assignmentId) {
        this.id = id;
        this.grade = grade;
        this.studentId = studentId;
        this.assignmentId = assignmentId;
    }

    public String getId() {
        return id;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }
}
