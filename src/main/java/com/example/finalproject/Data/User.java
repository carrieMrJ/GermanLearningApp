package com.example.finalproject.Data;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Random;

/**
 * User Object
 * @author Junqi.Sun
 */
@Entity(tableName = "users",indices = {@Index(value = "username",unique = true)})
public class User implements Serializable {

    /**
     * username
     */
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    private String userName;

    /**
     * user password
     */
    @ColumnInfo(name = "userPassword")
    private String userPw;

    /**
     * user's points
     */
    @ColumnInfo(name = "points")
    private int points;

    /**
     * user's consecutive check in days
     */
    @ColumnInfo(name = "checkInDays")
    private int checkInDays;
    /**
     * last date that the user have checked in
     */
    @ColumnInfo(name = "lastCheckInDate")
    private String lastDate;

    /**
     * Constructor
     * @param userName
     * @param userPw
     */
    public User(String userName, String userPw){
        //this.userId = getRandomString(6);
        this.userName = userName;
        this.userPw = userPw;
        this.points = 0;
        this.checkInDays = 0;
        this.lastDate = "";
    }
    /*public User (String userName,String userPw,int points){
        this.userName = userName;
        this.userPw = userPw;
        this.points = points;
    }

    @Ignore
    public User(){

    }*/


    /**
     * get user name
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * set user name
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * get user password
     * @return
     */
    public String getUserPw() {
        return userPw;
    }

    /**
     * set user password
     * @param userPw
     */
    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    /**
     * get points
     * @return
     */
    public int getPoints() {
        return points;
    }

    /**
     * set poitns
     * @param permission
     */
    public void setPoints(int permission) {
        this.points = permission;
    }

    /**
     * get consecutive check in days
     * @return
     */
    public int getCheckInDays() {
        return checkInDays;
    }

    /**
     *
     * @param checkInDays
     */
    public void setCheckInDays(int checkInDays) {
        this.checkInDays = checkInDays;
    }


    /**
     *
     * @return
     */
    public String getLastDate() {
        return lastDate;
    }

    /**
     *
     * @param lastDate
     */
    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }
}
