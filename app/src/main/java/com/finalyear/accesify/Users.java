package com.finalyear.accesify;

public class Users

{
public  String name;
public String info;
public  String image;
public  String roll_number;
public String thumb_image;
public String notice;
public String marks;
public String subject;
public String number;
public String assignment;
public String date;
public String attendancestatus;

public Users(){

}




    public Users(String name, String image, String roll_number, String thumb_image, String info, String notice, String subject, String marks, String number, String assignment, String date, String attendancestatus) {
        this.name = name;
        this.image = image;
        this.roll_number = roll_number;
        this.thumb_image=thumb_image;
        this.info=info;
        this.notice=notice;
        this.marks=marks;
        this.subject=subject;
        this.number=number;
        this.assignment=assignment;
        this.date=date;
        this.attendancestatus=attendancestatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRollno() {
        return roll_number;
    }

    public void setRollno(String status) {
        this.roll_number = roll_number;
    }
    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getNotice() {
        return info;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
    public String getMarks()
    {
        return marks;
    }
    public void setMarks()
    {
        this.marks=marks;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNumber() {
        return number;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getAttendancestatus() {
        return attendancestatus;
    }

    public void setAttendancestatus(String attendancestatus) {
        this.attendancestatus = attendancestatus;
    }




}
