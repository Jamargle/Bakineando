package com.jamargle.bakineando.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.jamargle.bakineando.data.local.dao.IngredientDao;
import com.jamargle.bakineando.data.local.dao.RecipeDao;
import com.jamargle.bakineando.data.local.dao.StepDao;
import com.jamargle.bakineando.domain.model.Ingredient;
import com.jamargle.bakineando.domain.model.Recipe;
import com.jamargle.bakineando.domain.model.Step;

@Database(entities = {
        Ingredient.class,
        Step.class,
        Recipe.class
},
        version = 1,
        exportSchema = false)
public abstract class BakingDb extends RoomDatabase {

    public static final String DATABASE_NAME = "recipes_db";

    public abstract RecipeDao getRecipeDao();

    public abstract StepDao getStepDao();

    public abstract IngredientDao getIngredientDao();

}
