package com.maya.yaswanththaluri.remindme;

public class Notificaation
{
    private String subject;
    private String description;
    private String date;

    public Notificaation()
    {

    }

    public Notificaation(String subject, String description,String date)
    {
        this.subject = subject;
        this.description = description;
        this.date = date;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getDescription()
    {
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
