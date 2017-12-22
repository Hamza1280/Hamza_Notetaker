package com.example.hamzajavaid.notetaker;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MyDbHandler  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes.db";
    public static final String INFO_NOTES = "note";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String IMAGE = "image";
    String query = "CREATE TABLE " + INFO_NOTES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            IMAGE + " BLOB" +
            ")";

    public MyDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Create", "chk");
        db.execSQL(query);
    }
    ImageConversion imageConversion = new ImageConversion();

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INFO_NOTES);
        onCreate(db);
    }
    public void addnote(String titleee,String desc,Bitmap img) {
        Log.i("add", "chkl");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, titleee);
        values.put(COLUMN_DESCRIPTION, desc);
        byte[] data = null;
       if(img!=null){
           data = imageConversion.getBitmapAsByteArray(img);
           values.put(IMAGE,data);
       }else {
           values.put(IMAGE, data);
       }
        db.insert(INFO_NOTES, null, values);
    }
    public ArrayList<NoteItem> getData() {
        ArrayList<NoteItem> values = new ArrayList<NoteItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + INFO_NOTES + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        for(c.moveToFirst(); !c.isAfterLast();c.moveToNext()) {

            NoteItem noteItem = new NoteItem(
                    c.getInt(c.getColumnIndex(COLUMN_ID)),
                    c.getString(c.getColumnIndex(COLUMN_TITLE)),
                    c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)),
                    imageConversion.bytesToBitmap(c.getBlob(c.getColumnIndex(IMAGE))));
            values.add(noteItem);
        }
            db.close();
        Collections.reverse(values);
        return values;
    }

    public void deletenote(String Title){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + INFO_NOTES + " WHERE " + COLUMN_TITLE + "= '" + Title + "'");
    }
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("delete * from "+ INFO_NOTES);
        db.execSQL("DELETE FROM " + INFO_NOTES + ";");
        //onCreate(db);
        //db.close();
    }

    public void updateNote(String newTitle, String newDescription, Bitmap newimage, String oldTitle, String oldDescription, byte[] oldImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        boolean edited = false;
        if(!newTitle.equals(oldTitle))
        {
            cv.put(COLUMN_TITLE,newTitle);
            edited = true;
        }
        if(!newDescription.equals(oldDescription)){
            cv.put(COLUMN_DESCRIPTION,newDescription);
            edited = true;
        }
        if(newimage!= null){
            cv.put(IMAGE,ImageConversion.getBitmapAsByteArray(newimage));
            edited = true;
        }
        String cond = COLUMN_TITLE + " = '" + oldTitle + "' and " + COLUMN_DESCRIPTION + " = '" + oldDescription + "'";
        if(edited){
            db.update(INFO_NOTES, cv, cond, null);
        }
    }
}