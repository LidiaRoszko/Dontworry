package com.lila.dontworry.Logic;

/**
 * Created by Gustav on 25.11.2017.
 */

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper implements Serializable {

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

    private static DatabaseHandler instance;

    public static DatabaseHandler getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHandler(context);

        return instance;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


        resetDatabase();

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
                + KEY_HINT_ID + " INTEGER," + KEY_OBJECT_ID + " INTEGER," + " PRIMARY KEY (" + KEY_HINT_ID + ", " + KEY_OBJECT_ID + ")" + ")";
        db.execSQL(CREATE_HINT_OBJECTS_TABLE);

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

    private void wipeTables() {
        onUpgrade(this.getReadableDatabase(), DATABASE_VERSION, DATABASE_VERSION);
    }

    public void resetDatabase() {
        wipeTables();

        Question quest =new Question("Did you enjoy your call with %s?");
        Question quest2 =new Question("Did you enjoy to visit %s?");
        Question quest3 =new Question("Do you like to eat candy?");
        Hint hint =  new Hint("Call %s.");
        Hint hint2 =  new Hint("Visit %s.");
        Hint hint3 =  new Hint("Eat some candy.");
        DisplayObject obj = new DisplayObject(ObjectType.CONTACT, "Bodirsky");
        DisplayObject obj2 = new DisplayObject(ObjectType.PLACE, "APB");

        long qID = add(quest);
        long qID2 = add(quest2);
        long qID3 = add(quest3);
        long hID = add(hint);
        long hID2 = add(hint2);
        long hID3 = add(hint3);

        addConnected(obj, (int)qID, (int)hID);
        addConnected(obj2, (int)qID2, (int)hID2);
    }

    public int getNumberOfQuestions(){
        return countRows(TABLE_QUESTIONS);
}

    public Question nextQuestion() {
        int rows = countRows(TABLE_QUESTIONS) + 1;
        int randomQuestionId = 1;
        if (rows > 1)
            randomQuestionId = ThreadLocalRandom.current().nextInt(1, rows);
        return getQuestion(randomQuestionId);
    }

    public Hint nextHint() {
        int rows = countRows(TABLE_HINTS) + 1;
        int randomHintId = 1;
        if (rows > 1)
            randomHintId = ThreadLocalRandom.current().nextInt(1, rows);
        return getHint(randomHintId);

    }


    private boolean contains(String table, String column, String value) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table, new String[] { column }, column + "=?",
                new String[] { value }, null, null, null, null);

        boolean result = false;
        if (cursor != null) {
            result = (cursor.getCount() > 0);
            cursor.close();
        }
        db.close();

        return result;

    }


    private int countRows(String table) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table, null, null,
                null, null, null, null, null);

        int result = 0;
        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        db.close();

        return result;

    }


    private void connectObject(DisplayObject displayObject, Question question) {
        //if (contains(TABLE_QUESTION_OBJECTS, KEY_QUESTION_ID, String.valueOf(question.getId())))
        //    return;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_ID, question.getId());
        values.put(KEY_OBJECT_ID, displayObject.getId());

        // Inserting Row
        db.insert(TABLE_QUESTION_OBJECTS, null, values);
        db.close(); // Closing database connection
    }

    private void connectObject(DisplayObject displayObject, Hint hint) {
        //if (contains(TABLE_HINT_OBJECTS, KEY_HINT_ID, String.valueOf(hint.getId())))
        //    return;

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



    private long add(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, question.getText());
        values.put(KEY_ANSWER, question.isAnswer());

        // Inserting Row
        long id = db.insert(TABLE_QUESTIONS, null, values);
        db.close(); // Closing database connection
        return  id;
    }

    private long add(Hint hint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, hint.getText());

        // Inserting Row
        long id = db.insert(TABLE_HINTS, null, values);
        db.close(); // Closing database connection
        return id;
    }

    private long add(DisplayObject displayObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, displayObject.getContent());
        values.put(KEY_TYPE, displayObject.getObjectType().toString());

        // Inserting Row
        long id = db.insert(TABLE_OBJECTS, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public long addConnected(DisplayObject displayObject, int questionId, int hintId) {
        int objId = (int)add(displayObject);
        connectObject(getObject(objId), getQuestion(questionId));
        connectObject(getObject(objId), getHint(hintId));
        return objId;
    }

    private void delete(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, KEY_QUESTION_ID + " = ?",
                new String[] { String.valueOf(question.getId()) });
        db.close();
    }

    private void delete(Hint hint) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HINTS, KEY_HINT_ID + " = ?",
                new String[] { String.valueOf(hint.getId()) });
        db.close();
    }

    private void delete(DisplayObject displayObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBJECTS, KEY_OBJECT_ID + " = ?",
                new String[] { String.valueOf(displayObject.getId()) });
        db.close();
    }

    private DisplayObject getObject(int objectId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorObject = db.query(TABLE_OBJECTS, null, KEY_OBJECT_ID + " = ?",
                new String[] { String.valueOf(objectId) }, null, null, null, null);

        DisplayObject displayObject = DisplayObject.getDefault();

        if (cursorObject != null) {
            if (cursorObject.getCount() > 0) {
                cursorObject.moveToFirst();
                int o_id = cursorObject.getInt(0);
                String o_text = cursorObject.getString(1);
                ObjectType o_type = ObjectType.valueOf(cursorObject.getString(2));
                displayObject = new DisplayObject(o_id, o_type, o_text);
                cursorObject.close();
            }
        }
        db.close();
        return displayObject;

    }

    private DisplayObject getObject(int connectedId, String TABLE) {
        DisplayObject displayObject = DisplayObject.getDefault();
        String KEY;


        if (TABLE.equals(TABLE_QUESTION_OBJECTS))
            KEY = KEY_QUESTION_ID;
        else if( TABLE.equals(TABLE_HINT_OBJECTS))
            KEY = KEY_HINT_ID;
        else
            return displayObject;

        SQLiteDatabase db = this.getReadableDatabase();

        final String OBJECT_QUERY = "SELECT * FROM " + TABLE +
                " a INNER JOIN " + TABLE_OBJECTS + " b ON a." + KEY_OBJECT_ID + "=b." + KEY_OBJECT_ID + " WHERE a." + KEY + "=?";
        Cursor cursorObject = db.rawQuery(OBJECT_QUERY, new String[]{String.valueOf(connectedId)});

        if (cursorObject != null) {
            if (cursorObject.getCount() > 0) {
                cursorObject.moveToFirst();
                int o_id = cursorObject.getInt(0);
                displayObject = getObject(o_id);
                cursorObject.close();
            }
        }
        db.close();
        return displayObject;
    }

    private Question getQuestion(int questionId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorQuest = db.query(TABLE_QUESTIONS, null, KEY_QUESTION_ID + " = ?",
                new String[] { String.valueOf(questionId) }, null, null, null, null);



        Question question = Question.getDefault();
        if (cursorQuest != null) {
            if (cursorQuest.getCount() > 0) {
                cursorQuest.moveToFirst();
                int q_id = cursorQuest.getInt(0);
                String q_text = cursorQuest.getString(1);
                boolean q_answer = cursorQuest.getInt(2) != 0;
                DisplayObject q_obj = getObject(questionId, TABLE_QUESTION_OBJECTS);
                question = new Question(q_text, q_id, q_answer, q_obj);
                cursorQuest.close();
            }
        }

        db.close();

        return question;
    }

    private Hint getHint(int hintId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HINTS, null, KEY_HINT_ID + " = ?",
                new String[] { String.valueOf(hintId) }, null, null, null, null);

        Hint hint = Hint.getDefault();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int h_id = cursor.getInt(0);
                String h_text = cursor.getString(1);
                DisplayObject h_obj = getObject(hintId, TABLE_HINT_OBJECTS);
                //System.out.println(h_obj);
                hint = new Hint(h_text, h_id, h_obj);
                cursor.close();
            }
        }
        db.close();
        return hint;

    }

}