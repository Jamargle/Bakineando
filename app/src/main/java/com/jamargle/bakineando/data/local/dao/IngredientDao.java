package com.jamargle.bakineando.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.jamargle.bakineando.domain.model.Ingredient;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM " + Ingredient.TABLE_NAME + " WHERE " + Ingredient.COLUMN_RECIPE_ID + " =:recipeId")
    List<Ingredient> getIngredientsForRecipe(int recipeId);

    @Insert(onConflict = REPLACE)
    void addIngredient(Ingredient ingredient);

    @Delete
    void deleteIngredient(Ingredient ingredient);

    @Update(onConflict = REPLACE)
    void updateIngredient(Ingredient ingredient);

}
