package br.com.kmg.youdocleaning.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManagerUtil {
    public static String JANPRO_PREFERENCE = "janpro_preference";
    public static String USER_ID_KEY_PREFERENCE = "user_key_preference";


    public static void saveUserIdPreference(Context context, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_ID_KEY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID_KEY_PREFERENCE, value);
        editor.apply();
    }

    public static void removeUserIdPreference(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_ID_KEY_PREFERENCE, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(USER_ID_KEY_PREFERENCE).commit();
    }

    public static String getUserIdPreference(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(JANPRO_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID_KEY_PREFERENCE, null);
    }

}
