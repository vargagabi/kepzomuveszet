package com.example.kepzomuveszet;

public class MyItem {
    private String id;
    private String name;
    private String description;
    private int price;
    private String amount;
//    private String imgRes;

    public MyItem(String name, String description, int price, String amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
//        this.imgRes = imgRes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getImgRes() {
//        return imgRes;
//    }
//
//    public void setImgRes(String imgRes) {
//        this.imgRes = imgRes;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
