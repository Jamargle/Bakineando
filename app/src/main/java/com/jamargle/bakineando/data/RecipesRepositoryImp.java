package com.jamargle.bakineando.data;

import android.arch.lifecycle.LiveData;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.repository.LocalRecipeGateway;
import com.jamargle.bakineando.domain.repository.NetworkRecipeGateway;
import com.jamargle.bakineando.domain.repository.RecipesRepository;
import io.reactivex.Completable;
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
        return localRecipeGateway.persist(recipesToPersist);
    }

    @Override
    public LiveData<List<Recipe>> getRecipes() {
        refreshRecipesIfNeeded();
        return localRecipeGateway.obtainRecipes();
    }

    @Override
    public Completable delete(final Recipe recipeToDelete) {
        return localRecipeGateway.delete(recipeToDelete);
    }

    private void refreshRecipesIfNeeded() {
        if (localRecipeGateway.isToBeRefreshed()) {
            localRecipeGateway.persist(networkRecipeGateway.obtainRecipes().getValue());
        }
    }

}
