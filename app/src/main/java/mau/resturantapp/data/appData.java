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
import mau.resturantapp.utils.Firebase.FirebaseRead;
import mau.resturantapp.utils.LanguageContextWrapper;

/**
 * Created by anwar on 10/15/16.
 */

public class appData extends Application {
    public static EventCreator event = new EventCreator();
    public static ArrayList<Product> cartContent = new ArrayList<>();
    public static LoggedInUser currentUser;
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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

    public static CartContent shoppingCart;
    public static boolean loggingIn = false;

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
        FirebaseRead.getOpeningHours();
        FirebaseRead.getTabs();
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

    public static void setNewPrice(int newItemPrice){
        totalPrice += newItemPrice;
        setNewPrices();
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

    public static boolean stringToBoolean(String string){
        boolean convertedString = false;
        if(string.equals("true")){
            convertedString = true;
        }
        return convertedString;
    }
}