package mau.restaurantapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Yoouughurt on 08-12-2016.
 */

public class LanguageHandler {
    private static final String LANGUAGE = "SAVED_LANGUAGE";
    private static final String DEFAULT_LANGUAGE = "dk";

    /**
     * Saves the given language with Shared Preferences
     *
     * @param context  The context
     * @param language The language to save
     */
    public static void saveLanguage(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (!preferences.contains(LANGUAGE)) {
            preferences.edit().putString(LANGUAGE, language).apply();
        }
    }

    /**
     * Returns language code DK if none is present @Default language code = dk.
     *
     * @param context Context.
     * @return String language-code
     */
    public static String getLanguage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = preferences.getString(LANGUAGE, DEFAULT_LANGUAGE);
        Log.d("LanguageHandler", "getLanguage = " + language);
        return language;
    }

    /**
     * Returns false if none is present @Default language code = dk.
     *
     * @param context Context.
     * @return Boolean true = english | false = danish
     */
    public static boolean isChecked(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = preferences.getString(LANGUAGE, DEFAULT_LANGUAGE);

        return language.equals("en");
    }
}
