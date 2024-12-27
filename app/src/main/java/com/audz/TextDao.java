package com.audz;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TextDao {

    @Insert
    void insert(Text text);

    @Query("SELECT * FROM texts")
    List<Text> getTexts();

    @Query("DELETE FROM texts")
    void deleteAll();
    
}
