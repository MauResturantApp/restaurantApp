package mau.restaurantapp.user;

import java.util.ArrayList;

import mau.restaurantapp.data.types.Order;
import mau.restaurantapp.data.types.Product;

/**
 * Created by AnwarC on 12/11/2016.
 */

public class LoggedInUser {
    private String name;
    private String email;
    private String phoneNumber;
    private boolean admin = false;
    private ArrayList<Order> oldOrders = new ArrayList<>();
    private ArrayList<Product> cart = new ArrayList<>();
    private int points;
    private boolean anonymous = false;

    public LoggedInUser() {
        anonymous = true;
    }

    public LoggedInUser(String email, String name, int points) {
        this.email = email;
        this.name = name;
        this.points = points;
    }

    public ArrayList<Order> getOldOrders() {
        return oldOrders;
    }

    public void setOldOrders(ArrayList<Order> oldOrders) {
        this.oldOrders = oldOrders;
    }

    public ArrayList<Product> getCart() {
        return cart;
    }

    public void setCart(ArrayList<Product> cart) {
        this.cart = cart;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAnonymous() {
        return anonymous;
    }
}
