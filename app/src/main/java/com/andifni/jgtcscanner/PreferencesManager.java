package com.andifni.jgtcscanner;


import android.content.Context;
import android.content.SharedPreferences;


/**
 * Shared Preferences Manager, to simplify method call and initiate object. Save memory too.
 * Created by Andi Fajar on 14/06/2016.
 */
public class PreferencesManager {

    private static final String PREF_NAME = "NAME";
    private static final String KEY_PREFERENCES_VERSION = "PREFERENCES_VERSION";
    private static final String KEY_END_POINT = "END_POINT";

    private static PreferencesManager instance;
    private final SharedPreferences pref;

    private PreferencesManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        checkPreferences(pref);
    }

    public static synchronized void initializeInstance(Context context) {
        if (instance == null) {

        }
    }

    public static synchronized PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    /**
     *
     * @param thePreferences current preseference
     */
    private static void checkPreferences(SharedPreferences thePreferences) {
        final double oldVersion = thePreferences.getInt(KEY_PREFERENCES_VERSION, 1);
        int currentVersion = BuildConfig.VERSION_CODE;
        if (oldVersion < currentVersion) {
            final SharedPreferences.Editor edit = thePreferences.edit();
            edit.clear();
            edit.putInt(KEY_PREFERENCES_VERSION, currentVersion);
            edit.apply();
        }
    }

    public void setEndPoint(String value) {
        pref.edit()
                .putString(KEY_END_POINT, value)
                .apply();
    }

    public String getEndPoint() {
        return pref.getString(KEY_END_POINT, "");
    }

    public void remove(String key) {
        pref.edit()
                .remove(key)
                .apply();
    }

    public boolean clear() {
        return pref.edit()
                .clear()
                .commit();
    }

}
