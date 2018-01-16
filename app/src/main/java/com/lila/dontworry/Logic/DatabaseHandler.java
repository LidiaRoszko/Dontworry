package com.lila.dontworry.Logic;

/**
 * Created by Gustav on 25.11.2017.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.INotificationSideChannel;

public class DatabaseHandler extends SQLiteOpenHelper implements Serializable {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DontWorryData";

    // table names
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_HINTS = "hints";
    private static final String TABLE_RELEVANT_FOR = "relevant_for";
    private static final String TABLE_OBJECTS = "objects";
    private static final String TABLE_QUESTION_OBJECTS = "question_objects";
    private static final String TABLE_HINT_OBJECTS = "hint_objects";
    private static final String TABLE_PLACES = "places";
    private static final String TABLE_EVENTS = "events";
    private static final String TABLE_WEATHER = "weather";


    // Table Columns names
    private static final String KEY_QUESTION_ID = "question_id";
    private static final String KEY_TEXT = "text";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_PERMA = "permanent";
    private static final String KEY_INVERTED = "inverted";
    private static final String KEY_HINT_ID = "hint_id";
    private static final String KEY_OBJECT_ID = "object_id";
    private static final String KEY_TYPE = "type";

    private static final String KEY_PLACE_ID = "place_id";
    private static final String KEY_LONG = "longitude";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_EVENT_ID = "event_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_PLACE = "place";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LINK = "link";
    private static final String KEY_POS = "position";






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
                + KEY_ANSWER + " INTEGER, " + KEY_PERMA + " INTEGER" + ")";
        db.execSQL(CREATE_QUESTIONS_TABLE);

        System.out.println(CREATE_QUESTIONS_TABLE);

        String CREATE_HINTS_TABLE = "CREATE TABLE " + TABLE_HINTS + "("
                + KEY_HINT_ID + " INTEGER PRIMARY KEY," + KEY_TEXT + " TEXT"
                + ")";
        db.execSQL(CREATE_HINTS_TABLE);

        String CREATE_RELEVANT_FOR_TABLE = "CREATE TABLE " + TABLE_RELEVANT_FOR + "("
                + KEY_QUESTION_ID + " INTEGER," + KEY_HINT_ID + " INTEGER,"
                + KEY_INVERTED + " INTEGER," + " PRIMARY KEY (" + KEY_QUESTION_ID + ", " + KEY_HINT_ID + ")" + ")";
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

        // ###

        String CREATE_PLACES_TABLE = "CREATE TABLE " + TABLE_PLACES + "("
                + KEY_PLACE_ID + " INTEGER," + KEY_LONG + " DOUBLE," + KEY_LAT + " DOUBLE," + " PRIMARY KEY (" + KEY_PLACE_ID + ")" + ")";
        db.execSQL(CREATE_PLACES_TABLE);

        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_EVENT_ID + " INTEGER," + KEY_DATE + " TEXT," + KEY_PLACE + " TEXT," + KEY_TITLE + " TEXT," + KEY_LINK + " TEXT," + KEY_POS + " INTEGER, " + " PRIMARY KEY (" + KEY_EVENT_ID + ")" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);


        /*
        String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_WEATHER + "("
                + KEY_HINT_ID + " INTEGER," + KEY_OBJECT_ID + " INTEGER," + " PRIMARY KEY (" + KEY_HINT_ID + ", " + KEY_OBJECT_ID + ")" + ")";
        db.execSQL(CREATE_HINT_OBJECTS_TABLE);
        */
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);


        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    private void wipeTables() {
        onUpgrade(this.getReadableDatabase(), DATABASE_VERSION, DATABASE_VERSION);
    }


    private void addQuestionHint(String questionText, String hintText, boolean inverted, boolean permanent) {
        Question quest =new Question(questionText);
        quest.setPermanent(permanent);
        Hint hint =  new Hint(hintText);
        long qID = add(quest);
        long hID = add(hint);
        connectQH((int)qID, (int)hID, inverted);
    }

    private void connectQH(int qId, int hId, boolean inverted) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_ID, qId);
        values.put(KEY_HINT_ID, hId);
        values.put(KEY_INVERTED, inverted ? -1 : 1);

        // Inserting Row
        db.insert(TABLE_RELEVANT_FOR, null, values);
        db.close(); // Closing database connection
    }

    public void resetDatabase() {
        wipeTables();

        addQuestionHint("Did you enjoy your call with %s?", "Call %s.", false, true);
        addQuestionHint("Did you enjoy to visit %s?", "Visit %s.", false, true);
        addQuestionHint("Do you like to eat candy?", "Eat some candy.", false, false);
        addQuestionHint("Do you like to drink?", "Go out and have a drink!", false, false);
        addQuestionHint("Did you sleep well?", "Go to bed 1 hour earlier today!", true, true);
        addQuestionHint("Did you do any sports today?", "Jump around 20 times!", true, true);
        addQuestionHint("Did you eat any fruits today?", "Eat a big banana!", true, true);
        addQuestionHint("Did you receive love today?", "Invite your beloved person!", true, true);
        addQuestionHint("Did you see the sun today?", "#Bild der Sonne", true, true);
        addQuestionHint("Did you have fun today?", "#Witz", true, true);
        addQuestionHint("Have you been to cinema recently?", "#Kinoprogramm", true, true);
        addQuestionHint("Do you like Italian food?", "#Italienische Restaurants", false, false);
        addQuestionHint("Have you called your grandma today?", "Call your grandma, she will be happy!", true, true);


        /*
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
        */
    }

    public int getNumberOfQuestions(){
        return countRows(TABLE_QUESTIONS);
}

    public Question nextQuestion() {
        /*
        int rows = countRows(TABLE_QUESTIONS) + 1;
        int randomQuestionId = 1;
        if (rows > 1)
            randomQuestionId = ThreadLocalRandom.current().nextInt(1, rows);
        return getQuestion(randomQuestionId);
        */

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorQuest = db.query(TABLE_QUESTIONS, null, null,
                null, null, null, "ABS("+ KEY_ANSWER + ")", null);

        Question question = Question.getDefault();
        if (cursorQuest != null) {
            if (cursorQuest.getCount() > 0) {
                cursorQuest.moveToFirst();

                while (!cursorQuest.isLast()){
                    int q_id = cursorQuest.getInt(0);
                    String q_text = cursorQuest.getString(1);
                    int q_answer = cursorQuest.getInt(2);
                    boolean q_permanent = cursorQuest.getInt(3) == 1;
                    DisplayObject q_obj = getObject(q_id, TABLE_QUESTION_OBJECTS);
                    question = new Question(q_text, q_id, q_answer, q_obj);
                    question.setPermanent(q_permanent);
                    System.out.println(question.toString());
                    if ((!question.isPermanent() && question.getAnswer() == 0) || question.isPermanent())
                        break;
                    cursorQuest.moveToNext();
                }
                cursorQuest.close();

            }
        }

        db.close();

        return question;
    }

    public Hint nextHint() {

        SQLiteDatabase db = this.getReadableDatabase();

        String HINT_QUERY = "SELECT " + TABLE_HINTS + "." + KEY_HINT_ID + ", " +
                TABLE_HINTS + "." + KEY_TEXT + ", " +
                TABLE_QUESTIONS + "." + KEY_ANSWER + ", " +
                TABLE_RELEVANT_FOR + "." + KEY_INVERTED + ", " +
                TABLE_QUESTIONS + "." + KEY_QUESTION_ID + ", " +
                TABLE_QUESTIONS + "." + KEY_PERMA +
                " FROM (" + TABLE_QUESTIONS + " INNER JOIN " + TABLE_RELEVANT_FOR +
                " ON " + TABLE_QUESTIONS + "." + KEY_QUESTION_ID + " = " + TABLE_RELEVANT_FOR + "." + KEY_QUESTION_ID + ")"+
                " INNER JOIN " + TABLE_HINTS +
                " ON " + TABLE_RELEVANT_FOR + "." + KEY_HINT_ID + " = " + TABLE_HINTS + "." + KEY_HINT_ID +
                " WHERE " + TABLE_QUESTIONS + "." + KEY_PERMA + " = 1 OR " + TABLE_QUESTIONS + "." + KEY_ANSWER + " > 0" +
                " ORDER BY " + TABLE_QUESTIONS + "." + KEY_ANSWER + " DESC";

        //System.out.println(HINT_QUERY);

        Cursor cursor = db.rawQuery(HINT_QUERY, null);

        /*
        cursor.moveToFirst();
        while (true)
        {
            for (int i = 0; i < 6; i++)
                System.out.print(cursor.getString(i)+ ", ");
            System.out.print("\n");

            if (cursor.isLast())
                break;

            cursor.moveToNext();
        }
        */

        Hint hint = Hint.getDefault();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                System.out.println(cursor.getString(0) + cursor.getString(1));

                int highest_answer = cursor.getInt(2);
                ArrayList<Hint> hints = new ArrayList<>();
                ArrayList<Integer> questionIds = new ArrayList<>();

                while (cursor.getInt(2) == highest_answer)
                {
                    int h_id = cursor.getInt(0);
                    String h_text = cursor.getString(1);
                    DisplayObject h_obj = getObject(h_id, TABLE_HINT_OBJECTS);
                    hints.add(new Hint(h_text, h_id, h_obj));
                    questionIds.add(cursor.getInt(4));
                    cursor.moveToNext();
                    if (cursor.isAfterLast())
                        break;
                }

                int randomHintIndex = ThreadLocalRandom.current().nextInt(0, hints.size());
                //System.out.println("hint " + randomHintIndex);
                hint = hints.get(randomHintIndex);

                /*
                int h_id = cursor.getInt(0);
                String h_text = cursor.getString(1);
                int h_inv = cursor.getInt(3);
                int q_perma = cursor.getInt(5);
                DisplayObject h_obj = getObject(h_id, TABLE_HINT_OBJECTS);
                //System.out.println(h_obj);
                hint = new Hint(h_text, h_id, h_obj);
                */

                Question question = getQuestion(questionIds.get(randomHintIndex));

                answerQuestion(question, false);

            }
        }



        cursor.close();
        db.close();
        return hint;
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

        String INV_QUERY = "SELECT " + TABLE_HINTS + "." + KEY_HINT_ID + ", " + TABLE_RELEVANT_FOR + "." + KEY_INVERTED +
                " FROM " + TABLE_RELEVANT_FOR + " INNER JOIN " + TABLE_HINTS +
                " ON " + TABLE_RELEVANT_FOR + "." + KEY_HINT_ID + " = " + TABLE_HINTS + "." + KEY_HINT_ID;

        //System.out.println(INV_QUERY);

        Cursor cursor = db.rawQuery(INV_QUERY, null);
        cursor.moveToFirst();
        int h_inv = cursor.getInt(1);

        //System.out.println(question.toString() + " answer: " + answer + " h_inv: " + h_inv);


        ContentValues values = new ContentValues();
        values.put(KEY_ANSWER, question.getAnswer() + (answer ? 1 : -1) * h_inv);

        //System.out.println(values.get(KEY_ANSWER));

        // updating row
        int result = db.update(TABLE_QUESTIONS, values, KEY_QUESTION_ID + "=?",
                new String[] { String.valueOf(question.getId()) });
        db.close();
        return result;
    }



    private long add(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, question.getText());
        //values.put(KEY_ANSWER, question.isAnswer() ? 1 : 0);
        values.put(KEY_ANSWER, question.getAnswer());
        values.put(KEY_PERMA, question.isPermanent() ? 1 : 0);

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
                int q_answer = cursorQuest.getInt(2);
                DisplayObject q_obj = getObject(questionId, TABLE_QUESTION_OBJECTS);
                question = new Question(q_text, q_id, q_answer, q_obj);
                cursorQuest.close();
            }
        }

        db.close();

        return question;
    }

    public Question getQuestion(String questionText) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorQuest = db.query(TABLE_QUESTIONS, null, KEY_TEXT + " = ?",
                new String[] { String.valueOf(questionText) }, null, null, null, null);

        Question question = Question.getDefault();
        if (cursorQuest != null) {
            if (cursorQuest.getCount() > 0) {
                cursorQuest.moveToFirst();
                int q_id = cursorQuest.getInt(0);
                String q_text = cursorQuest.getString(1);
                int q_answer = cursorQuest.getInt(2);
                DisplayObject q_obj = getObject(q_id, TABLE_QUESTION_OBJECTS);
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