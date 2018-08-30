package com.lazytomatostudios.feedback.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.lazytomatostudios.feedback.db.entity.Feedback;

import java.util.List;

@Dao
public interface FeedbackDao {

    @Insert
    void create(Feedback feedback);

    @Query("SELECT * FROM feedback")
    List<Feedback> readAll();
}
