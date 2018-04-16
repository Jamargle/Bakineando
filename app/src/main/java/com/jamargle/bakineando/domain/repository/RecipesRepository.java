package com.jamargle.bakineando.domain.repository;

import android.arch.lifecycle.LiveData;
import com.jamargle.bakineando.domain.model.Recipe;
import io.reactivex.Completable;
import java.util.List;

public interface RecipesRepository {

    Completable persist(List<Recipe> recipesToPersist);

    LiveData<List<Recipe>> getRecipes();

    Completable delete(Recipe recipeToDelete);

}
