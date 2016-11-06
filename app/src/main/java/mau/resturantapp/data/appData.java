package mau.resturantapp.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by anwar on 10/15/16.
 */

public class appData extends Application{
    private static MenuTabs tempItem = new MenuTabs();

    public static ArrayList<Product> cartContent = new ArrayList<Product>();
    public static ArrayList<MenuItem> menu1 = new ArrayList<>();
    public static ArrayList<MenuItem> menu2 = new ArrayList<>();
    public static ArrayList<MenuItem> menu3 = new ArrayList<>();
    public static ArrayList<MenuItem> menu4 = new ArrayList<>();
    public static ArrayList<MenuItem> menu5 = new ArrayList<>();
    public static String[] currentTabs = tempItem.getTabs();

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




}
