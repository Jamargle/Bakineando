package com.jamargle.bakineando.util;

import android.content.Context;
import com.jamargle.bakineando.R;
import java.util.Locale;

public final class IngredientListUtil {

    private IngredientListUtil() {
    }

    public static String getIngredientLabel(
            final Context context,
            final String name,
            final float quantity,
            final String measure) {

        final String baseLabel = context.getResources().getString(R.string.recipe_details_ingredient_line);
        final String quantityLabel = getQuantityLabelWithPlural(quantity);
        return String.format(baseLabel, name, quantityLabel, measure);
    }

    public static String getIngredientQuantityLabel(
            final Context context,
            final float quantity,
            final String measure) {

        final String baseLabel = context.getResources().getString(R.string.recipe_details_ingredient_quantity_line);
        final String quantityLabel = getQuantityLabelWithPlural(quantity);
        return String.format(baseLabel, quantityLabel, measure);
    }

    private static String getQuantityLabelWithPlural(final float quantity) {
        if (quantity == (long) quantity) {
            return String.format(Locale.getDefault(), "%d", (long) quantity);
        } else {
            return String.format("%s", quantity);
        }
    }

}
