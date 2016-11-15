package mau.resturantapp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoouughurt on 05-11-2016.
 */

public class Order {

    private List<Product> products = new ArrayList<>();
    private int totalPrice;
    private Object timestamp;

    public Order(){

    }

    public Order(ArrayList<Product> products, int totalPrice, Object timestamp){
        this.products = products;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
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
