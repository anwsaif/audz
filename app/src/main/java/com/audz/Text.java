package com.audz;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
// import androidx.room.Index;

@Entity(
    tableName = "texts"
)
public class Text {
    // @PrimaryKey(autoGenerate = true)
    // public int id;

    @PrimaryKey
    @NonNull
    public String text;

    // Constructor
    public Text(String text) {
        this.text = text;
    }
}
