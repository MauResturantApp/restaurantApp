package mau.resturantapp.utils.Firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import mau.resturantapp.data.MenuTab;
import mau.resturantapp.data.Order;
import mau.resturantapp.data.Product;
import mau.resturantapp.data.ShoppingCartItem;
import mau.resturantapp.data.UserProfile;
import mau.resturantapp.data.appData;

/**
 * Created by Yoouughurt on 14-12-2016.
 */

public class FirebaseWrite {

    /**
     * Admin function.
     * Adds a new Menu-Tab with the given variables.
     * @param name The name of the Menu-Tab
     * @param position The position of the Menu-Tab
     * @param active Whether or not it should be active
     */
    public static void addTab(String name, int position, String active) {
        DatabaseReference ref = appData.firebaseDatabase.getReference("menutabs/");
        MenuTab menuTab = new MenuTab(name, position, appData.stringToBoolean(active));
        String key = ref.push().getKey();
        menuTab.setKey(key);
        ref.child(key).setValue(menuTab);
    }

    /**
     * Admin function.
     * Removes the Menu-Tab and all products associated with it.
     * @param key The key of the Menu-Tab
     */
    public static void removeTab(String key) {
        DatabaseReference ref = appData.firebaseDatabase.getReference();

        Map removeTab = new HashMap();
        removeTab.put("menutabs/" + key, null);
        removeTab.put("product/" + key, null);

        ref.updateChildren(removeTab);
    }

    /**
     * Admin function.
     * Updates a Menu-Tab with the given values
     * @param name The name of the Menu-Tab
     * @param position The position of the Menu-Tab
     * @param active Whether or not it should be active
     * @param key The key of the Menu-Tab
     */
    public static void updateTab(String name, int position, String active, String key) {
        DatabaseReference ref = appData.firebaseDatabase.getReference("menutabs/" + key);
        MenuTab menuTab = new MenuTab(name, position, appData.stringToBoolean(active));
        menuTab.setKey(key);
        ref.setValue(menuTab);
    }

    /**
     * Saves the given product in the current users shoppingcart.
     * @param product The product to save
     */
    public static void addProductToCart(Product product) {
        DatabaseReference ref = appData.firebaseDatabase.getReference("shoppingcart/" + appData.firebaseAuth.getCurrentUser().getUid());
        DatabaseReference newRef = ref.push();
        String key = newRef.getKey();
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem(product.getName(), product.getPrice(), key);
        newRef.setValue(shoppingCartItem);
    }

    /**
     * Removes a product from the current users shoppingcart.
     * @param key The key of the product to remove
     */
    public static void removeProductFromCart(final String key) {
        DatabaseReference ref = appData.firebaseDatabase.getReference("shoppingcart/" + appData.firebaseAuth.getCurrentUser().getUid() + "/" + key);
        ref.removeValue();
    }


    /**
     * Handles the placing order flow.
     * Retrieves the current users shoppingcart and generates an Order object.
     * Proceeds to then remove the items from the shoppingcart.
     * And calls the placeOrderInFirebase Function with the Order.
     * @param comment
     * @param timeToPickup
     */
    public static void placeOrder(final String comment, final String timeToPickup){
        final DatabaseReference ref = appData.firebaseDatabase.getReference("shoppingcart/" + appData.firebaseAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Product> cartContent = new HashMap<>();
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        String key = productSnapshot.getKey();
                        Product product = productSnapshot.getValue(Product.class);
                        Log.d("CartContent: ", "key " + key + " name " + product.getName() + " price " + product.getPrice());
                        cartContent.put(key, product);
                    }
                    ref.removeValue();
                    Order order = new Order(cartContent, appData.totalPrice, comment, timeToPickup, ServerValue.TIMESTAMP);
                    placeOrderInFirebase(order);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("place order error", databaseError.getMessage());
            }
        });
    }

    /**
     * Places an order
     * @param order The order to be saved
     */
    private static void placeOrderInFirebase(Order order){
        DatabaseReference ref = appData.firebaseDatabase.getReference("orders/" + appData.firebaseAuth.getCurrentUser().getUid());
        ref.push().setValue(order);
        appData.event.orderSuccessful();
    }

    /**
     * Sets or updates a users profile.
     * @param name Their name
     * @param phoneNumber Their phone number
     */
    public static void updateUserProfile(String name, String phoneNumber){
        DatabaseReference ref = appData.firebaseDatabase.getReference("users/" + appData.firebaseAuth.getCurrentUser().getUid());
        if(appData.userProfile != null){
            appData.userProfile.setName(name);
            appData.userProfile.setPhoneNumber(phoneNumber);
        } else {
            appData.userProfile = new UserProfile(appData.firebaseAuth.getCurrentUser().getEmail(), name, phoneNumber);
        }

        ref.setValue(appData.userProfile);
    }

    /**
     * Admin function.
     * Change the opening hours of the shop.
     * @param openHour The opening hour
     * @param openMinutes The opening minutes
     * @param closeHour The closing hour
     * @param closeMinutes The closing minutes
     */
    public static void setOpeningHours(int openHour, int openMinutes, int closeHour, int closeMinutes){
        DatabaseReference ref = appData.firebaseDatabase.getReference("restaurantsettings/");
        Map<String, Object> openingHours = new HashMap<>();
        openingHours.put("openHour", openHour);
        openingHours.put("openMinutes", openMinutes);
        openingHours.put("closeHour", closeHour);
        openingHours.put("closeMinutes", closeMinutes);
        ref.updateChildren(openingHours);
    }

    /**
     * Admin function.
     * Saves a new product to a given tab.
     * @param name The name of the product
     * @param price The price of the product
     * @param key The key of the menu-tab to add it to.
     */
    public static void addProductToTab(String name, int price, String key){
        DatabaseReference ref = appData.firebaseDatabase.getReference("product/" + key);
        Product product = new Product(name, price);
        String productKey = ref.push().getKey();
        product.setKey(productKey);
        ref.child(productKey).setValue(product);
    }

    /**
     * Admin function.
     * Removes a product from the given menu-tab.
     * @param tabKey The key of the menu-tab
     * @param product The product to be removed
     */
    public static void removeProductFromTab(String tabKey, Product product){
        DatabaseReference ref = appData.firebaseDatabase.getReference("product/" + tabKey + "/" + product.getKey());
        ref.removeValue();
    }

    /**
     * Admin function.
     * Saves whether or not the shop is open or closed.
     * @param open Shop open or closed.
     */
    public static void setShopOpenOrClosed(boolean open){
        DatabaseReference ref = appData.firebaseDatabase.getReference("restaurantsettings/");
        Map<String, Object> update = new HashMap<>();
        update.put("shopOpen", open);
        ref.updateChildren(update);
    }

    /**
     * Admin function.
     * Saves the main text for the homepage
     * @param text Text for the homepage.
     */
    public static void setShopMainText(String text){
        DatabaseReference ref = appData.firebaseDatabase.getReference("restaurantsettings/");
        Map<String, Object> update = new HashMap<>();
        update.put("mainText", text);
        ref.updateChildren(update);
    }

    /**
     * Admin function
     * Saves the restaurants address.
     * @param address The address
     */
    public static void setAddress(String address){
        DatabaseReference ref = appData.firebaseDatabase.getReference("restaurantsettings/");
        Map<String, Object> update = new HashMap<>();
        update.put("address", address);
        ref.updateChildren(update);
    }

    /**
     * Admin function.
     * Saves the email address where you want to be contacted.
     * @param emailAddress The email address.
     */
    public static void setEmailAddress(String emailAddress){
        DatabaseReference ref = appData.firebaseDatabase.getReference("restaurantsettings/");
        Map<String, Object> update = new HashMap<>();
        update.put("emailAddress", emailAddress);
        ref.updateChildren(update);
    }
}
