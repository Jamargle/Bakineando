package com.jamargle.bakineando.domain.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

import static com.jamargle.bakineando.domain.model.Recipe.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public final class Recipe {

    public static final String TABLE_NAME = "Recipes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SERVINGS = "servings";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_STEPS = "fk_steps";
    public static final String COLUMN_INGREDIENTS = "fk_ingredients";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private int id;

    @ColumnInfo(name = COLUMN_NAME)
    private String name;

    @ColumnInfo(name = COLUMN_SERVINGS)
    private int servings;

    @ColumnInfo(name = COLUMN_IMAGE)
    private String image;

    @Ignore
    private List<Step> steps;
    @Ignore
    private List<Ingredient> ingredients;

    public Recipe() {
        // Needed by Room setup
    }

    @Ignore
    public Recipe(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.servings = builder.servings;
        this.image = builder.image;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(final int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    @Ignore
    public List<Step> getSteps() {
        return steps;
    }

    @Ignore
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public static class Builder {

        private int id;
        private String name;
        private int servings;
        private String image;
        private List<Step> steps = new ArrayList<>();
        private List<Ingredient> ingredients = new ArrayList<>();

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

    private static final class StepsForRecipe {

        @Embedded
        private Recipe recipeInstance;

        @Relation(
                parentColumn = COLUMN_ID,
                entityColumn = COLUMN_STEPS,
                entity = Step.class)
        private List<Step> stepList;

        StepsForRecipe(@NonNull final List<Step> steps) {
            this.stepList = steps;
        }

        public List<Step> getStepList() {
            return stepList;
        }

        public void setStepList(final List<Step> steps) {
            this.stepList = steps;
        }

    }

    private static final class IngredientsForRecipe {

        @Embedded
        private Recipe recipeInstance;

        @Relation(
                parentColumn = COLUMN_ID,
                entityColumn = COLUMN_INGREDIENTS,
                entity = Ingredient.class)
        private List<Ingredient> ingredientList;

        IngredientsForRecipe(@NonNull final List<Ingredient> ingredients) {
            this.ingredientList = ingredients;
        }

        public List<Ingredient> getStepList() {
            return ingredientList;
        }

        public void setStepList(final List<Ingredient> ingredients) {
            this.ingredientList = ingredients;
        }

    }

}
