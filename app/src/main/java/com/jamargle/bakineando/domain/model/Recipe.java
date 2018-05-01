package com.jamargle.bakineando.domain.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import static com.jamargle.bakineando.domain.model.Recipe.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public final class Recipe implements Parcelable {

    public static final String TABLE_NAME = "Recipes";
    public static final String COLUMN_ID = "id";

    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SERVINGS = "servings";
    private static final String COLUMN_IMAGE = "image";

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
    private List<Step> steps = new ArrayList<>();
    @Ignore
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {
        // Needed by Room setup
    }

    @Ignore
    public Recipe(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.servings = builder.servings;
        this.image = builder.image;
        this.steps = builder.steps;
        this.ingredients = builder.ingredients;
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

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(final List<Step> steps) {
        this.steps = steps;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(final List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(final Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(final int size) {
            return new Recipe[size];
        }
    };

    protected Recipe(final Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
        in.readTypedList(this.steps, Step.CREATOR);
        in.readTypedList(this.ingredients, Ingredient.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(
            final Parcel dest,
            final int flags) {

        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeTypedList(steps);
        dest.writeTypedList(ingredients);
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

}
