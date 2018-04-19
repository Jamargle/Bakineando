package com.jamargle.bakineando.di;

import com.jamargle.bakineando.data.RecipesRepositoryImp;
import com.jamargle.bakineando.data.local.LocalRecipeGatewayImp;
import com.jamargle.bakineando.data.network.NetworkRecipeGatewayImp;
import com.jamargle.bakineando.data.network.RecipeApiClient;
import com.jamargle.bakineando.domain.repository.LocalRecipeGateway;
import com.jamargle.bakineando.domain.repository.NetworkRecipeGateway;
import com.jamargle.bakineando.domain.repository.RecipesRepository;
import com.jamargle.bakineando.util.ServiceGenerator;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class RepositoryModule {

    @Provides
    @Singleton
    public LocalRecipeGateway providesLocalRecipesGateway(LocalRecipeGatewayImp localRecipeGateway) {
        return localRecipeGateway;
    }

    @Provides
    @Singleton
    public NetworkRecipeGateway providesNetworkRecipeGateway() {
        return new NetworkRecipeGatewayImp(ServiceGenerator.createService(RecipeApiClient.class));
    }

    @Provides
    @Singleton
    public RecipesRepository provideRecipesRepository(RecipesRepositoryImp repository) {
        return repository;
    }

}
