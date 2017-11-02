package com.lukaspaczos.emergencynumber.ui.messages.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lukaspaczos.emergencynumber.App;
import com.lukaspaczos.emergencynumber.ui.messages.data.MessageModel;
import com.lukaspaczos.emergencynumber.ui.report.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukas Paczos on 29-Mar-17
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private static final String DATABASE_NAME = "112.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_TABLE = "messages";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_SENDER = "sender";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_REPORT = "report";
    private static final String COLUMN_FIRST_MESSAGE = "first_message";
    private static final String COLUMN_SENT = "sent";
    private static final String COLUMN_DELIVERED = "delivered";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private SQLiteDatabase database;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table if not exists "
            + DATABASE_TABLE + "( " + COLUMN_TIMESTAMP
            + " integer primary key, " + COLUMN_SENDER
            + " text not null, " + COLUMN_MESSAGE + " text not null, " + COLUMN_REPORT + " text, "
            + COLUMN_FIRST_MESSAGE + " integer, " + COLUMN_SENT + " integer, " +
            COLUMN_DELIVERED + " integer);";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper(App.getContext());
        }

        return instance;
    }

    public void initialize() {
        if (database == null || !database.isOpen())
            database = getWritableDatabase();
    }

    public void uninitialize() {
        database.close();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    /**
     * Inserting values to database
     * @param text message text
     * @param report whole report object
     * @param firstMessage 1 if this message starts new report, 0 otherwise
     * @return true if inserted into database, false otherwise
     */
    public boolean insertMyMessage(String text, Report report, int firstMessage) {
        String jsonReport = gson.toJson(report);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TIMESTAMP, System.currentTimeMillis());
        contentValues.put(COLUMN_SENDER, "me");
        contentValues.put(COLUMN_MESSAGE, text);
        contentValues.put(COLUMN_REPORT, jsonReport);
        contentValues.put(COLUMN_FIRST_MESSAGE, firstMessage);
        return database.insert(DATABASE_TABLE, null, contentValues) != -1;
    }

    public boolean insert112Message(String text) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TIMESTAMP, System.currentTimeMillis());
        contentValues.put(COLUMN_SENDER, "112");
        contentValues.put(COLUMN_MESSAGE, text);
        return database.insert(DATABASE_TABLE, null, contentValues) != -1;
    }

    public List<MessageModel> getAllMessages() {
        List<MessageModel> messages = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            messages.add(new MessageModel(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    gson.fromJson(cursor.getString(3), Report.class),
                    cursor.getInt(4) == 1,
                    cursor.getInt(5),
                    cursor.getInt(6)
                    ));
            cursor.moveToNext();
        }
        cursor.close();
        return messages;
    }

    public boolean removeEntry(long timestampStart, long timestampEnd) {
        return database.delete(DATABASE_TABLE, COLUMN_TIMESTAMP + " >= ? AND " + COLUMN_TIMESTAMP + "< ?", new String[]{String.valueOf(timestampStart), String.valueOf(timestampEnd)}) == 1;
    }
}
