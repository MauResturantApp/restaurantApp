package mau.resturantapp.data;

import com.google.firebase.database.Exclude;

/**
 * Created by Marianne H on 03-11-2016.
 */

public class Product {
    private String name;
    private int price;
    private String key;

    public Product(){

    }

    public Product(String name, int price){
        this.name = name;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
