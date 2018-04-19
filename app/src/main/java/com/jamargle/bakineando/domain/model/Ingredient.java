package com.jamargle.bakineando.domain.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static com.jamargle.bakineando.domain.model.Ingredient.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public final class Ingredient {

    public static final String TABLE_NAME = "Ingredients";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_MEASURE = "measure";
    public static final String COLUMN_INGREDIENT = "ingredient";

    @ColumnInfo(name = COLUMN_QUANTITY)
    private float quantity;

    @ColumnInfo(name = COLUMN_MEASURE)
    private String measure;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = COLUMN_INGREDIENT)
    private String ingredient;

    public Ingredient() {
        this.ingredient = "";
        // Needed by Room setup
    }

    @Ignore
    public Ingredient(final Builder builder) {
        this.quantity = builder.quantity;
        this.measure = builder.measure;
        this.ingredient = builder.ingredient;
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

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(final String ingredient) {
        this.ingredient = ingredient;
    }

    public static class Builder {

        private float quantity;
        private String measure;
        private String ingredient;

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
