package com.jamargle.bakineando.domain.repository;

import com.jamargle.bakineando.domain.model.Recipe;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.List;

public interface LocalRecipeGateway {

    /**
     * Persist the recipest in a asynchronous way
     *
     * @param recipesToPersist List of Recipes to persist
     * @return The number of persisted recipes or -1 if an error ocurred
     */
    Completable persistAsynchronously(List<Recipe> recipesToPersist);

    /**
     * Persist the recipes in a synchronous way
     *
     * @param recipesToPersist List of Recipes to persist
     * @return The number of persisted recipes or -1 if an error ocurred
     */
    int persistSynchronously(List<Recipe> recipesToPersist);

    Single<List<Recipe>> obtainRecipes();

    Completable delete(Recipe recipeToDelete);

    boolean isToBeRefreshed();

}
