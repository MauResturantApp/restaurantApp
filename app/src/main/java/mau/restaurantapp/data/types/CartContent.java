package mau.restaurantapp.data.types;

import java.util.Map;

/**
 * Created by Yoouughurt on 14-11-2016.
 */

public class CartContent {

    private Map<String, Product> cartContent;

    public CartContent() {

    }

    public void setCartContent(Map<String, Product> entry) {
        this.cartContent = entry;
    }

    public CartContent(Map<String, Product> cartContent) {
        this.cartContent = cartContent;
    }


    public Map<String, Product> getCartContent() {
        return cartContent;
    }

    public int getTotalPrice() {
        int price = 0;
        if (cartContent != null) {
            for (Product product : cartContent.values()) {
                price += product.getPrice();
            }
        }
        return price;
    }
}
