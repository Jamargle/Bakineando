package com.jamargle.bakineando.domain.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static com.jamargle.bakineando.domain.model.Ingredient.COLUMN_INGREDIENT;
import static com.jamargle.bakineando.domain.model.Ingredient.COLUMN_RECIPE_ID;
import static com.jamargle.bakineando.domain.model.Ingredient.TABLE_NAME;

@Entity(
        tableName = TABLE_NAME,
        primaryKeys = {COLUMN_RECIPE_ID, COLUMN_INGREDIENT},
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = Recipe.COLUMN_ID,
                childColumns = Ingredient.COLUMN_RECIPE_ID,
                onDelete = CASCADE
        )
)
public final class Ingredient {

    public static final String TABLE_NAME = "Ingredients";
    public static final String COLUMN_RECIPE_ID = "recipe_id";
    static final String COLUMN_INGREDIENT = "ingredient";

    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_MEASURE = "measure";

    @ColumnInfo(index = true, name = Step.COLUMN_RECIPE_ID)
    private int recipeId;

    @ColumnInfo(name = COLUMN_QUANTITY)
    private float quantity;

    @ColumnInfo(name = COLUMN_MEASURE)
    private String measure;

    @NonNull
    @ColumnInfo(name = COLUMN_INGREDIENT)
    @SerializedName("ingredient")
    private String name;

    public Ingredient() {
        this.name = "";
        // Needed by Room setup
    }

    @Ignore
    public Ingredient(final Builder builder) {
        this.recipeId = builder.recipeId;
        this.quantity = builder.quantity;
        this.measure = builder.measure;
        this.name = builder.ingredient;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(final int recipeId) {
        this.recipeId = recipeId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(final float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(final String measure) {
        this.measure = measure;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }

    public static class Builder {

        private int recipeId;
        private float quantity;
        private String measure;
        private String ingredient;

        public Builder recipeId(final int recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder quantity(final float quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder measure(final String measure) {
            this.measure = measure;
            return this;
        }

        public Builder ingredient(final String ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public Ingredient build() {
            return new Ingredient(this);
        }

    }

}
