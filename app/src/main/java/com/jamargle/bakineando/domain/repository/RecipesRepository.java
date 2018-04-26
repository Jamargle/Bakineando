package com.jamargle.bakineando.domain.repository;

import com.jamargle.bakineando.domain.model.Recipe;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.List;

public interface RecipesRepository {

    Completable persist(List<Recipe> recipesToPersist);

    Single<List<Recipe>> getRecipes();

    Completable delete(Recipe recipeToDelete);

}
