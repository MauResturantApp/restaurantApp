package mau.resturantapp.utils.Firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mau.resturantapp.data.CartContent;
import mau.resturantapp.data.MenuTab;
import mau.resturantapp.data.Order;
import mau.resturantapp.data.Product;
import mau.resturantapp.data.UserProfile;
import mau.resturantapp.data.appData;

/**
 * Created by Yoouughurt on 14-12-2016.
 */

public class FirebaseRead {

    /**
     * Retrieves the Menu-Tabs and saves it in the application-object.
     */
    public static void getTabs(){
        DatabaseReference ref = appData.firebaseDatabase.getReference("menutabs/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<MenuTab> newTabs = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MenuTab menuTab = dataSnapshot.getValue(MenuTab.class);

                    Log.d("getTabs", menuTab.getName() + menuTab.getPosition() + menuTab.isActive());
                    newTabs.add(menuTab);
                }
                appData.tabs = newTabs;
                Collections.sort(appData.tabs);
                Log.d("getTabs", "Tabs Added");
                appData.event.tabsChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Get tabs", "Exception " + databaseError);
            }
        });
    }

    /**
     * Retrieves a users profile and saves it in the application-object
     */
    public static void getUserProfile(){
        DatabaseReference ref = appData.firebaseDatabase.getReference("users/" + appData.firebaseAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    appData.userProfile = snapshot.getValue(UserProfile.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GetUserProfile error", databaseError.getMessage());
            }
        });
    }

    /**
     * Admin function.
     * Returns pending orders.
     * Not implemented yet.
     */
    @Deprecated
    public static void getPendingOrders(){

    }

    /**
     * Admin function.
     * Retrieves all users orders.
     * @return An ArrayList with all users orders.
     */
    public static ArrayList<Order> getAllOrders(){
        DatabaseReference ref = appData.firebaseDatabase.getReference("users");
        final ArrayList<Order> orders = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot userSnapshot: snapshot.getChildren()){
                        for(DataSnapshot orderSnapshot: userSnapshot.getChildren()){
                            Order order = orderSnapshot.getValue(Order.class);
                            orders.add(order);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GetUserOrders() error", databaseError.getMessage());
            }
        });
        return orders;
    }

    /**
     * Retrieves the current users orders
     * @return An arraylist with all their orders.
     */
    public static ArrayList<Order> getCurrentUsersOrders(){
        DatabaseReference ref = appData.firebaseDatabase.getReference("users/" + appData.firebaseAuth.getCurrentUser().getUid());
        final ArrayList<Order> orders = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot keySnapshot: snapshot.getChildren()){
                        for(DataSnapshot orderSnapshot: keySnapshot.getChildren()){
                            Order order = orderSnapshot.getValue(Order.class);
                            orders.add(order);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GetUserOrders() error", databaseError.getMessage());
            }
        });
        return orders;
    }

    /**
     * Retrieves the shopSettings and saves it in the application-object.
     */
    public static void getShopSettings(){
        DatabaseReference ref = appData.firebaseDatabase.getReference("restaurantsettings/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Object> shopSettings = (Map) snapshot.getValue();
                    appData.OPENHOUR = (int) (long) shopSettings.get("openHour");
                    appData.OPENMINUT = (int) (long) shopSettings.get("openMinutes");
                    appData.CLOSEHOUR = (int) (long) shopSettings.get("closeHour");
                    appData.CLOSEMINUT = (int) (long) shopSettings.get("closeMinutes");
                    appData.shopOpen = (boolean) shopSettings.get("shopOpen");
                    appData.mainText = (String) shopSettings.get("mainText");
                    appData.address = (String) shopSettings.get("address");
                    appData.emailAddress = (String) shopSettings.get("emailAddress");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GetOpeningHours() error", databaseError.getMessage());
            }
        });
    }

    public static void getCartContent(){
        DatabaseReference ref = appData.firebaseDatabase.getReference("shoppingcart/" + appData.firebaseAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Product> cartContent = new HashMap<String, Product>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Product product = snapshot.getValue(Product.class);
                    cartContent.put(snapshot.getKey(), product);
                }

                appData.cartContent = new CartContent(cartContent);
                if(appData.cartContent != null) {
                    appData.setNewPrice(appData.cartContent.getTotalPrice());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getCartContent", "Error" + databaseError);
            }
        });
    }

}
