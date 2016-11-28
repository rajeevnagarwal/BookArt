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
   //private Integer balance;
    public User(String name,String contact,String age,String img,String username){
        this.Name=name;
        this.Contact=contact;
        this.Age=age;
        this.image=img;
        this.username=username;
        //this.balance=bal;
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
   /* public Integer getBalance(){
        return this.balance;}
    public boolean isPayable(Integer amt){
        if(this.balance>=amt){
            return true;
        }
        return false;
    }
    public void addBalance(Integer bal){
        this.balance=this.balance+bal;
    }
    public void deductBalance(Integer bal){
        this.balance=this.balance-bal;
    }*/
}
