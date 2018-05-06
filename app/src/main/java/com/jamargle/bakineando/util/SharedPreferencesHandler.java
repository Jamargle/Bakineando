package com.jamargle.bakineando.util;

import android.content.SharedPreferences;

import javax.inject.Inject;

public final class SharedPreferencesHandler {

    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesHandler(final SharedPreferences preferences) {
        this.sharedPreferences = preferences;
    }

    public void setWidgetRecipeTitle(final String title) {
        sharedPreferences.edit().putString(WIDGET_RECIPE_TITLE, title).apply();
    }

    public Set<String> getRecipeNames() {
        return sharedPreferences.getStringSet(WIDGET_RECIPE_LIST, new HashSet<String>(0));
    }

}
