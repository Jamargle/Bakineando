package com.jamargle.bakineando.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static com.jamargle.bakineando.domain.model.Step.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public final class Step {

    public static final String TABLE_NAME = "steps";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;


    public Step(final Builder builder) {
        this.id = builder.id;
        this.shortDescription = builder.shortDescription;
        this.description = builder.description;
        this.videoURL = builder.videoURL;
        this.thumbnailURL = builder.thumbnailURL;
    }

    public static class Builder {

        private int id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        public Builder id(final int id) {
            this.id = id;
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
