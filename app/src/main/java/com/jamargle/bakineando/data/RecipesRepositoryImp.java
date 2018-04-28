package com.jamargle.bakineando.data;

import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.repository.LocalRecipeGateway;
import com.jamargle.bakineando.domain.repository.NetworkRecipeGateway;
import com.jamargle.bakineando.domain.repository.RecipesRepository;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import java.util.List;
import javax.inject.Inject;

public final class RecipesRepositoryImp implements RecipesRepository {

    private final LocalRecipeGateway localRecipeGateway;
    private final NetworkRecipeGateway networkRecipeGateway;

    @Inject
    public RecipesRepositoryImp(
            final LocalRecipeGateway localRecipeGateway,
            final NetworkRecipeGateway networkRecipeGateway) {

        this.localRecipeGateway = localRecipeGateway;
        this.networkRecipeGateway = networkRecipeGateway;
    }

    @Override
    public Completable persist(final List<Recipe> recipesToPersist) {
        return localRecipeGateway.persistAsynchronously(recipesToPersist);
    }

    @Override
    public Single<List<Recipe>> getRecipes() {
        if (localRecipeGateway.isToBeRefreshed()) {
            return networkRecipeGateway.obtainRecipes()
                    .doAfterSuccess(new Consumer<List<Recipe>>() {
                        @Override
                        public void accept(final List<Recipe> recipes) {
                            localRecipeGateway.persistSynchronously(recipes);
                        }
                    });
        } else {
            return localRecipeGateway.obtainRecipes();
        }
    }

    @Override
    public Completable delete(final Recipe recipeToDelete) {
        return localRecipeGateway.delete(recipeToDelete);
    }

}
