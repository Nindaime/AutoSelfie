package app0.com.autoselfie;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class UtilitySharedPreference {

    public static final String MyPREFERENCES = "MyPrefs" ;

    static Set<String> set = new HashSet<>();



    public static void insertIntoListOfIncompleteUserRegistration(SharedPreferences.Editor editor, int id){

        set.add(String.valueOf(id));
        editor.putStringSet("Key", set);

        editor.commit();
    }


    public static void removeFromListOfIncompleteUserRegistration(SharedPreferences.Editor editor, int id){

        set.remove(String.valueOf(id));
        editor.putStringSet("Key", set);
        editor.commit();
    }





}
