package com.bkai.flowerdetect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bkai.flowerdetect.models.Flower;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by marsch on 4/20/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "flowerdb.db";
    private static final String SVM_NAME = "svm.yml";
    private String DB_PATH;
    private Context mContext;
    private SQLiteDatabase myDataBase;

    // Table Name
    private static final String TABLE_FLOWER = "flowers";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_SCIENCE_NAME = "science_name";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    // Table create Statements
    private static final String CREATE_TABLE_FLOWER = "CREATE TABLE "
            + TABLE_FLOWER + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + "TEXT,"
            + KEY_SCIENCE_NAME + "TEXT,"
            + KEY_DESCRIPTION + "TEXT" + ")";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mContext = context;
        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mContext = context;
        Log.e(TAG, "DBHelper construction");

        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    private boolean checkDataSVM(){

        String SVM_PATH = this.mContext.getApplicationInfo().dataDir + "/" + SVM_NAME;
        File svm_file = new File(SVM_PATH);

        if (svm_file.exists())
            return true;
        else
            return false;
    }

    private void copySVMData() throws IOException {
        InputStream myInput = mContext.getAssets().open(SVM_NAME);

        String outFileName = this.mContext.getApplicationInfo().dataDir + "/" + SVM_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while((length = myInput.read(buffer))>0){
            myOutput.write(buffer,0,length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

        Log.e(TAG, "Copy finish");

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     **/

    public void createDataBase() throws IOException{
        Log.e(TAG, "Create database");

        boolean dbExist = checkDataBase();

        boolean svmExist = checkDataSVM();

        if (svmExist){

        } else {
            copySVMData();
        }

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {
                Log.e(TAG, "Copy database");

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    public void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        db.execSQL(CREATE_TABLE_FLOWER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_FLOWER);
        onCreate(db);
    }

    public boolean createFlower(Flower flower){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(KEY_NAME, flower.getName());
        content.put(KEY_SCIENCE_NAME, flower.getScienceName());
        content.put(KEY_DESCRIPTION, flower.getDescription());

        try {
            db.insert(TABLE_FLOWER, null, content);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public ArrayList<Flower> getAllFowers(){
        ArrayList<Flower> flowers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FLOWER, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            flowers.add(new Flower(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                                    cursor.getString(cursor.getColumnIndex(KEY_SCIENCE_NAME)),
                                    cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))
            ));
            cursor.moveToNext();
        }
        return flowers;
    }

    public Flower getFlowerById(int id){
        Flower flower = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String queryById  = "SELECT * FROM " + TABLE_FLOWER
                + " WHERE ID = " + id;
        Cursor cursor = null;
        cursor = db.rawQuery(queryById, null);

        cursor.moveToFirst();

        if (cursor.getCount()>0) {
            flower = new Flower(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_SCIENCE_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))
            );
        }

        return flower;
    }
}
