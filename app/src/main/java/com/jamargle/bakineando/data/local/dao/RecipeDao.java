package com.jamargle.bakineando.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.jamargle.bakineando.domain.model.Recipe;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {

    // TODO finish this!!!!
    @Query("SELECT * FROM " + Recipe.TABLE_NAME + " WHERE " + Recipe.TABLE_NAME)
    LiveData<List<Recipe>> getRecipes();

    @Insert(onConflict = REPLACE)
    void addRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe event);

    @Update(onConflict = REPLACE)
    void updateRecipe(Recipe recipe);

}
