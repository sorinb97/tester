package com.license.tester.transfer.dto;

public class GradeShowDto {
    private String id;

    private String studentName;

    private double grade;

    public GradeShowDto(String id, String studentName, double grade) {
        this.id = id;
        this.studentName = studentName;
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public double getGrade() {
        return grade;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
