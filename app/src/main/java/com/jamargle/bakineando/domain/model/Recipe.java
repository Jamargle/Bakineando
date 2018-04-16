package com.jamargle.bakineando.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.util.ArrayList;
import java.util.List;

import static com.jamargle.bakineando.domain.model.Recipe.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public final class Recipe {

    public static final String TABLE_NAME = "recipes";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int servings;
    private String image;
    private List<Step> steps;
    private List<Ingredient> ingredients;

    public Recipe(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public static class Builder {

        private int id;
        private String name;
        private int servings;
        private String image;
        private List<Step> steps = new ArrayList<>();
        private List<Ingredient> ingredients;

        public Builder id(final int id) {
            this.id = id;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder servings(final int servings) {
            this.servings = servings;
            return this;
        }

        public Builder image(final String image) {
            this.image = image;
            return this;
        }

        public Builder steps(final List<Step> steps) {
            if (steps != null) {
                this.steps.addAll(steps);
            }
            return this;
        }

        public Builder ingredients(final List<Ingredient> ingredients) {
            if (ingredients != null) {
                this.ingredients.addAll(ingredients);
            }
            return this;
        }

        public Recipe build() {
            return new Recipe(this);
        }

    }

}
