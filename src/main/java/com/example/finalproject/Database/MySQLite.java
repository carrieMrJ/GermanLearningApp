package com.example.finalproject.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.room.Database;

import com.example.finalproject.Data.Questions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite Database for Questions
 * @author  Mengru.Ji
 */
public class MySQLite extends SQLiteOpenHelper {


    //private static final String DB_NAME = "germanstudy.db";
    /**
     * Table Name of Question
     */
    private static final String TABLE_QUESTION = "questions";
    /**
     * Table Name of Wrong QUestions
     */
    private static final String TABLE_WRONG = "wrongquestions";
    /**
     * Table Name of FilePath
     */
    private static final String TABLE_PATH = "filePath";
    /**
     * Table Name of Favorites
     */
    private static final String TABLE_COLLECT = "collection";

    /**
     * context
     */
    private Context context;
    /**
     * SQLite Database
     */
    private SQLiteDatabase db;
    //ShareData data = new shareData();

    public MySQLite(Context context, String db_name){
        super(context, db_name,null,5);
        db = getWritableDatabase();
    }

    public MySQLite(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context, name, factory, version);
        this.context = context;
    }

    /**
     * Create tables for Database
     * @param db Database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        //for filepath
        db.execSQL("create table " + TABLE_PATH + "(_id integer primary key autoincrement,filepath text)");

        //for questions
        db.execSQL("create table "+TABLE_QUESTION
                + "(_id integer primary key autoincrement," +
                "description text," +
                "optionA text,optionB text, optionC text, optionD text, " +
                "answer integer, " +
                "type integer," +
                "iscollect integer)");

        //for wrong questions
        db.execSQL("create table "+TABLE_WRONG
                + "(_id integer primary key autoincrement," +
                "description text," +
                "optionA text,optionB text, optionC text, optionD text, " +
                "answer integer, " +
                "type integer," +
                "iscollect integer," +
                "wrongTime integer)");

        //for favorites
        db.execSQL("create table "+TABLE_COLLECT
                + "(_id integer primary key autoincrement," +
                "description text," +
                "optionA text,optionB text, optionC text, optionD text, " +
                "answer integer, " +
                "type integer," +
                "iscollect integer)");

    }

    /**
     * Update SQLite Database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


            /*db.execSQL("ALTER TABLE "+TABLE_COLLECT+" ADD COLUMN description text");
            db.execSQL("ALTER TABLE "+TABLE_COLLECT+" ADD COLUMN optionA text");
            db.execSQL("ALTER TABLE "+TABLE_COLLECT+" ADD COLUMN optionB text");
            db.execSQL("ALTER TABLE "+TABLE_COLLECT+" ADD COLUMN optionC text");
            db.execSQL("ALTER TABLE "+TABLE_COLLECT+" ADD COLUMN optionD text");
            db.execSQL("ALTER TABLE "+TABLE_COLLECT+" ADD COLUMN answer integer");
            db.execSQL("ALTER TABLE "+TABLE_COLLECT+" ADD COLUMN type integer");
            db.execSQL("ALTER TABLE "+TABLE_COLLECT+" ADD COLUMN iscollect integer");

            db.execSQL("ALTER TABLE "+TABLE_WRONG+" ADD COLUMN description text");
            db.execSQL("ALTER TABLE "+TABLE_WRONG+" ADD COLUMN optionA text");
            db.execSQL("ALTER TABLE "+TABLE_WRONG+" ADD COLUMN optionB text");
            db.execSQL("ALTER TABLE "+TABLE_WRONG+" ADD COLUMN optionC text");
            db.execSQL("ALTER TABLE "+TABLE_WRONG+" ADD COLUMN optionD text");
            db.execSQL("ALTER TABLE "+TABLE_WRONG+" ADD COLUMN answer integer");
            db.execSQL("ALTER TABLE "+TABLE_WRONG+" ADD COLUMN type integer");
            db.execSQL("ALTER TABLE "+TABLE_WRONG+" ADD COLUMN iscollect integer");
            db.execSQL("ALTER TABLE "+TABLE_WRONG+" ADD COLUMN wrongTime integer");

            Log.i("SSS","update two tables");

            db.execSQL("DROP TABLE " + TABLE_COLLECT);
            db.execSQL("DROP TABLE "+ TABLE_WRONG);

        db.execSQL("create table "+TABLE_WRONG
                + "(_id integer primary key autoincrement," +
                "description text," +
                "optionA text,optionB text, optionC text, optionD text, " +
                "answer integer, " +
                "type integer," +
                "iscollect integer," +
                "wrongTime integer)");


        db.execSQL("create table "+TABLE_COLLECT
                + "(_id integer primary key autoincrement," +
                "description text," +
                "optionA text,optionB text, optionC text, optionD text, " +
                "answer integer, " +
                "type integer," +
                "iscollect integer)");*/


    }

    /**
     * Delete all Questions in table
     * @param table_name
     */
    public void deleteAllQuestions(String table_name){

        db.execSQL("delete from " +table_name);
    }

    public void deleteCollection(Questions questions){

        String str = questions.getQDescription();

        db.execSQL("delete from " + TABLE_COLLECT +" where description = ?",new Object[]{str});
    }

    /**
     * Insert Question into Table
     * @param questions
     * @param table_name
     */
    //insert single question
    public void insertQuestion(Questions questions,String table_name){

        if(table_name == TABLE_QUESTION){
            if(!isQueAlreadyInsert(questions.getQDescription(),table_name)){
                ContentValues values = new ContentValues();
                values.put("description",questions.getQDescription());
                values.put("optionA",questions.getOptionA());
                values.put("optionB",questions.getOptionB());
                values.put("optionC",questions.getOptionC());
                values.put("optionD",questions.getOptionD());
                values.put("answer",questions.getAnswer());
                values.put("type",questions.getQType());
                values.put("iscollect",questions.isCollect());
                db.insert(TABLE_QUESTION,"_id",values);
            }
        }else if(table_name == TABLE_WRONG){
            if( !isQueAlreadyInsert(questions.getQDescription(),table_name)){
                ContentValues values = new ContentValues();
                values.put("description",questions.getQDescription());
                values.put("optionA",questions.getOptionA());
                values.put("optionB",questions.getOptionB());
                values.put("optionC",questions.getOptionC());
                values.put("optionD",questions.getOptionD());
                values.put("answer",questions.getAnswer());
                values.put("type",questions.getQType());
                values.put("iscollect",questions.isCollect());
                values.put("wrongTime",questions.getWrongTime());
                db.insert(TABLE_WRONG,"_id",values);
            }else{
                db.execSQL("update "+TABLE_WRONG+" set wrongTime = ? where description = ?",new Object[]{questions.getWrongTime(),questions.getQDescription()});
            }
        }else if(table_name == TABLE_COLLECT){
            if(!isQueAlreadyInsert(questions.getQDescription(),table_name)){
                ContentValues values = new ContentValues();
                values.put("description",questions.getQDescription());
                values.put("optionA",questions.getOptionA());
                values.put("optionB",questions.getOptionB());
                values.put("optionC",questions.getOptionC());
                values.put("optionD",questions.getOptionD());
                values.put("answer",questions.getAnswer());
                values.put("type",questions.getQType());
                values.put("iscollect",questions.isCollect());
                db.insert(TABLE_COLLECT,"_id",values);
            }
        }



    }

    /**
     * Get all Questions in Favorites
     * @return List of Questions
     */
    public List<Questions> getCollectQuestions(){
        List<Questions> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from " + TABLE_COLLECT,null);

        long count = getCount(TABLE_COLLECT);
        //Log.i("QQQ",count + "  size!");

        if(count>0){
            cursor.moveToFirst();
            for(int i = 0; i < count; i++){
                cursor.moveToPosition(i);
                Questions questions = new Questions();
                questions.setQId(cursor.getInt(cursor.getColumnIndex("_id")));
                questions.setqDescription(cursor.getString(cursor.getColumnIndex("description")));
                questions.setOptionA(cursor.getString(cursor.getColumnIndex("optionA")));
                questions.setOptionB(cursor.getString(cursor.getColumnIndex("optionB")));
                questions.setOptionC(cursor.getString(cursor.getColumnIndex("optionC")));
                questions.setOptionD(cursor.getString(cursor.getColumnIndex("optionD")));
                questions.setAnswer(cursor.getInt(cursor.getColumnIndex("answer")));
                questions.setQType(cursor.getInt(cursor.getColumnIndex("type")));
                questions.setCollect(cursor.getInt(cursor.getColumnIndex("iscollect")));
                //questions.setWrongTime(cursor.getInt(cursor.getColumnIndex("wrongtime")));

                questions.setSelectedAnswer(-1);

                list.add(questions);
            }
        }
        return list;
    }


    /**
     * Get all Questions in Wrong Questions Table
     * @return List of Wrong Question
     */
    public List<Questions> getWrongQuestions(){

        List<Questions> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from " + TABLE_WRONG,null);

        long count = getCount(TABLE_WRONG);
        //Log.i("QQQ",count + "  size!");

        if(count>0){
            cursor.moveToFirst();
            for(int i = 0; i < count; i++){
                cursor.moveToPosition(i);
                Questions questions = new Questions();
                questions.setQId(cursor.getInt(cursor.getColumnIndex("_id")));
                questions.setqDescription(cursor.getString(cursor.getColumnIndex("description")));
                questions.setOptionA(cursor.getString(cursor.getColumnIndex("optionA")));
                questions.setOptionB(cursor.getString(cursor.getColumnIndex("optionB")));
                questions.setOptionC(cursor.getString(cursor.getColumnIndex("optionC")));
                questions.setOptionD(cursor.getString(cursor.getColumnIndex("optionD")));
                questions.setAnswer(cursor.getInt(cursor.getColumnIndex("answer")));
                questions.setQType(cursor.getInt(cursor.getColumnIndex("type")));
                questions.setCollect(cursor.getInt(cursor.getColumnIndex("iscollect")));
                questions.setWrongTime(cursor.getInt(cursor.getColumnIndex("wrongTime")));

                questions.setSelectedAnswer(-1);

                list.add(questions);
            }
        }
        return list;
    }


    /**
     * Insert FilePath into Table
     * @param path
     */
    public void insertPath(String path){
        ContentValues contentValues = new ContentValues();
        contentValues.put("filepath",path);
        db.insert(TABLE_PATH,"_id",contentValues);
    }

    /**
     * Insert Questions into Table from File
     * @param context
     * @param fileName
     */
    public void insertQueListFromFileToDB(Context context, String fileName){

        if(this.isPathAlreadyInsert(fileName)){
            Log.i("Test File","Already insert file");
            return;
        } else{
            this.insertPath(fileName);
        }

        AssetManager assetManager = context.getApplicationContext().getAssets();
        try {
            InputStream inputStream = assetManager.open("questionList1");
            if (inputStream != null) {
                InputStreamReader inputReader = new InputStreamReader(inputStream);
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line1;
                String line2;
                String line3;
                //Read line by line
                while ((line1 = buffReader.readLine()) != null && (line2 = buffReader.readLine()) != null
                        && (line3 = buffReader.readLine()) != null) {

                    String[] temp = line2.split("/");
                    Questions questions = new Questions();
                    questions.setqDescription(line1);
                    questions.setOptionA(temp[0]);
                    questions.setOptionB(temp[1]);
                    questions.setOptionC(temp[2]);
                    questions.setOptionD(temp[3]);
                    questions.setAnswer(convert(line3));
                    questions.setQType(1);
                    questions.setCollect(0);

                    insertQuestion(questions,TABLE_QUESTION);
                }
                inputStream.close();
            }
        } catch (java.io.FileNotFoundException e) {
            Log.d("Test File", "The File doesn't not exist.");
        } catch (IOException e) {
            Log.d("Test File", e.getMessage());
        }
    }

    /**
     * Convert Letters to Integer(A-D:0-3)
     * @param option A,B,C,D
     * @return 0,1,2,3
     */
    private int convert(String option){
        int temp = Integer.parseInt(option);
        return temp;
    }

    /**
     * check if this question has already inserted
     * @param qDescription Description of this question
     * @param table_name name of table
     * @return
     */
    //if the question has already insert
    public boolean isQueAlreadyInsert(String qDescription,String table_name){

        List<String> list = getQDescription(table_name);
        if(list.contains(qDescription)){
            return true;
        }
        return false;
    }

    /**
     * Get descriptions of all Questions
     * @param table_name
     * @return
     */
    //get all question descriptions
    private List<String> getQDescription(String table_name) {

        ArrayList<String> list = new ArrayList<>();



        if(getCount(table_name) > 1){
            Cursor cursor = db.rawQuery("select * from " + table_name, null);
            while(cursor.moveToNext()){
                String description = cursor.getString(cursor.getColumnIndex("description"));
                list.add(description);
            }
        }

        return list;
    }

    /**
     * Check if this File has already been inserted
     * @param path
     * @return
     */
    //if this file has already be loaded
    public boolean isPathAlreadyInsert(String path){
        ArrayList<String> list = getAlreadyInsertPath();
        if(list.contains(path)){
            return true;
        }
        return false;
    }

    /**
     * Get Id of Question by given description of Question
     * @param qDescription
     * @return
     */
    //get questions ids by question title
    public int getQueIdByQueTitle(String qDescription){

        Cursor cursor = db.rawQuery("select * from "+TABLE_QUESTION+" where qDescription = description",null);
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        return id;

    }

    /**
     * Get all inserted File
     * @return
     */
    //get all insert filepath
    public ArrayList<String> getAlreadyInsertPath(){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PATH, null);
        while(cursor.moveToNext()){
            String path = cursor.getString(cursor.getColumnIndex("filepath"));
            list.add(path);
        }
        return list;
    }


    /**
     * Get all Questions in Table Questions
     * @return
     */
    public List<Questions> getQuestion() {

        List<Questions> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from " + TABLE_QUESTION,null);

        long count = getCount(TABLE_QUESTION);
        Log.i("QQQ",count + "  size!");

        if(count>0){
            cursor.moveToFirst();
            for(int i = 0; i < count; i++){
                cursor.moveToPosition(i);
                Questions questions = new Questions();
                questions.setQId(cursor.getInt(cursor.getColumnIndex("_id")));
                questions.setqDescription(cursor.getString(cursor.getColumnIndex("description")));
                questions.setOptionA(cursor.getString(cursor.getColumnIndex("optionA")));
                questions.setOptionB(cursor.getString(cursor.getColumnIndex("optionB")));
                questions.setOptionC(cursor.getString(cursor.getColumnIndex("optionC")));
                questions.setOptionD(cursor.getString(cursor.getColumnIndex("optionD")));
                questions.setAnswer(cursor.getInt(cursor.getColumnIndex("answer")));
                questions.setQType(cursor.getInt(cursor.getColumnIndex("type")));
                questions.setCollect(cursor.getInt(cursor.getColumnIndex("iscollect")));
                //questions.setWrongTime(cursor.getInt(cursor.getColumnIndex("wrongtime")));

                questions.setSelectedAnswer(-1);

                list.add(questions);
            }
        }
        return list;
    }

    /**
     * Get Size of Table
     * @param table_name
     * @return
     */
    public long getCount(String table_name){

        Cursor cursor = db.rawQuery("select count(*) from " + table_name, null);
        cursor.moveToFirst();
        long result = cursor.getLong(0);
        cursor.close();

        return result;
    }


    /**
     * Update Data in Table Favorites
     * @param table_name
     * @param questions
     */
    public void updateIsCollect(String table_name,Questions questions){
        db.execSQL("UPDATE "+ table_name +" set iscollect=? where description=?",new Object[]{questions.isCollect(),questions.getQDescription()});
    }


}
