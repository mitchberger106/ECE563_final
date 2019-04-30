package berger.mitchell.ece563;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SharedPref {
    private static SharedPreferences mSharedPref;
    public static final String Workout = "Workout";
    public static final String ClientID = "ClientID";
    public static final String Date = "Date";

    public static void init(Context context)
    {
        if(mSharedPref == null) {
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mSharedPref.edit();
            java.util.Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy");
            prefsEditor.putString("Date", df.format(c));
            prefsEditor.commit();
        }
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static boolean read(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public static Integer read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).commit();
    }
}