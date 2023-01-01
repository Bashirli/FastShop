package com.bashirli.fastshop.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.bashirli.fastshop.model.DatabaseModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface RoomDAO {

 @Insert
 Completable insert(DatabaseModel model);

 @Delete
 Completable delete(DatabaseModel model);

 @Query("Delete from databasemodel")
 void deleteAll();

 @Query("SELECT * FROM DatabaseModel")
 Observable<List<DatabaseModel>> getAll();

 @Query("Update databasemodel set number=:num where itemId=:itemId")
 void setNewValue(int num,int itemId);



}
