package com.bashirli.fastshop.roomdb;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.bashirli.fastshop.model.DatabaseModel;

@Database(entities = {DatabaseModel.class},version = 1)
public abstract class RoomDB extends RoomDatabase {
    public abstract RoomDAO getDao();
}
