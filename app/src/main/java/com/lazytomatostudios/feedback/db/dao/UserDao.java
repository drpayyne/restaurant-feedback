package com.lazytomatostudios.feedback.db.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.lazytomatostudios.feedback.db.entity.Feedback;
import com.lazytomatostudios.feedback.db.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void create(User user);

    @Query("SELECT * FROM user WHERE phone_number = :phone_number")
    User read(String phone_number);

    @Query("SELECT * FROM user")
    List<User> readAll();

}
