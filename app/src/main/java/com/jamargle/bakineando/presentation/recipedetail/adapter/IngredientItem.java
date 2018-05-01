package com.jamargle.bakineando.presentation.recipedetail.adapter;

import com.jamargle.bakineando.domain.model.Ingredient;

public final class IngredientItem extends RecipeItem<Ingredient> {

    public IngredientItem(final Ingredient ingredient) {
        viewType = INGREDIENT;
        item = ingredient;
    }

}
