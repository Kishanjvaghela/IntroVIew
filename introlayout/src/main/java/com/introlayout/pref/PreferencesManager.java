package com.introlayout.pref;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by kisha_000 on 2/28/2017.
 */

public class PreferencesManager {

    private static final String PREFERENCES_NAME = "material_intro_preferences";

    private SharedPreferences sharedPreferences;

    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean isDisplayed(String id) {
        return sharedPreferences.getBoolean(id, false);
    }

    public void setDisplayed(String id) {
        sharedPreferences.edit().putBoolean(id, true).apply();
    }

    public void reset(String id) {
        sharedPreferences.edit().putBoolean(id, false).apply();
    }

    public void resetAll() {
        sharedPreferences.edit().clear().apply();
    }
}