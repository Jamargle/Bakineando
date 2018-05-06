package com.jamargle.bakineando.di;

import com.jamargle.bakineando.presentation.recipedetail.RecipeDetailActivity;
import com.jamargle.bakineando.presentation.recipelist.RecipeListActivity;
import com.jamargle.bakineando.presentation.stepdetail.StepDetailActivity;
import com.jamargle.bakineando.presentation.widgetconfiguration.BakingWidgetConfigureActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = FragmentBuilder.class)
    public abstract RecipeListActivity bindRecipeListActivity();

    @ContributesAndroidInjector(modules = FragmentBuilder.class)
    public abstract RecipeDetailActivity bindRecipeDetailsActivity();

    @ContributesAndroidInjector(modules = FragmentBuilder.class)
    public abstract StepDetailActivity bindStepDetailActivity();

    @ContributesAndroidInjector(modules = FragmentBuilder.class)
    public abstract BakingWidgetConfigureActivity bindBakingWidgetConfigureActivity();

}
