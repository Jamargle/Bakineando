package com.jamargle.bakineando.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static com.jamargle.bakineando.domain.model.Step.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public final class Ingredient {

    public static final String TABLE_NAME = "ingredients";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private float quantity;
    private String measure;
    private String ingredient;

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
