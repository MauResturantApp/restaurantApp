package mau.resturantapp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoouughurt on 14-11-2016.
 */

public class CartContent {

    private List<Product> products = new ArrayList<>();

    public CartContent(){

    }


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
