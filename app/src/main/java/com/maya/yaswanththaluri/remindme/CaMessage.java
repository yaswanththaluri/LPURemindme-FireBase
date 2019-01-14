package com.maya.yaswanththaluri.remindme;

public class CaMessage {
    private String course;
    private String date;
    private String type;
    private String syllabus;
    private String group;

    public CaMessage() {

    }

    public CaMessage(String course, String type, String syllabus, String date, String group) {
        this.course = course;
        this.date = date;
        this.type = type;
        this.syllabus = syllabus;
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }


}