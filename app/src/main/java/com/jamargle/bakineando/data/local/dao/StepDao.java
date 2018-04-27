package com.jamargle.bakineando.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.jamargle.bakineando.domain.model.Step;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StepDao {

    @Query("SELECT * FROM " + Step.TABLE_NAME + " WHERE " + Step.COLUMN_RECIPE_ID + " =:recipeId")
    List<Step> getStepsForRecipe(int recipeId);

    @Insert(onConflict = REPLACE)
    void addStep(Step step);

    @Delete
    void deleteStep(Step step);

    @Update(onConflict = REPLACE)
    void updateStep(Step step);

}
