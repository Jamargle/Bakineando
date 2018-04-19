package com.jamargle.bakineando.data.network;

import android.arch.lifecycle.LiveData;

import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.repository.NetworkRecipeGateway;

import java.util.List;

public final class NetworkRecipeGatewayImp implements NetworkRecipeGateway {

    private final RecipeApiClient apiService;

    public NetworkRecipeGatewayImp(final RecipeApiClient apiService) {
        this.apiService = apiService;
    }

    @Override
    public LiveData<List<Recipe>> obtainRecipes() {
        return new RetrofitLiveData<>(apiService.getListOfRecipes());
    }

}
