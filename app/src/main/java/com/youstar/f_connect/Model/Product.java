package com.youstar.f_connect.Model;

/**
 * Created by odera on 3/6/2018.
 */

public class Product {
    String productname,price,postedby,postedon,quantity,desc,imagelink;

    public String getImagelink() {
        return imagelink;
    }

    public Product() {
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public Product(String productname, String price, String postedby, String postedon, String quantity, String desc) {
        this.productname = productname;
        this.price = price;
        this.postedby = postedby;
        this.postedon = postedon;
        this.quantity = quantity;
        this.desc = desc;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPostedby() {
        return postedby;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

    public String getPostedon() {
        return postedon;
    }

    public void setPostedon(String postedon) {
        this.postedon = postedon;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
