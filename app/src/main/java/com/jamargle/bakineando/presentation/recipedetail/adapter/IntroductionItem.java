package com.jamargle.bakineando.presentation.recipedetail.adapter;

import com.jamargle.bakineando.domain.model.Step;

public final class IntroductionItem extends RecipeItem<Step> {

    public IntroductionItem(final Step step) {
        viewType = INTRODUCTION;
        item = step;
    }

}
