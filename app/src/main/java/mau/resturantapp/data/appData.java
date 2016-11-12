package mau.resturantapp.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import mau.resturantapp.events.OnFailedLogIn;
import mau.resturantapp.events.OnSuccesfullLogInEvent;
import mau.resturantapp.user.LoggedInUser;

/**
 * Created by anwar on 10/15/16.
 */

public class appData extends Application{
    private static MenuTabs tempItem = new MenuTabs();

    public static ArrayList<Product> cartContent = new ArrayList<Product>();
    public static String[] currentTabs = tempItem.getTabs();
    public static LoggedInUser currentUser;
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static FirebaseUser firebaseUser;
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static SharedPreferences appPrefs;

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

    private static void onFailledLogin() {
        OnFailedLogIn event = new OnFailedLogIn();
        EventBus.getDefault().post(event);
    }

    private static void onSuccesfullLogin() {
        OnSuccesfullLogInEvent event = new OnSuccesfullLogInEvent();
        EventBus.getDefault().post(event);
        String name = firebaseAuth.getCurrentUser().getDisplayName();
        String email = firebaseAuth.getCurrentUser().getDisplayName();
        currentUser = new LoggedInUser(name,email,0);

    }



}
