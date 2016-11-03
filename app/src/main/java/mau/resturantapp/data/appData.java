package mau.resturantapp.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by anwar on 10/15/16.
 */

public class appData {
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


}
