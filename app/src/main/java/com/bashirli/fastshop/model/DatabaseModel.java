package com.bashirli.fastshop.model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DatabaseModel {
    @PrimaryKey(autoGenerate = true)
    public int id;


    @Nullable
    @ColumnInfo(name = "itemId")
    public int itemId;

    @Nullable
    @ColumnInfo(name = "number")
    public int number;


    public DatabaseModel(@Nullable int itemId, int number) {

        this.itemId=itemId;
        this.number = number;
    }
}
