package mau.resturantapp.data;

import java.util.ArrayList;

/**
 * Created by Yoouughurt on 05-11-2016.
 */

public class Order {

    private ArrayList<Product> products;
    private int totalPrice;
    private Object timestamp;

    public Order(){

    }

    public Order(ArrayList<Product> products, int totalPrice, Object timestamp){
        this.products = products;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
