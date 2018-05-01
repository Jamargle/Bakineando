package com.jamargle.bakineando.presentation.recipedetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.jamargle.bakineando.domain.model.Recipe;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class RecipeDetailViewModel extends ViewModel {

    private final MutableLiveData<Recipe> recipeLiveData = new MutableLiveData<>();

    @Inject
    public RecipeDetailViewModel() {
    }

    LiveData<Recipe> getRecipe() {
        return recipeLiveData;
    }

    void setRecipe(final Recipe recipeToShow) {
        recipeLiveData.setValue(recipeToShow);
    }

}
