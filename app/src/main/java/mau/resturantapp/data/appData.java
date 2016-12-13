package mau.resturantapp.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mau.resturantapp.adapters.Cartcontent_adapter;
import mau.resturantapp.event.EventCreator;
import mau.resturantapp.event.events.NewUserFailedEvent;
import mau.resturantapp.event.events.NewUserSuccesfullEvent;
import mau.resturantapp.event.events.OnFailedLogIn;
import mau.resturantapp.event.events.OnSuccesfullLogInEvent;
import mau.resturantapp.user.LoggedInUser;
import mau.resturantapp.utils.LanguageContextWrapper;

/**
 * Created by anwar on 10/15/16.
 */

public class appData extends Application {
    private static MenuTabs tempItem = new MenuTabs();
    public static EventCreator event = new EventCreator();
    public static ArrayList<Product> cartContent = new ArrayList<>();
    public static String[] currentTabs = tempItem.getTabs();
    public static LoggedInUser currentUser;
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static FirebaseUser firebaseUser;
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseAuth anonymousAuth;
    public static SharedPreferences appPrefs;
    public static List<Runnable> priceObservers = new ArrayList<Runnable>();
    public static int totalPrice = 0;


    // Denne værdig skal hentes fra Firebase, så kan man lukke/åbne resturanten fra selve admincontrols.
    public static boolean shopOpen = true;

    public static Cartcontent_adapter adapter = new Cartcontent_adapter();

    public static int OPENHOUR = 11; //dette laves om senere til at admin kan skifte, men for now tester jeg bare.
    public static int OPENMINUT = 00;
    public static int CLOSEHOUR = 21;
    public static int CLOSEMINUT = 00;

    //New Cart in progress
    public static CartContent shoppingCart;
    public static boolean loggingIn = false;

    //MenuTabs in progress
    public static ArrayList<MenuTab> tabs = new ArrayList<>();

    public static UserProfile userProfile;


    private static void setNewPrices(){
        for(int i = 0; i<priceObservers.size();i++){
            priceObservers.get(i).run();
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();

        appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setPrefs();
        getOpeningHours();

        //Maybe move this depending on how we structure the security rules in firebase
        getTabs();
    }

    public static void closeShop(){
        shopOpen = false;
    }
    public static void openShop(){
        shopOpen = true;
    }


    private void setPrefs() {
        if (!appPrefs.contains("mainColor")) {
            appPrefs.edit().putString("mainColor", "#f23423")
                    .putString("textColor", "#f123456").commit();
        }
    }

    public static int getTotalPrice() {
        int totalprice = 0;

        for (int i = 0; i < appData.cartContent.size(); i++) {
            totalprice += appData.cartContent.get(i).getPrice();
        }

        return totalprice;
    }


    //These functions can potentially be moved to a Firebase related singleton.

    public static boolean isLoggedIn() {
        if (firebaseAuth.getCurrentUser() != null) {
            return true;
        }

        return false;
    }

    @Nullable
    public static String getUID() {
        if (isLoggedIn()) {
            return firebaseAuth.getCurrentUser().getUid();
        }

        return null;
    }

    private static void onSuccesfullLogin() {
        String name = firebaseAuth.getCurrentUser().getDisplayName();
        String email = firebaseAuth.getCurrentUser().getEmail();
        currentUser = new LoggedInUser(name, email, 0);
        getUserProfile();
        isAdmin();
    }

    public static void isAdmin() {
        DatabaseReference ref = firebaseDatabase.getReference("permissions/" + getUID());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentUser.setAdmin(true);
                    Log.d("Logged in as admin : ", "" + currentUser.isAdmin());
                    event.isAdmin();
                }
                  //Succesfull Login used to clean up recyclerviewAdapter in CartContentFirebase
                  event.succesfullLogin();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Admin check error", databaseError.getMessage());
            }
        });
    }

    public static void addTab(String name, int position, String active) {
        DatabaseReference ref = firebaseDatabase.getReference("menutabs/");
        MenuTab menuTab = new MenuTab(name, position, stringToBoolean(active));
        String key = ref.push().getKey();
        menuTab.setKey(key);
        ref.child(key).setValue(menuTab);
    }

    //Consider if transactions needed
    public static void removeTab(String key) {
        DatabaseReference ref = firebaseDatabase.getReference();

        Map removeTab = new HashMap();
        removeTab.put("menutabs/" + key, null);
        removeTab.put("product/" + key, null);

        ref.updateChildren(removeTab);
    }

    public static void updateTab(String name, int position, String active, String key) {
        DatabaseReference ref = firebaseDatabase.getReference("menutabs/" + key);
        MenuTab menuTab = new MenuTab(name, position, stringToBoolean(active));
        menuTab.setKey(key);
        ref.setValue(menuTab);
    }

    public static boolean stringToBoolean(String string){
        boolean convertedString = false;
        if(string.equals("true")){
            convertedString = true;
        }
        return convertedString;
    }

    public static void getTabs(){
        DatabaseReference ref = firebaseDatabase.getReference("menutabs/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<MenuTab> newTabs = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MenuTab menuTab = dataSnapshot.getValue(MenuTab.class);

                    Log.d("getTabs", menuTab.getName() + menuTab.getPosition() + menuTab.isActive());
                    newTabs.add(menuTab);
                }
                tabs = newTabs;
                Collections.sort(tabs);
                Log.d("getTabs", "Tabs Added");
                event.tabsChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Get tabs", "Exception " + databaseError);
            }
        });
    }

    public static void testNewUser(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        linkWithCredential(credential);
    }

    public static void testValidLogin(final String userEmail, final String userPassword) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && user.isAnonymous()) {
            saveShoppingCart();
        } else {
            Log.d("prepare", "for login");
            prepareForLogin();
        }

        testLoginAndTransfer(userEmail, userPassword);
    }

    private static void testLoginAndTransfer(String userEmail, String userPassword) {

        String email = userEmail;
        String password = userPassword;

        email = email.trim();
        password = password.trim();

        appData.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            transferShoppingCart();
                            onSuccesfullLogin();
                        } else {
                            Log.d("Fail Login Exception ", ""+task.getException());
                            event.failedLogin();
                        }

                    }
                });
    }

    public static void addProductToCart(Product product) {
        DatabaseReference ref = firebaseDatabase.getReference("shoppingcart/" + getUID());
        DatabaseReference newRef = ref.push();
        String key = newRef.getKey();
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem(product.getName(), product.getPrice(), key);
        newRef.setValue(shoppingCartItem);
    }

    public static void removeProductFromCart(final String key) {
        DatabaseReference ref = firebaseDatabase.getReference("shoppingcart/" + getUID() + "/" + key);
        ref.removeValue();
    }

    public static void logOutUser() {
        if(!loggingIn) {
            LoginManager.getInstance().logOut();
        }
        firebaseAuth.signOut();
        currentUser = null;
        Log.d("Appdata", "Logged out user");
    }

    public static void logInAnonymously() {
        appData.firebaseAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Authstate", "signInAnonymously:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("AuthState", "signInAnonymously", task.getException());
                            event.failedLogin();
                        } else {
                            currentUser = new LoggedInUser();
                            event.succesfullLogin();
                        }
                    }
                });
    }

    public static void loginFacebook(AccessToken token) {
        Log.d("appData", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        linkWithCredential(credential);
    }

    private static void linkWithCredential(final AuthCredential credential){
        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LinkWithCredential", "SUCCESS");
                            event.newUserSuccesfull();
                        } else {
                            loginWithCredential(credential);
                        }
                    }
                });
    }

    private static void loginWithCredential(AuthCredential credential){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && user.isAnonymous()) {
            saveShoppingCart();
        } else {
            prepareForLogin();
        }

        firebaseAuth.signInWithCredential(credential)
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           Log.d("appData", "signInWithCredential:onComplete:" + task.isSuccessful());
                           if (!task.isSuccessful()) {
                               Log.w("appData", "signInWithCredential", task.getException());
                               event.failedLogin();
                           } else {
                               transferShoppingCart();
                               onSuccesfullLogin();
                           }
                      }
                    });

    }

    private static void saveShoppingCart(){
            final DatabaseReference ref = firebaseDatabase.getReference("shoppingcart/" + firebaseAuth.getCurrentUser().getUid());
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
                        shoppingCart = new CartContent(cartContent);
                        ref.removeValue();
                    }
                    prepareForLogin();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("save shoppingCart error", databaseError.getMessage());
                }
            });
    }

    public static void setNewPrice(int newItemPrice){
        totalPrice += newItemPrice;
        setNewPrices();
    }

    private static void transferShoppingCart(){
        if (shoppingCart != null) {
            DatabaseReference ref = firebaseDatabase.getReference("shoppingcart/" + getUID());
            ref.setValue(shoppingCart.getCartContent());
            shoppingCart = null;
        }
    }

    private static void prepareForLogin(){
        loggingIn = true;
        logOutUser();
    }

    public static void placeOrder(final String comment, final String timeToPickup){
        final DatabaseReference ref = firebaseDatabase.getReference("shoppingcart/" + firebaseAuth.getCurrentUser().getUid());
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
                    Order order = new Order(cartContent, totalPrice, comment, timeToPickup, ServerValue.TIMESTAMP);
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
        DatabaseReference ref = firebaseDatabase.getReference("orders/" + firebaseAuth.getCurrentUser().getUid());
        ref.push().setValue(order);
        event.orderSuccessful();
    }

    public static void getUserProfile(){
        DatabaseReference ref = firebaseDatabase.getReference("users/" + firebaseAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userProfile = snapshot.getValue(UserProfile.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GetUserProfile error", databaseError.getMessage());
            }
        });
    }

    public static boolean isShopOpen(){

        Calendar c = Calendar.getInstance();
        if(c.get(Calendar.HOUR_OF_DAY) > OPENHOUR && c.get(Calendar.HOUR_OF_DAY) < CLOSEHOUR){
            return true;
        }
        else if((int)c.get(Calendar.HOUR_OF_DAY) == OPENHOUR && c.get(Calendar.MINUTE) >= OPENMINUT )
        {
            return true;
        }
        else if((int)c.get(Calendar.HOUR_OF_DAY) == CLOSEHOUR && c.get(Calendar.MINUTE) < CLOSEMINUT){
            return true;
        }
        else{
            return false;
        }
    }

    public static void updateUserProfile(String name, String phoneNumber){
        DatabaseReference ref = firebaseDatabase.getReference("users/" + firebaseAuth.getCurrentUser().getUid());
        if(userProfile != null){
            userProfile.setName(name);
            userProfile.setPhoneNumber(phoneNumber);
        } else {
            userProfile = new UserProfile(firebaseAuth.getCurrentUser().getEmail(), name, phoneNumber);
        }

        ref.setValue(userProfile);
    }

    public static void checkIfAutoLogIn(){
        if(currentUser == null){
            onSuccesfullLogin();
        }
    }

    @Deprecated
    public static void getPendingOrders(){
        DatabaseReference ref = firebaseDatabase.getReference("pendingOrders");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userProfile = snapshot.getValue(UserProfile.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GetPendingOrders error", databaseError.getMessage());
            }
        });
    }

    public static ArrayList<Order> getAllOrders(){
        DatabaseReference ref = firebaseDatabase.getReference("users");
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
        DatabaseReference ref = firebaseDatabase.getReference("users/" + firebaseAuth.getCurrentUser().getUid());
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

    public static void setOpeningHours(int openHour, int openMinutes, int closeHour, int closeMinutes){
        DatabaseReference ref = firebaseDatabase.getReference("restaurantsettings/");
        Map<String, Integer> openingHours = new HashMap<>();
        openingHours.put("openHour", openHour);
        openingHours.put("openMinutes", openMinutes);
        openingHours.put("closeHour", closeHour);
        openingHours.put("closeMinutes", closeMinutes);
        ref.setValue(openingHours);
    }

    public static void getOpeningHours(){
        DatabaseReference ref = firebaseDatabase.getReference("restaurantsettings/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Long> openingHours = (Map) snapshot.getValue();
                    OPENHOUR = (int) (long) openingHours.get("openHour");
                    OPENMINUT = (int) (long) openingHours.get("openMinutes");
                    CLOSEHOUR = (int) (long) openingHours.get("closeHour");
                    CLOSEMINUT = (int) (long) openingHours.get("closeMinutes");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GetOpeningHours() error", databaseError.getMessage());
            }
        });
    }
}