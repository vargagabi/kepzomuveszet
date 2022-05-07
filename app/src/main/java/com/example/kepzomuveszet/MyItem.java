package com.example.kepzomuveszet;

public class MyItem {
    private String id;
    private String name;
    private String description;
    private int price;
    private int amount;

    @Override
    public String toString() {
        return "MyItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }

    public MyItem(String id,String name, String description, int price, int amount) {
        this.id=id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
//        this.imgRes = imgRes;
    }

    public MyItem(String name, String description, int price, int amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    String getId() {
        return id;
    }

    void setId(String id) {
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
