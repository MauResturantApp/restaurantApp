package mau.resturantapp.data;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Yoouughurt on 05-11-2016.
 */

public class Order {

    private Map<String, Product> cartContent;
    private int totalPrice;
    private String comment;
    private String timeToPickup;
    private Object timestamp;

    public Order(){

    }

    public Order(Map<String, Product> cartContent, int totalPrice, String comment, String timeToPickup, Object timestamp){
        this.cartContent = cartContent;
        this.totalPrice = totalPrice;
        this.comment = comment;
        this.timeToPickup = timeToPickup;
        this.timestamp = timestamp;
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

    @Exclude
    public Date getTimestampAsDate(){
        Date date = new Date((Long)timestamp);
        //Handle Locale for multi-language later
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sfd.format(date);
        return date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, Product> getCartContent() {
        return cartContent;
    }

    public void setCartContent(Map<String, Product> cartContent) {
        this.cartContent = cartContent;
    }

    public String getTimeToPickup() {
        return timeToPickup;
    }

    public void setTimeToPickup(String timeToPickup) {
        this.timeToPickup = timeToPickup;
    }
}
