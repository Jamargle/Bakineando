package com.jamargle.bakineando.di;

import android.arch.persistence.room.Room;
import com.jamargle.bakineando.BakineandoApp;
import com.jamargle.bakineando.data.local.BakingDb;
import com.jamargle.bakineando.data.local.dao.RecipeDao;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
class RoomDatabaseModule {

    @Singleton
    @Provides
    BakingDb providesRoomDatabase(BakineandoApp app) {
        return Room.databaseBuilder(app, BakingDb.class, BakingDb.DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    RecipeDao providesRecipesDao(BakingDb database) {
        return database.getRecipeDao();
    }

}
