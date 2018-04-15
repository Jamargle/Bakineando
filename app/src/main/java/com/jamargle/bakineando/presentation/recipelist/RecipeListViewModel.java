package com.jamargle.bakineando.presentation.recipelist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.jamargle.bakineando.domain.model.Recipe;

import java.util.List;

import javax.inject.Inject;

public final class RecipeListViewModel extends ViewModel {

    private final LiveData<List<Recipe>> recipes = new MutableLiveData<>();

    @Inject
    public RecipeListViewModel() {
    }


    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
    
}
