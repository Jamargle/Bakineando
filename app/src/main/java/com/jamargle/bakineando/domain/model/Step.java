package com.jamargle.bakineando.domain.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import static com.jamargle.bakineando.domain.model.Step.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public final class Step {

    public static final String TABLE_NAME = "Steps";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private int id;

    @ColumnInfo(name = COLUMN_SHORT_DESCRIPTION)
    private String shortDescription;

    @ColumnInfo(name = COLUMN_DESCRIPTION)
    private String description;

    @ColumnInfo(name = COLUMN_VIDEO_URL)
    private String videoURL;

    @ColumnInfo(name = COLUMN_THUMBNAIL_URL)
    private String thumbnailURL;

    @Ignore
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
