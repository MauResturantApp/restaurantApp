package mau.resturantapp.data;

import java.util.ArrayList;

/**
 * Created by Yoouughurt on 14-11-2016.
 */

public class CartContent {

    private ArrayList<Product> products = new ArrayList<>();

    public CartContent(){

    }


    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
