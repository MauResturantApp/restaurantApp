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

    public static void addTab(String name, int position, String active) {
        DatabaseReference ref = appData.firebaseDatabase.getReference("menutabs/");
        MenuTab menuTab = new MenuTab(name, position, appData.stringToBoolean(active));
        String key = ref.push().getKey();
        menuTab.setKey(key);
        ref.child(key).setValue(menuTab);
    }

    //Consider if transactions needed
    public static void removeTab(String key) {
        DatabaseReference ref = appData.firebaseDatabase.getReference();

        Map removeTab = new HashMap();
        removeTab.put("menutabs/" + key, null);
        removeTab.put("product/" + key, null);

        ref.updateChildren(removeTab);
    }

    public static void updateTab(String name, int position, String active, String key) {
        DatabaseReference ref = appData.firebaseDatabase.getReference("menutabs/" + key);
        MenuTab menuTab = new MenuTab(name, position, appData.stringToBoolean(active));
        menuTab.setKey(key);
        ref.setValue(menuTab);
    }

    public static void addProductToCart(Product product) {
        DatabaseReference ref = appData.firebaseDatabase.getReference("shoppingcart/" + appData.firebaseAuth.getCurrentUser().getUid());
        DatabaseReference newRef = ref.push();
        String key = newRef.getKey();
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem(product.getName(), product.getPrice(), key);
        newRef.setValue(shoppingCartItem);
    }

    public static void removeProductFromCart(final String key) {
        DatabaseReference ref = appData.firebaseDatabase.getReference("shoppingcart/" + appData.firebaseAuth.getCurrentUser().getUid() + "/" + key);
        ref.removeValue();
    }


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

    private static void placeOrderInFirebase(Order order){
        DatabaseReference ref = appData.firebaseDatabase.getReference("orders/" + appData.firebaseAuth.getCurrentUser().getUid());
        ref.push().setValue(order);
        appData.event.orderSuccessful();
    }

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

    public static void setOpeningHours(int openHour, int openMinutes, int closeHour, int closeMinutes){
        DatabaseReference ref = appData.firebaseDatabase.getReference("restaurantsettings/");
        Map<String, Object> openingHours = new HashMap<>();
        openingHours.put("openHour", openHour);
        openingHours.put("openMinutes", openMinutes);
        openingHours.put("closeHour", closeHour);
        openingHours.put("closeMinutes", closeMinutes);
        ref.updateChildren(openingHours);
    }

    public static void addProductToTab(String name, int price, String key){
        DatabaseReference ref = appData.firebaseDatabase.getReference("product/" + key);
        Product product = new Product(name, price);
        String productKey = ref.push().getKey();
        product.setKey(productKey);
        ref.child(productKey).setValue(product);
    }

    public static void removeProductFromTab(String tabKey, Product product){
        DatabaseReference ref = appData.firebaseDatabase.getReference("product/" + tabKey + "/" + product.getKey());
        ref.removeValue();
    }

    public static void setShopOpenOrClosed(boolean open){
        DatabaseReference ref = appData.firebaseDatabase.getReference("restaurantsettings/");
        Map<String, Object> update = new HashMap<>();
        update.put("shopOpen", open);
        ref.updateChildren(update);
    }

    public static void setShopMainText(String text){
        DatabaseReference ref = appData.firebaseDatabase.getReference("restaurantsettings/");
        Map<String, Object> update = new HashMap<>();
        update.put("mainText", text);
        ref.updateChildren(update);
    }

    public static void setAddress(String address){
        //TODO HVILKET FORMAT SKAL ADDRESSE VÆRE?
    }
}
