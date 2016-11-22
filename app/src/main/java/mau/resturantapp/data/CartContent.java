package mau.resturantapp.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yoouughurt on 14-11-2016.
 */

public class CartContent {

    private Map<String, Product> cartContent;

    public CartContent(){

    }

    public CartContent(Map<String, Product> cartContent){
        this.cartContent = cartContent;
    }


    public Map<String, Product> getCartContent() {
        return cartContent;
    }
}
