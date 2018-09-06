package com.lazytomatostudios.feedback.db;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.lazytomatostudios.feedback.db.dao.FeedbackDao;
import com.lazytomatostudios.feedback.db.dao.UserDao;
import com.lazytomatostudios.feedback.db.dao.WaiterDao;
import com.lazytomatostudios.feedback.db.entity.Feedback;
import com.lazytomatostudios.feedback.db.entity.User;
import com.lazytomatostudios.feedback.db.entity.Waiter;

@android.arch.persistence.room.Database(entities = {Waiter.class, User.class, Feedback.class}, version = 9, exportSchema = false)
public abstract class Database extends RoomDatabase {

    public abstract WaiterDao waiterDao();
    public abstract UserDao userDao();
    public abstract FeedbackDao feedbackDao();

    private static Database INSTANCE;

    public static Database getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "feedback-db").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
