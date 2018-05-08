package com.jamargle.bakineando.presentation.recipedetail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import com.jamargle.bakineando.R;
import com.jamargle.bakineando.domain.model.Ingredient;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.model.Step;
import java.util.ArrayList;
import java.util.List;

import static com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeItem.INGREDIENT;
import static com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeItem.INTRODUCTION;
import static com.jamargle.bakineando.presentation.recipedetail.adapter.RecipeItem.STEP;

public final class RecipeDetailsAdapterItemsUtil {

    private static final String INTRODUCTION_TEXT = "Recipe Introduction";

    private static SparseIntArray headerMapper;

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

        final List<Step> steps = new ArrayList<>(recipe.getSteps());
        if (!steps.isEmpty() && INTRODUCTION_TEXT.equals(steps.get(0).getShortDescription())) {
            final Step introduction = steps.remove(0);
            items.add(new IntroductionItem(introduction));
        }
        if (recipe.getIngredients() != null) {
            for (final Ingredient ingredient : recipe.getIngredients()) {
                items.add(new IngredientItem(ingredient));
            }
        }
        if (!steps.isEmpty()) {
            for (final Step step : steps) {
                items.add(new StepItem(step));
            }
        }

        return items;
    }

    public static List<String> getRecipeDetailsHeaders(@NonNull final Context context) {
        headerMapper = new SparseIntArray(3);
        final List<String> headers = new ArrayList<>(3);

        headers.add(context.getString(R.string.recipe_introduction_list_header));
        headerMapper.put(INTRODUCTION, 0);

        headers.add(context.getString(R.string.ingredients_list_header));
        headerMapper.put(INGREDIENT, 1);

        headers.add(context.getString(R.string.steps_list_header));
        headerMapper.put(STEP, 2);
        return headers;
    }

    public static int getRecipeDetailHeaderIndex(final int viewType) {
        if (headerMapper != null) {
            return headerMapper.get(viewType);
        }
        return -1;
    }

}
