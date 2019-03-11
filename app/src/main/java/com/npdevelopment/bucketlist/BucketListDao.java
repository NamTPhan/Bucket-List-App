package com.npdevelopment.bucketlist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BucketListDao {

    @Insert
    void insert(BucketListItem bucketListItem);

    @Delete
    void delete(BucketListItem bucketListItem);

    @Delete
    void delete(List<BucketListItem> products);

    @Update
    void update(BucketListItem bucketListItem);

    @Query("SELECT * from bucketlist_table")
    List<BucketListItem> getAllBucketListItems();

}
