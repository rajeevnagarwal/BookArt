package com.example.himaneeshchhabra.loginproject;

/**
 * Created by HARSHIT on 15-Oct-16.
 */
public class Book {
    private String Name;
    private Integer available_for_borrow;
    private Integer available_for_renting;
    private String image;
    private String bid;
    public String getBid(){
        return this.bid;
    }
    public String getImg(){
        return this.image;
    }
    public Book(String name,Integer ab,Integer ar,String img,String bid){
        this.Name=name;
        this.available_for_borrow=ab;
        this.available_for_renting=ar;
        this.image=img;
        this.bid=bid;
    }
    public void setName(String name){
        this.Name=name;
    }
    public void setAvailable_for_borrow(Integer availableForBorrow){
        this.available_for_borrow=availableForBorrow;
    }
    public void setAvailable_for_renting(Integer availableForRenting){
        this.available_for_renting=availableForRenting;
    }
    public String getName(){
        return this.Name;
    }
    public Integer getAvailable_for_borrow(){
        return this.available_for_borrow;
    }
    public Integer getAvailable_for_renting(){
        return this.available_for_renting;
    }
}
