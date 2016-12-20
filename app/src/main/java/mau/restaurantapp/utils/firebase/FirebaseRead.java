package mau.restaurantapp.utils.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mau.restaurantapp.data.AppData;
import mau.restaurantapp.data.types.CartContent;
import mau.restaurantapp.data.types.MenuTab;
import mau.restaurantapp.data.types.Order;
import mau.restaurantapp.data.types.Product;
import mau.restaurantapp.data.types.UserProfileType;

/**
 * Created by Yoouughurt on 14-12-2016.
 */

public class FirebaseRead {

    /**
     * Retrieves the Menu-Tabs and saves it in the application-object.
     */
    public static void getTabs() {
        DatabaseReference ref = AppData.firebaseDatabase.getReference("menutabs/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<MenuTab> newTabs = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MenuTab menuTab = dataSnapshot.getValue(MenuTab.class);

                    Log.d("getTabs", menuTab.getName() + menuTab.getPosition() + menuTab.isActive());
                    newTabs.add(menuTab);
                }
                AppData.tabs = newTabs;
                Collections.sort(AppData.tabs);
                Log.d("getTabs", "Tabs Added");
                AppData.event.tabsChanged();
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
    public static void getUserProfile() {
        DatabaseReference ref = AppData.firebaseDatabase.getReference("users/" + AppData.firebaseAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    AppData.userProfileType = snapshot.getValue(UserProfileType.class);
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
    public static void getPendingOrders() {

    }

    /**
     * Admin function.
     * Retrieves all users orders.
     *
     * @return An ArrayList with all users orders.
     */
    public static ArrayList<Order> getAllOrders() {
        DatabaseReference ref = AppData.firebaseDatabase.getReference("users");
        final ArrayList<Order> orders = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
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
     * Admin function.
     * Retrieves all current products
     * @return An ArrayList with all current products.
     */
    public static ArrayList<Product> getAllProducts() {
        DatabaseReference ref = AppData.firebaseDatabase.getReference("menutabs");
        final ArrayList<Product> products = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot menuSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot productSnapshot : menuSnapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            products.add(product);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GetAllProducts() error", databaseError.getMessage());
            }
        });
        return products;
    }

    /**
     * Retrieves the current users orders
     *
     * @return An arraylist with all their orders.
     */
    public static ArrayList<Order> getCurrentUsersOrders() {
        DatabaseReference ref = AppData.firebaseDatabase.getReference("users/" + AppData.firebaseAuth.getCurrentUser().getUid());
        final ArrayList<Order> orders = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot keySnapshot : snapshot.getChildren()) {
                        for (DataSnapshot orderSnapshot : keySnapshot.getChildren()) {
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
    public static void getShopSettings() {
        DatabaseReference ref = AppData.firebaseDatabase.getReference("restaurantsettings/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Object> shopSettings = (Map) snapshot.getValue();
                    AppData.OPENHOUR = (int) (long) shopSettings.get("openHour");
                    AppData.OPENMINUT = (int) (long) shopSettings.get("openMinutes");
                    AppData.CLOSEHOUR = (int) (long) shopSettings.get("closeHour");
                    AppData.CLOSEMINUT = (int) (long) shopSettings.get("closeMinutes");
                    AppData.shopOpen = (boolean) shopSettings.get("shopOpen");
                    AppData.mainText = (String) shopSettings.get("mainText");
                    AppData.address = (String) shopSettings.get("address");
                    AppData.emailAddress = (String) shopSettings.get("emailAddress");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GetOpeningHours() error", databaseError.getMessage());
            }
        });
    }

    public static void getCartContent() {
        DatabaseReference ref = AppData.firebaseDatabase.getReference("shoppingcart/" + AppData.firebaseAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Product> cartContent = new HashMap<String, Product>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    cartContent.put(snapshot.getKey(), product);
                }

                AppData.cartContent = new CartContent(cartContent);
                if (AppData.cartContent != null) {
                    AppData.setNewPrice(AppData.cartContent.getTotalPrice());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getCartContent", "Error" + databaseError);
            }
        });
    }

}
