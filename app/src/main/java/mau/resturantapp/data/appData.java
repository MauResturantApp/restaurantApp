package mau.resturantapp.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mau.resturantapp.event.EventCreator;
import mau.resturantapp.event.events.NewUserFailedEvent;
import mau.resturantapp.event.events.NewUserSuccesfullEvent;
import mau.resturantapp.event.events.OnFailedLogIn;
import mau.resturantapp.event.events.OnSuccesfullLogInEvent;
import mau.resturantapp.user.LoggedInUser;

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

    public static int OPENHOUR = 11; //dette laves om senere til at admin kan skifte, men for now tester jeg bare.
    public static int OPENMINUT = 00;
    public static int CLOSEHOUR = 21;
    public static int CLOSEMINUT = 00;

    //New Cart in progress
    public static CartContent shoppingCart;
    public static boolean loggingIn = false;

    @Override
    public void onCreate() {
        super.onCreate();

        appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setPrefs();
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


    public static void validLogin(String userEmail, String userPassword) {
        event.logUserIn();
        String email = userEmail;
        String password = userPassword;
        email = email.trim();
        password = password.trim();

        appData.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            onSuccesfullLogin();
                        } else {
                            event.failedLogin();
                        }

                    }
                });


    }

    public static void newUser(final String email, final String password) {
        event.logUserIn();
        appData.firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser = new LoggedInUser(email, password, 0);
                    event.newUserSuccesfull();

                } else {
                    event.newUserFailed();
                }
            }
        });

    }

    private static void onSuccesfullLogin() {
        String name = firebaseAuth.getCurrentUser().getDisplayName();
        String email = firebaseAuth.getCurrentUser().getEmail();
        currentUser = new LoggedInUser(name, email, 0);
        isAdmin();
    }

    private static void isAdmin() {
        DatabaseReference ref = firebaseDatabase.getReference("permissions/" + getUID());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentUser.setAdmin(true);
                    Log.d("Logged in as admin : ", "" + currentUser.isAdmin());
                }
                event.succesfullLogin();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Admin check error", databaseError.getMessage());
            }
        });
    }

    public static void addTab(MenuTab menuTab) {
        DatabaseReference ref = firebaseDatabase.getReference("menutabs/");
        ref.push().setValue(menuTab);
    }

    //Consider if transactions needed
    public static void removeTab(MenuTab menuTab) {
        DatabaseReference ref = firebaseDatabase.getReference();

        Map removeTab = new HashMap();
        removeTab.put("menutabs/" + menuTab.getKey(), null);
        removeTab.put("product/" + menuTab.getKey(), null);

        ref.updateChildren(removeTab);
    }

    public static void updateTab(MenuTab menuTab) {
        DatabaseReference ref = firebaseDatabase.getReference("menutabs/");
        ref.child(menuTab.getKey()).setValue(menuTab);
    }

    public static void transferAnonymousData() {
        final DatabaseReference ref = firebaseDatabase.getReference();
        final DatabaseReference refAnonymous = firebaseDatabase.getReference("shoppingcart/" + anonymousAuth.getCurrentUser().getUid());
        final DatabaseReference refKnownUser = firebaseDatabase.getReference("shoppingcart/" + firebaseAuth.getCurrentUser().getUid());
        refAnonymous.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    CartContent cartContent = snapshot.getValue(CartContent.class);
                    Map childUpdates = new HashMap();
                    childUpdates.put(refKnownUser, cartContent);
                    childUpdates.put(refAnonymous, null);
                    ref.updateChildren(childUpdates);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Transfer Anonymous Data", databaseError.getMessage());
            }
        });
    }

    public static void testNewUser(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            event.newUserSuccesfull();
                        } else {
                            event.newUserFailed();
                        }
                    }
                });

    }

    public static void testValidLogin(final String userEmail, final String userPassword) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && user.isAnonymous()) {
            final DatabaseReference ref = firebaseDatabase.getReference("shoppingcart/" + firebaseAuth.getCurrentUser().getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Map<String, Product> cartContent = new HashMap<>();
                        for(DataSnapshot productSnapshot: snapshot.getChildren()){
                            String key = productSnapshot.getKey();
                            Product product = productSnapshot.getValue(Product.class);
                            Log.d("CartContent: ", "key " + key + " name " + product.getName() + " price " + product.getPrice());
                            cartContent.put(key,product);
                        }
                        shoppingCart = new CartContent(cartContent);
                        ref.removeValue();
                        loggingIn = true;
                        logOutUser();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Transfer Anonymous Data", databaseError.getMessage());
                }
            });
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
                            if (shoppingCart != null) {
                                DatabaseReference ref = firebaseDatabase.getReference("shoppingcart/" + getUID());
                                ref.setValue(shoppingCart.getCartContent());
                                shoppingCart = null;
                            }
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

        ref.push().setValue(product);
    }

    public static void removeProductFromCart(int position) {
        DatabaseReference ref = firebaseDatabase.getReference("shoppingcart/" + getUID());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                    int length = (int) snapshot.getChildrenCount();


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Remove from Cart", databaseError.getMessage());
            }
        });
    }

    public static void logOutUser() {
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

                            // Toast.makeText(getContext(), "Authentication failed.",
                            //       Toast.LENGTH_SHORT).show();
                        } else {
                            currentUser = new LoggedInUser();
                            event.succesfullLogin();
                        }
                    }
                });
    }

}