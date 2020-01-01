package br.com.kmg.youdocleaning.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManagerUtil {

    public static String USER_LOGGED = "user_logged";
    public static String BEER_PREFERENCE = "beer_preference";

    public static  void savePreference(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(BEER_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void savePreference(Context context, String key, Boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(BEER_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBooleanPreference(Context context, String key, boolean defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(BEER_PREFERENCE, Context.MODE_PRIVATE);

        boolean result = sharedPreferences.getBoolean(key, defaultValue);
        return result;
    }


    public static boolean isLogged(Context context){
        return PreferencesManagerUtil.getBooleanPreference(context, PreferencesManagerUtil.USER_LOGGED, false);
    }

    public static void setLogged(Context context, boolean logged){
        PreferencesManagerUtil.savePreference(context, PreferencesManagerUtil.USER_LOGGED, logged);
    }

}
