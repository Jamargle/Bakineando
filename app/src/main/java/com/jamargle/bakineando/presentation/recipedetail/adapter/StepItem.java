package com.jamargle.bakineando.presentation.recipedetail.adapter;

import com.jamargle.bakineando.domain.model.Step;

public final class StepItem extends RecipeItem<Step> {

    public StepItem(final Step step) {
        viewType = STEP;
        item = step;
    }

}
