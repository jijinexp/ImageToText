// 159336
//Assignment 3: Image To Text Editor
//Student ID:18032679
//Name:Jijin Johney Mundappallil

package com.example.imagetotexteditor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {


    //Application Context for TextDB
    DBHandler(Context context) {
        super(context, "TextDB", null, 1);
    }


    //db SQLiteDatabase
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE textnote (id INTEGER PRIMARY KEY, name TEXT, message TEXT)");
    }


    // SQLiteDatabase
    //oldVersion Older version number
    //New Version number
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS textnote");
        onCreate(db);
    }


    // id for the Note Object to be deleted
    //variable if the delete is successful or not
    boolean deleteNote(long id) {

        SQLiteDatabase db = getWritableDatabase();
        String id_string = "id = '" + id + "'";

        //Returns the number of rows deleted
        int deleted = db.delete("textnote", id_string, null);
        db.close();

        return deleted > 0;
    }


     //name Note name
     //Note Message
     //variable if the create is successful or not
    boolean addNote(String name, String message) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("message", message);

        //Returns id if successful, -1 if not
        long added = db.insert("textnote", null, contentValues);
        db.close();

        return added != -1;
    }


    //id Note id
    //Note name
    //Note message
     //variable if the update is successful or not
    boolean updateNote(long id, String name, String message) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String id_string = "id = '" + id + "'";
        contentValues.put("name", name);
        contentValues.put("message", message);
        int update = db.update("textnote", contentValues, id_string, null);

        return update > 0;
    }


    // returns ArrayList\<Note>\
    ArrayList<Note> getAllNotes() {

        ArrayList<Note> itemArray = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM textnote", null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getInt(0);
                String name = cursor.getString(1);
                String message = cursor.getString(2);
                Note note = new Note(id, name, message);
                itemArray.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return itemArray;
    }

}