package com.jamargle.bakineando.di;

import com.jamargle.bakineando.BakineandoApp;
import com.jamargle.bakineando.data.local.BakingDb;
import com.jamargle.bakineando.data.local.dao.IngredientDao;
import com.jamargle.bakineando.data.local.dao.RecipeDao;
import com.jamargle.bakineando.data.local.dao.StepDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class RoomDatabaseModule {

    @Singleton
    @Provides
    BakingDb providesRoomDatabase(final BakineandoApp app) {
        return BakingDb.getInstance(app);
    }

    @Singleton
    @Provides
    RecipeDao providesRecipesDao(BakingDb database) {
        return database.getRecipeDao();
    }

    @Singleton
    @Provides
    StepDao providesStepDao(BakingDb database) {
        return database.getStepDao();
    }

    @Singleton
    @Provides
    IngredientDao providesIngredientDao(BakingDb database) {
        return database.getIngredientDao();
    }

}
