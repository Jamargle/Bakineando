package com.jamargle.bakineando.presentation.recipelist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.jamargle.bakineando.domain.interactor.DefaultObserver;
import com.jamargle.bakineando.domain.interactor.FetchRecipesUseCase;
import com.jamargle.bakineando.domain.model.Recipe;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class RecipeListViewModel extends ViewModel {

    private final FetchRecipesUseCase useCase;

    @Inject
    public RecipeListViewModel(final FetchRecipesUseCase useCase) {
        this.useCase = useCase;
    }

    LiveData<List<Recipe>> getRecipes() {
        final MutableLiveData<List<Recipe>> liveData = new MutableLiveData<>();
        useCase.execute(null, new DefaultObserver<List<Recipe>>() {

            @Override
            public void processOnNext(final List<Recipe> recipes) {
                liveData.setValue(recipes);
            }

        });

        return liveData;
    }

}
