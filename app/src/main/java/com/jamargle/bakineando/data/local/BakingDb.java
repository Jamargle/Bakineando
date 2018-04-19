package com.jamargle.bakineando.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.jamargle.bakineando.data.local.dao.RecipeDao;
import com.jamargle.bakineando.domain.model.Recipe;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class BakingDb extends RoomDatabase {

    public static final String DATABASE_NAME = "recipes_db";

    public abstract RecipeDao getRecipeDao();

}
