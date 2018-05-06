package com.jamargle.bakineando.util;

import android.content.SharedPreferences;

import javax.inject.Inject;

public final class SharedPreferencesHandler {

    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesHandler(final SharedPreferences preferences) {
        this.sharedPreferences = preferences;
    }

}
