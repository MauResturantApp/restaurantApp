package mau.resturantapp.utils.Firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import mau.resturantapp.data.MenuTab;
import mau.resturantapp.data.Order;
import mau.resturantapp.data.UserProfile;
import mau.resturantapp.data.appData;

/**
 * Created by Yoouughurt on 14-12-2016.
 */

public class FirebaseRead {

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

    @Deprecated
    public static void getPendingOrders(){
        DatabaseReference ref = appData.firebaseDatabase.getReference("pendingOrders");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    appData.userProfile = snapshot.getValue(UserProfile.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GetPendingOrders error", databaseError.getMessage());
            }
        });
    }

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


}
