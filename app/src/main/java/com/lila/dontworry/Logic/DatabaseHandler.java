package com.lila.dontworry.Logic;

/**
 * Created by Gustav on 25.11.2017.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DontWorryData";

    // Contacts table name

    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_HINTS = "hints";
    private static final String TABLE_RELEVANT_FOR = "relevant_for";
    private static final String TABLE_OBJECTS = "objects";
    private static final String TABLE_QUESTION_OBJECTS = "question_objects";
    private static final String TABLE_HINT_OBJECTS = "hint_objects";


    // Contacts Table Columns names
    private static final String KEY_QUESTION_ID = "question_id";
    private static final String KEY_TEXT = "text";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_INVERTED = "inverted";
    private static final String KEY_HINT_ID = "hint_id";
    private static final String KEY_OBJECT_ID = "object_id";
    private static final String KEY_TYPE = "type";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + KEY_QUESTION_ID + " INTEGER PRIMARY KEY," + KEY_TEXT + " TEXT,"
                + KEY_ANSWER + " BIT" + ")";
        db.execSQL(CREATE_QUESTIONS_TABLE);

        String CREATE_HINTS_TABLE = "CREATE TABLE " + TABLE_HINTS + "("
                + KEY_HINT_ID + " INTEGER PRIMARY KEY," + KEY_TEXT + " TEXT"
                + ")";
        db.execSQL(CREATE_HINTS_TABLE);

        String CREATE_RELEVANT_FOR_TABLE = "CREATE TABLE " + TABLE_RELEVANT_FOR + "("
                + KEY_QUESTION_ID + " INTEGER," + KEY_HINT_ID + " INTEGER,"
                + KEY_INVERTED + " BIT," + " PRIMARY KEY (" + KEY_QUESTION_ID + ", " + KEY_HINT_ID + ")" + ")";
        db.execSQL(CREATE_RELEVANT_FOR_TABLE);

        String CREATE_OBJECTS_TABLE = "CREATE TABLE " + TABLE_OBJECTS + "("
                + KEY_OBJECT_ID + " INTEGER PRIMARY KEY," + KEY_TEXT + " TEXT,"
                + KEY_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_OBJECTS_TABLE);

        String CREATE_QUESTION_OBJECTS_TABLE = "CREATE TABLE " + TABLE_QUESTION_OBJECTS + "("
                + KEY_QUESTION_ID + " INTEGER," + KEY_OBJECT_ID + " INTEGER," + " PRIMARY KEY (" + KEY_QUESTION_ID + ", " + KEY_OBJECT_ID + ")" + ")";
        db.execSQL(CREATE_QUESTION_OBJECTS_TABLE);

        String CREATE_HINT_OBJECTS_TABLE = "CREATE TABLE " + TABLE_HINT_OBJECTS + "("
                + KEY_HINT_ID + " INTEGER," + KEY_OBJECT_ID + " INTEGER," + ")" + " PRIMARY KEY (" + KEY_HINT_ID + ", " + KEY_OBJECT_ID + ")" + ")";


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RELEVANT_FOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_OBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HINT_OBJECTS);


        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */


    public Question nextQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();

        int id = 1;
        Cursor cursor = db.query(TABLE_QUESTIONS, null, KEY_QUESTION_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        Question question = Question.getDefault();
        if (cursor != null) {
            System.out.println(cursor.getCount());
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int q_id = cursor.getInt(0);
                String q_text = cursor.getString(1);
                boolean q_answer = cursor.getInt(2) != 0;
                question = new Question(q_text, q_id, q_answer);
            }
        }
        db.close();

        return question;
    }

    public Hint nextHint() {
        SQLiteDatabase db = this.getReadableDatabase();

        int id = 1;
        Cursor cursor = db.query(TABLE_HINTS, null, KEY_HINT_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        Hint hint = Hint.getDefault();
        if (cursor != null) {
            System.out.println(cursor.getCount());
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int h_id = cursor.getInt(0);
                String h_text = cursor.getString(1);
                hint = new Hint(h_text, h_id);
            }
        }
        db.close();
        return hint;

    }


    private boolean contains(String table, String column, String value) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table, new String[] { column }, column + "=?",
                new String[] { value }, null, null, null, null);

        boolean result = false;
        if (cursor != null)
            result = (cursor.getCount() > 0);
        db.close();

        return result;

    }


    public void connectObject(DisplayObject displayObject, Question question) {
        if (contains(TABLE_QUESTION_OBJECTS, KEY_QUESTION_ID, String.valueOf(question.getId())))
            return;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_ID, question.getId());
        values.put(KEY_OBJECT_ID, displayObject.getId());

        // Inserting Row
        db.insert(TABLE_QUESTION_OBJECTS, null, values);
        db.close(); // Closing database connection
    }

    public void connectObject(DisplayObject displayObject, Hint hint) {
        if (contains(TABLE_HINT_OBJECTS, KEY_HINT_ID, String.valueOf(hint.getId())))
            return;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HINT_ID, hint.getId());
        values.put(KEY_OBJECT_ID, displayObject.getId());

        // Inserting Row
        db.insert(TABLE_HINT_OBJECTS, null, values);
        db.close(); // Closing database connection
    }

    public int answerQuestion(Question question, boolean answer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ANSWER, question.isAnswer());

        // updating row
        int result = db.update(TABLE_QUESTIONS, values, KEY_QUESTION_ID + " = ?",
                new String[] { String.valueOf(question.getId()) });
        db.close();
        return result;
    }



    private void add(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, question.getText());
        values.put(KEY_ANSWER, question.isAnswer());

        // Inserting Row
        db.insert(TABLE_QUESTIONS, null, values);
        db.close(); // Closing database connection
    }

    private void add(Hint hint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, hint.getText());

        // Inserting Row
        db.insert(TABLE_HINTS, null, values);
        db.close(); // Closing database connection
    }

    private void add(DisplayObject displayObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, displayObject.getContent());
        values.put(KEY_TYPE, displayObject.getObjectType().toString());

        // Inserting Row
        db.insert(TABLE_OBJECTS, null, values);
        db.close(); // Closing database connection
    }


/*

    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    */

}