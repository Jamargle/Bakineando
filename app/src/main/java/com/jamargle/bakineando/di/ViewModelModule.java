package com.jamargle.bakineando.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jamargle.bakineando.presentation.recipedetail.RecipeDetailViewModel;
import com.jamargle.bakineando.presentation.recipelist.RecipeListViewModel;
import com.jamargle.bakineando.presentation.stepdetail.StepDetailViewModel;
import com.jamargle.bakineando.presentation.widgetconfiguration.BakingWidgetConfigureViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeListViewModel.class)
    abstract ViewModel bindRecipeListViewModel(RecipeListViewModel recipeListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailViewModel.class)
    abstract ViewModel bindRecipeDetailViewModel(RecipeDetailViewModel recipeDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StepDetailViewModel.class)
    abstract ViewModel bindStepDetailViewModel(StepDetailViewModel stepDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BakingWidgetConfigureViewModel.class)
    abstract ViewModel bindBakingWidgetConfigureViewModel(BakingWidgetConfigureViewModel widgetConfigureViewModel);

}
