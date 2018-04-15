package com.jamargle.bakineando.di;

import com.jamargle.bakineando.presentation.recipedetail.RecipeDetailActivity;
import com.jamargle.bakineando.presentation.recipelist.RecipeListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = FragmentBuilder.class)
    public abstract RecipeListActivity bindRecipeListActivity();

    @ContributesAndroidInjector(modules = FragmentBuilder.class)
    public abstract RecipeDetailActivity bindRecipeDetailsActivity();

}
