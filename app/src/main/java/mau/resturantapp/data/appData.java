package mau.resturantapp.data;

import java.util.ArrayList;

/**
 * Created by anwar on 10/15/16.
 */

public class appData {
    private static MenuTabs tempItem = new MenuTabs();

    public static ArrayList<MenuItem> cartContent = new ArrayList<MenuItem>();
    public static ArrayList<MenuItem> menu1 = new ArrayList<>();
    public static ArrayList<MenuItem> menu2 = new ArrayList<>();
    public static ArrayList<MenuItem> menu3 = new ArrayList<>();
    public static ArrayList<MenuItem> menu4 = new ArrayList<>();
    public static ArrayList<MenuItem> menu5 = new ArrayList<>();
    public static String[] currentTabs = tempItem.getTabs();


}
