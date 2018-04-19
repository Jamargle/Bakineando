package com.jamargle.bakineando.domain.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import static com.jamargle.bakineando.domain.model.Step.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public final class Ingredient {

    public static final String TABLE_NAME = "ingredients";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_MEASURE = "measure";
    public static final String COLUMN_INGREDIENT = "ingredient";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private int id;

    @ColumnInfo(name = COLUMN_QUANTITY)
    private float quantity;

    @ColumnInfo(name = COLUMN_MEASURE)
    private String measure;

    @ColumnInfo(name = COLUMN_INGREDIENT)
    private String ingredient;

    @Ignore
    public Ingredient(final Builder builder) {
        this.id = builder.id;
        this.quantity = builder.quantity;
        this.measure = builder.measure;
        this.ingredient = builder.ingredient;
    }

    public int getId() {
        return id;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public static class Builder {

        private int id;
        private float quantity;
        private String measure;
        private String ingredient;

        public Builder id(final int id) {
            this.id = id;
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
