package mau.resturantapp.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mau.resturantapp.events.NewUserFailedEvent;
import mau.resturantapp.events.NewUserSuccesfullEvent;
import mau.resturantapp.events.OnFailedLogIn;
import mau.resturantapp.events.OnSuccesfullLogInEvent;
import mau.resturantapp.user.LoggedInUser;

/**
 * Created by anwar on 10/15/16.
 */

public class appData extends Application{
    private static MenuTabs tempItem = new MenuTabs();

    public static ArrayList<Product> cartContent = new ArrayList<>();
    public static String[] currentTabs = tempItem.getTabs();
    public static LoggedInUser currentUser;
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static FirebaseUser firebaseUser;
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseAuth anonymousAuth;
    public static SharedPreferences appPrefs;

    //New Cart in progress
    public static CartContent shoppingCart;

    @Override
    public void onCreate() {
        super.onCreate();

        appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setPrefs();
    }


    private void setPrefs(){
        if(!appPrefs.contains("mainColor")){
            appPrefs.edit().putString("mainColor","#f23423")
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

    public static boolean isLoggedIn(){
        if(firebaseAuth.getCurrentUser() != null){
            return true;
        }

        return false;
    }

    @Nullable
    public static String getUID(){
        if(isLoggedIn()) {
            return firebaseAuth.getCurrentUser().getUid();
        }

        return null;
    }



    public static void validLogin(String userEmail, String userPassword) {
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
                                }
                            else{
                                onFailledLogin();

                                }

                        }
                                   });



    }

    public static void newUser(String email, String password){
        appData.firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    newUserSuccesfull();
                }
                else{
                    NewUserFailedEvent event = new NewUserFailedEvent();
                    EventBus.getDefault().post(event);
                }
            }
        });

    }

    private static void newUserSuccesfull() {
        NewUserSuccesfullEvent event = new NewUserSuccesfullEvent();
        EventBus.getDefault().post(event);
    }


    private static void onFailledLogin() {
        OnFailedLogIn event = new OnFailedLogIn();
        EventBus.getDefault().post(event);
    }

    private static void onSuccesfullLogin() {
        OnSuccesfullLogInEvent event = new OnSuccesfullLogInEvent();
        EventBus.getDefault().post(event);
        String name = firebaseAuth.getCurrentUser().getDisplayName();
        String email = firebaseAuth.getCurrentUser().getEmail();
        currentUser = new LoggedInUser(name,email,0);
        isAdmin();
    }

    private static void isAdmin() {
        DatabaseReference ref = firebaseDatabase.getReference("permissions/" + getUID());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentUser.setAdmin(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Admin check error", databaseError.getMessage());
            }
        });
    }

    public static void addTab(MenuTab menuTab){
        DatabaseReference ref = firebaseDatabase.getReference("menutabs/");
        ref.push().setValue(menuTab);
    }

    //Consider if transactions needed
    public static void removeTab(MenuTab menuTab){
        DatabaseReference ref = firebaseDatabase.getReference();

        Map removeTab = new HashMap();
        removeTab.put("menutabs/" + menuTab.getKey(), null);
        removeTab.put("product/" + menuTab.getKey(), null);

        ref.updateChildren(removeTab);
    }

    public static void updateTab(MenuTab menuTab){
        DatabaseReference ref = firebaseDatabase.getReference("menutabs/");
        ref.child(menuTab.getKey()).setValue(menuTab);
    }

    public static void transferAnonymousData(){
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

    public static void testNewUser(String email, String password){
        AuthCredential credential = EmailAuthProvider.getCredential(email,password);

        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            newUserSuccesfull();
                        } else {
                            NewUserFailedEvent event = new NewUserFailedEvent();
                            EventBus.getDefault().post(event);
                        }
                    }
                });

    }

    public static void testValidLogin(final String userEmail, final String userPassword) {

        if(firebaseAuth.getCurrentUser().isAnonymous()){;
            final DatabaseReference ref = firebaseDatabase.getReference("shoppingcart/" + firebaseAuth.getCurrentUser().getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        shoppingCart = snapshot.getValue(CartContent.class);
                        ref.removeValue();
                    }

                    testLoginAndTransfer(userEmail, userPassword);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Transfer Anonymous Data", databaseError.getMessage());
                }
            });
        }
    }

    private static void testLoginAndTransfer(String userEmail, String userPassword) {
        boolean wasAnonymous = false;
        if(firebaseAuth.getCurrentUser().isAnonymous()){
            wasAnonymous = true;
        }

        String email = userEmail;
        String password = userPassword;

        email = email.trim();
        password = password.trim();

        final boolean finalWasAnonymous = wasAnonymous;
        appData.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            if(finalWasAnonymous && shoppingCart != null){
                                DatabaseReference ref = firebaseDatabase.getReference("shoppingCart/" + getUID());
                                ref.setValue(shoppingCart);
                                shoppingCart = null;
                            }
                            onSuccesfullLogin();
                        }
                        else{
                            onFailledLogin();

                        }

                    }
                });
    }

}
