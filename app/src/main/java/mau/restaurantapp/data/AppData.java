package mau.restaurantapp.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mau.restaurantapp.adapters.CartContent;
import mau.restaurantapp.data.types.MenuTab;
import mau.restaurantapp.data.types.UserProfileType;
import mau.restaurantapp.event.EventCreator;
import mau.restaurantapp.user.LoggedInUser;
import mau.restaurantapp.utils.firebase.FirebaseRead;

/**
 * Created by anwar on 10/15/16.
 */

public class AppData extends Application {
    public static EventCreator event = new EventCreator();
    //public static ArrayList<Product> cartContent = new ArrayList<>();
    public static LoggedInUser currentUser;
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static SharedPreferences appPrefs;
    public static List<Runnable> priceObservers = new ArrayList<Runnable>();
    public static int totalPrice = 0;


    //Shop settings
    public static boolean shopOpen = true;
    public static int OPENHOUR = 11;
    public static int OPENMINUT = 00;
    public static int CLOSEHOUR = 21;
    public static int CLOSEMINUT = 00;
    public static String mainText;
    public static String address;
    public static String emailAddress;

    public static CartContent adapter = new CartContent();
    public static mau.restaurantapp.data.types.CartContent shoppingCart;
    public static boolean loggingIn = false;

    public static ArrayList<MenuTab> tabs = new ArrayList<>();

    public static UserProfileType userProfileType;
    public static mau.restaurantapp.data.types.CartContent cartContent;

    private static void setNewPrices() {
        for (int i = 0; i < priceObservers.size(); i++) {
            priceObservers.get(i).run();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setPrefs();
        FirebaseRead.getShopSettings();
        FirebaseRead.getTabs();
    }

    public static void setShopOpenOrClosed(boolean bool) {
        shopOpen = bool;
    }


    private void setPrefs() {
        if (!appPrefs.contains("mainColor")) {
            appPrefs.edit().putString("mainColor", "#f23423")
                    .putString("textColor", "#f123456").commit();
        }
    }


    public static void setNewPrice(int newTotalPrice) {
        totalPrice = newTotalPrice;
        setNewPrices();
    }


    public static boolean isShopOpen() {

        Calendar c = Calendar.getInstance();
        if (c.get(Calendar.HOUR_OF_DAY) > OPENHOUR && c.get(Calendar.HOUR_OF_DAY) < CLOSEHOUR) {
            return true;
        } else if ((int) c.get(Calendar.HOUR_OF_DAY) == OPENHOUR && c.get(Calendar.MINUTE) >= OPENMINUT) {
            return true;
        } else if ((int) c.get(Calendar.HOUR_OF_DAY) == CLOSEHOUR && c.get(Calendar.MINUTE) < CLOSEMINUT) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean stringToBoolean(String string) {
        boolean convertedString = false;
        if (string.equals("true")) {
            convertedString = true;
        }
        return convertedString;
    }
}