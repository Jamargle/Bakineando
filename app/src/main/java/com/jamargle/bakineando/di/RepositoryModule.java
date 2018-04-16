package com.jamargle.bakineando.di;

import com.jamargle.bakineando.data.RecipesRepositoryImp;
import com.jamargle.bakineando.data.local.LocalRecipeGatewayImp;
import com.jamargle.bakineando.data.network.NetworkRecipeGatewayImp;
import com.jamargle.bakineando.domain.repository.LocalRecipeGateway;
import com.jamargle.bakineando.domain.repository.NetworkRecipeGateway;
import com.jamargle.bakineando.domain.repository.RecipesRepository;
import dagger.Binds;
import dagger.Module;
import javax.inject.Singleton;

@Module
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract LocalRecipeGateway providesLocalRecipesGateway(LocalRecipeGatewayImp localRecipeGateway);

    @Binds
    @Singleton
    public abstract NetworkRecipeGateway providesNetworkRecipeGateway(NetworkRecipeGatewayImp networkRecipeGateway);

    @Binds
    @Singleton
    public abstract RecipesRepository provideRecipesRepository(RecipesRepositoryImp repository);

}
