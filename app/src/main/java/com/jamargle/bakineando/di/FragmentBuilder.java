package com.jamargle.bakineando.di;

import com.jamargle.bakineando.presentation.recipedetail.RecipeDetailFragment;
import com.jamargle.bakineando.presentation.recipelist.RecipeListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuilder {

    @ContributesAndroidInjector(modules = {RecipeListFragmentModule.class})
    public abstract RecipeListFragment bindRecipeListFragment();

    @ContributesAndroidInjector(modules = {RecipeDetailFragmentModule.class})
    public abstract RecipeDetailFragment bindRecipeDetailFragment();

}
