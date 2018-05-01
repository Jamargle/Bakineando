package com.jamargle.bakineando.presentation.recipedetail.adapter;

import android.support.annotation.NonNull;

import com.jamargle.bakineando.domain.model.Ingredient;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.model.Step;

import java.util.ArrayList;
import java.util.List;

public final class RecipeDetailsAdapterItemsUtil {

    private static final String INTRODUCTION_TEXT = "Recipe Introduction";

    private RecipeDetailsAdapterItemsUtil() {
    }

    /**
     * Applies an order to the ingredient and step lists to create the RecipeItem list that will
     * be hold by the RecipeDetailsAdapter. The order will be introduction if exists first, then
     * Ingredients and then steps.
     *
     * @param recipe Recipe with items to show
     * @return List of RecipeItem containing the given ingredients and steps to be shown as
     * recipe details
     */
    public static List<RecipeItem> normalizeItems(@NonNull final Recipe recipe) {
        final List<RecipeItem> items = new ArrayList<>(
                recipe.getIngredients().size() +
                        recipe.getSteps().size());

        final List<Step> steps = recipe.getSteps();
        if (INTRODUCTION_TEXT.equals(steps.get(0).getShortDescription())) {
            final Step introduction = steps.remove(0);
            items.add(new IntroductionItem(introduction));
        }

        for (final Ingredient ingredient : recipe.getIngredients()) {
            items.add(new IngredientItem(ingredient));
        }
        for (final Step step : steps) {
            items.add(new StepItem(step));
        }

        return items;
    }

}
