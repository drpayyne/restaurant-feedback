package com.lazytomatostudios.feedback.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.lazytomatostudios.feedback.db.entity.Waiter;

import java.util.List;

@Dao
public interface WaiterDao {

    @Insert
    void create (Waiter waiter);

    @Query("SELECT * FROM waiter")
    List<Waiter> readAll ();

    @Query("SELECT * FROM waiter WHERE id = :id")
    Waiter read(int id);

    @Update
    void update (Waiter waiter);

    @Delete
    void delete (Waiter waiter);

}
