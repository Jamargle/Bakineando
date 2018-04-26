package com.jamargle.bakineando.data.network;

import android.accounts.NetworkErrorException;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.repository.NetworkRecipeGateway;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import java.io.IOException;
import java.util.List;

public final class NetworkRecipeGatewayImp implements NetworkRecipeGateway {

    private final RecipeApiClient apiService;

    public NetworkRecipeGatewayImp(final RecipeApiClient apiService) {
        this.apiService = apiService;
    }

    @Override
    public Single<List<Recipe>> obtainRecipes() {
        return Single.create(new SingleOnSubscribe<List<Recipe>>() {
            @Override
            public void subscribe(final SingleEmitter<List<Recipe>> emitter) {
                try {
                    emitter.onSuccess(apiService.getListOfRecipes().execute().body());
                } catch (IOException e) {
                    emitter.onError(new NetworkErrorException());
                }
            }
        });
    }

}
