package com.jamargle.bakineando.data.local;

import com.jamargle.bakineando.data.local.dao.IngredientDao;
import com.jamargle.bakineando.data.local.dao.RecipeDao;
import com.jamargle.bakineando.data.local.dao.StepDao;
import com.jamargle.bakineando.domain.model.Ingredient;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.model.Step;
import com.jamargle.bakineando.domain.repository.LocalRecipeGateway;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Action;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

public final class LocalRecipeGatewayImp implements LocalRecipeGateway {

    private static final long REFRESH_EVERY_60_SECONDS = 60 * 1000L;

    private final RecipeDao recipeDao;
    private final StepDao stepDao;
    private final IngredientDao ingredientDao;

    private long lastRefresh = 0;

    @Inject
    public LocalRecipeGatewayImp(
            final RecipeDao recipeDao,
            final StepDao stepDao,
            final IngredientDao ingredientDao) {

        this.recipeDao = recipeDao;
        this.stepDao = stepDao;
        this.ingredientDao = ingredientDao;
    }

    @Override
    public Completable persistAsynchronously(final List<Recipe> recipesToPersist) {
        if (recipesToPersist == null) {
            return Completable.error(new IllegalArgumentException("The recipe cannot be null"));
        }
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                persist(recipesToPersist);
            }
        });
    }

    @Override
    public int persistSynchronously(final List<Recipe> recipesToPersist) {
        if (recipesToPersist == null) {
            return -1;
        }
        persist(recipesToPersist);
        return recipesToPersist.size();
    }

    @Override
    public Single<List<Recipe>> obtainRecipes() {
        return Single.create(new SingleOnSubscribe<List<Recipe>>() {

            @Override
            public void subscribe(final SingleEmitter<List<Recipe>> emitter) {
                final List<Recipe> recipes = recipeDao.getRecipes();
                for (Recipe recipe : recipes) {
                    final List<Step> stepsForRecipe = stepDao.getStepsForRecipe(recipe.getId());
                    recipe.setSteps(stepsForRecipe);

                    final List<Ingredient> ingredientsForRecipe = ingredientDao.getIngredientsForRecipe(recipe.getId());
                    recipe.setIngredients(ingredientsForRecipe);
                }
                emitter.onSuccess(recipes);
            }

        });
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
        }
        return false;
    }

    private void persist(List<Recipe> recipes) {
        for (final Recipe recipe : recipes) {
            recipeDao.addRecipe(recipe);
            for (final Step step : recipe.getSteps()) {
                step.setRecipeId(recipe.getId());
                stepDao.addStep(step);
            }
            for (final Ingredient ingredient : recipe.getIngredients()) {
                ingredient.setRecipeId(recipe.getId());
                ingredientDao.addIngredient(ingredient);
            }
        }
    }

}
