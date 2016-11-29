package com.example.himaneeshchhabra.loginproject;

/**
 * Created by HARSHIT on 15-Oct-16.
 */
public class User {
    private String Name;
    private String Contact;
    private String Age;
    private String image;
    private String username;
    public User(String name,String contact,String age,String img,String username){
        this.Name=name;
        this.Contact=contact;
        this.Age=age;
        this.image=img;
        this.username=username;
    }
    public String getName(){
        return this.Name;
    }
    public String getContact(){
        return this.Contact;
    }
    public String getAge(){
        return this.Age;
    }
    public String getIMG(){
        return this.image;
    }
    public String getUsername(){
        return this.username;
    }

}
