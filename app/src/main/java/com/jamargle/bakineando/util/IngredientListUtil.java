package com.jamargle.bakineando.util;

import android.content.Context;

import com.jamargle.bakineando.R;

public final class IngredientListUtil {

    private IngredientListUtil() {
    }

    public static String getIngredientLabel(
            final Context context,
            final String name,
            final float quantity,
            final String measure) {

        final String baseLabel = context.getResources().getString(R.string.recipe_details_ingredient_line);
        final String quantityLabel = String.format("%s", quantity);

        return String.format(baseLabel, name, quantityLabel, measure);
    }

}
