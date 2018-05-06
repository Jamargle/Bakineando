package com.jamargle.bakineando.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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

    private static final String DATABASE_NAME = "recipes_db";

    private static BakingDb dbInstance;

    public static synchronized BakingDb getInstance(final Context context) {
        if (dbInstance == null) {
            dbInstance = Room
                    .databaseBuilder(context.getApplicationContext(), BakingDb.class, DATABASE_NAME)
                    .build();
        }
        return dbInstance;
    }

    public abstract RecipeDao getRecipeDao();

    public abstract StepDao getStepDao();

    public abstract IngredientDao getIngredientDao();

}
