package com.finalyear.accesify;

public class upload_notice

{
    public  String name;
    public  String image;
    public  String roll_number;
    public String thumb_image;

    public upload_notice(){

    }

    public upload_notice(String name, String image, String roll_number, String thumb_image) {
        this.name = name;
        this.image = image;
        this.roll_number = roll_number;
        this.thumb_image=thumb_image;
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




}
