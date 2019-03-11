package com.npdevelopment.bucketlist;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "bucketlist_table")
public class BucketListItem implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private int done;

    public BucketListItem(String title, String description, int done) {
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "BucketListItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", done=" + done +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Initialize the object
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeInt(this.done);
    }

    // Create a new instance of the Parcelable class, instantiating it from the given Parcel
    // whose data had previously been written by Parcelable.writeToParcel()
    public static final Creator<BucketListItem> CREATOR = new Creator<BucketListItem>() {
        @Override
        public BucketListItem createFromParcel(Parcel parcel) {
            return new BucketListItem(parcel.readString(), parcel.readString(), parcel.readInt());
        }

        @Override
        public BucketListItem[] newArray(int i) {
            return new BucketListItem[i];
        }
    };

}
