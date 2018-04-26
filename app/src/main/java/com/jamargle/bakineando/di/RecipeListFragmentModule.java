package com.jamargle.bakineando.di;

import com.jamargle.bakineando.domain.interactor.FetchRecipesUseCase;
import com.jamargle.bakineando.domain.interactor.UseCase;
import com.jamargle.bakineando.domain.model.Recipe;
import dagger.Binds;
import dagger.Module;
import java.util.List;
import javax.inject.Singleton;

@Module
public abstract class RecipeListFragmentModule {

    @Binds
    @Singleton
    abstract UseCase<Void, List<Recipe>> providesFetchRecipesUseCase(FetchRecipesUseCase useCase);

}
