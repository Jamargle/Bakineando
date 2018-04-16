package com.jamargle.bakineando.data.network;

import android.arch.lifecycle.LiveData;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.repository.NetworkRecipeGateway;
import java.util.List;
import javax.inject.Inject;

public final class NetworkRecipeGatewayImp implements NetworkRecipeGateway {

    private static final long REFRESH_EVERY_60_SECONDS = 60 * 1000L;

    private long lastRefresh = -1;

    @Inject
    public NetworkRecipeGatewayImp() {

    }

    @Override
    public LiveData<List<Recipe>> obtainRecipes() {
        return null;
    }

}
