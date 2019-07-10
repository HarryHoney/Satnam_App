package com.example.harpreet.myapplication;

public class Data {

    private String name;
    private String points;
    private String image_id;
    private String user_id;
    private String matches;

    public Data()
    {
        //empty constructor
    }

    public String getUser_id() {return user_id; }

    public void setUser_id(String user_id) { this.user_id = user_id; }

    public String getMatches() { return matches;   }

    public void setMatches(String matches) { this.matches = matches;   }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }






}
