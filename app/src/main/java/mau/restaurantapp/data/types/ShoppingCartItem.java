package mau.restaurantapp.data.types;

/**
 * Created by Yoouughurt on 22-11-2016.
 */

public class ShoppingCartItem {
    private String name;
    private int price;
    private String key;
    //maybe some accessories list here

    public ShoppingCartItem() {

    }

    public ShoppingCartItem(String name, int price, String key) {
        this.name = name;
        this.price = price;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getKey() {
        return key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
