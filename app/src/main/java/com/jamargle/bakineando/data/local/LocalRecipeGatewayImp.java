package com.jamargle.bakineando.data.local;

import android.arch.lifecycle.LiveData;
import com.jamargle.bakineando.data.local.dao.RecipeDao;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.repository.LocalRecipeGateway;
import io.reactivex.Completable;
import io.reactivex.functions.Action;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

public final class LocalRecipeGatewayImp implements LocalRecipeGateway {

    private static final long REFRESH_EVERY_60_SECONDS = 60 * 1000L;

    private final RecipeDao recipeDao;

    private long lastRefresh = -1;

    @Inject
    public LocalRecipeGatewayImp(final RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    @Override
    public Completable persist(final List<Recipe> recipesToPersist) {
        if (recipesToPersist == null) {
            return Completable.error(new IllegalArgumentException("The recipe cannot be null"));
        }
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                for (final Recipe recipe : recipesToPersist) {
                    recipeDao.addRecipe(recipe);
                }
            }
        });
    }

    @Override
    public LiveData<List<Recipe>> obtainRecipes() {
        return recipeDao.getRecipes();
    }

    @Override
    public Completable delete(final Recipe recipeToDelete) {
        if (recipeToDelete == null) {
            return Completable.error(new IllegalArgumentException("The recipe cannot be null"));
        }
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                recipeDao.deleteRecipe(recipeToDelete);
            }
        });
    }

    @Override
    public boolean isToBeRefreshed() {
        final long currentTime = new Date().getTime();
        if ((currentTime - lastRefresh) > REFRESH_EVERY_60_SECONDS) {
            lastRefresh = currentTime;
            return true;
        } else {
            return false;
        }
    }

}
