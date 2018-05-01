package com.jamargle.bakineando.domain.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static com.jamargle.bakineando.domain.model.Step.COLUMN_RECIPE_ID;
import static com.jamargle.bakineando.domain.model.Step.COLUMN_STEP_NUMBER;
import static com.jamargle.bakineando.domain.model.Step.TABLE_NAME;

@Entity(
        tableName = TABLE_NAME,
        primaryKeys = {COLUMN_RECIPE_ID, COLUMN_STEP_NUMBER},
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = Recipe.COLUMN_ID,
                childColumns = COLUMN_RECIPE_ID,
                onDelete = CASCADE
        )
)
public final class Step implements Parcelable {

    public static final String TABLE_NAME = "Steps";
    public static final String COLUMN_RECIPE_ID = "recipe_id";
    static final String COLUMN_STEP_NUMBER = "number";

    private static final String COLUMN_SHORT_DESCRIPTION = "short_description";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_VIDEO_URL = "video_url";
    private static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";

    @ColumnInfo(index = true, name = COLUMN_RECIPE_ID)
    private int recipeId;

    @ColumnInfo(index = true, name = COLUMN_STEP_NUMBER)
    @SerializedName("id")
    private int stepNumber;

    @ColumnInfo(name = COLUMN_SHORT_DESCRIPTION)
    private String shortDescription;

    @ColumnInfo(name = COLUMN_DESCRIPTION)
    private String description;

    @ColumnInfo(name = COLUMN_VIDEO_URL)
    private String videoURL;

    @ColumnInfo(name = COLUMN_THUMBNAIL_URL)
    private String thumbnailURL;

    public Step() {
        // Needed by Room setup
    }

    @Ignore
    public Step(final Builder builder) {
        this.recipeId = builder.recipeId;
        this.stepNumber = builder.stepNumber;
        this.shortDescription = builder.shortDescription;
        this.description = builder.description;
        this.videoURL = builder.videoURL;
        this.thumbnailURL = builder.thumbnailURL;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(final int recipeId) {
        this.recipeId = recipeId;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(final int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(final String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(final String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(final String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(final Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(final int size) {
            return new Step[size];
        }
    };

    protected Step(final Parcel in) {
        recipeId = in.readInt();
        stepNumber = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(
            final Parcel dest,
            final int flags) {

        dest.writeInt(recipeId);
        dest.writeInt(stepNumber);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    public static class Builder {

        private int recipeId;
        private int stepNumber;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        public Builder recipeId(final int recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder stepNumber(final int stepNumber) {
            this.stepNumber = stepNumber;
            return this;
        }

        public Builder shortDescription(final String shortDescription) {
            this.shortDescription = shortDescription;
            return this;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        public Builder videoURL(final String videoURL) {
            this.videoURL = videoURL;
            return this;
        }

        public Builder thumbnailURL(final String thumbnailURL) {
            this.thumbnailURL = thumbnailURL;
            return this;
        }

        public Step build() {
            return new Step(this);
        }

    }

}
