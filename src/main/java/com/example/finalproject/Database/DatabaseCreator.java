package com.example.finalproject.Database;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//import com.example.finalproject.Data.QuestionListDao;
import com.example.finalproject.Data.User;
import com.example.finalproject.Data.UsersDao;

@Database(entities = {User.class}, version = 6,exportSchema = false)
/**
 * Database Creator for User Object
 * @author Junqi.Sun
 */
public abstract class DatabaseCreator extends RoomDatabase {
    /**
     * Instance of Database
     */
    private static DatabaseCreator instance;

    /**
     * Get Instance of Database
     * @param context
     * @return
     */
    public static synchronized DatabaseCreator getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    /**
     * create Database
     * @param context
     * @return
     */
    public static DatabaseCreator create(Context context){
        return Room.databaseBuilder(context,DatabaseCreator.class,"users")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    /**
     * Interface for User
     * @return
     */
    public abstract UsersDao usersDao();
    //public abstract QuestionListDao questionListDao();
    //public  abstract QuestionOptionDao questionOptionDao();


}
