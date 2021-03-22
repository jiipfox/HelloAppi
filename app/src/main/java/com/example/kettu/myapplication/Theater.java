package com.example.kettu.myapplication;

public class Theater {

    private String id = "";
    private String name = "";

    public Theater(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
