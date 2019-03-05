package com.mau.dalvi.p4compass;

public class User {

    private String name, password;
    private int steps;

    public User(String name, String password, int steps){
        this.name = name;
        this.password = password;
        this.steps = steps;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }
    public User(int steps){
        this.steps = steps;
    }

    public void setSteps(int steps){
        this.steps = steps;
    }

    public int getSteps(){
        return steps;
    }
}
