package com.example.finalproject.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interface User Dao
 * @author Junqi.Sun
 */
@Dao
public interface UsersDao {

    /**
     * insert new user into table
     * @param user
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(User user);

    /**
     * update a set of user
     * @param users
     */
    @Update
    void updateUsers(User... users);

    /**
     * update user
     * @param user
     */
    @Update
    void updateUser(User user);

    /**
     * delete a set of users
     * @param users
     */
    @Delete
    void deleteUsers(User...users);

    /**
     * get all users
     * @return
     */
    @Query("select * from users")
    List<User> loadAllUsers();

    /**
     * get user object by username
     * @param username
     * @return
     */
    @Query("select * from users where username = :username")
    User findUserWithName(String username);

   /* @Query("select * from users where userID = :userID")
    User findUserWithID(int userID);*/

}
