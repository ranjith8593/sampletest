package com.javadude.todostarter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.net.Uri;
import android.util.Log;

public class Util {


    static String SNOOZE = "snooze";
    static String DONE = "done";
    static String DUE = "due";
    static String PENDING = "pending";

    public static TodoItem findTodo(Context context, long id) {

        Uri uri = Uri.withAppendedPath(TodoProvider.CONTENT_URI, "" + id);

        String[] projection = {
                TodoProvider.ID,
                TodoProvider.NAME,
                TodoProvider.DESCRIPTION,
                TodoProvider.PRIORITY,
                TodoProvider.DUETIME,
                TodoProvider.STATUS
        };

        Cursor cursor = null;
        try {

            cursor = context.getContentResolver().
                    query(uri, projection, null, null, null);


            if (cursor == null || !cursor.moveToFirst())
                return null;


            return todoItemFromCursor(cursor);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }


    public static void updateTodo(Context context, TodoItem todo , String type) {

        ContentValues values = new ContentValues();
        values.put(TodoProvider.NAME, todo.name.get());
        values.put(TodoProvider.DESCRIPTION, todo.description.get());
        values.put(TodoProvider.PRIORITY, todo.priority.get());
        if (todo.id.get() == -1) {

            values.put(TodoProvider.DUETIME , System.currentTimeMillis()+10000);

            if(type.equals(DONE)){

                values.put(TodoProvider.STATUS,DONE);
            }
            else{

                values.put(TodoProvider.STATUS,PENDING);
            }

            Uri uri = TodoProvider.CONTENT_URI;
            Uri insertedUri = context.getContentResolver().insert(uri, values);
            String idString = insertedUri.getLastPathSegment();
            long id = Long.parseLong(idString);
            todo.id.set(id);

        } else {

            if(type.equals(SNOOZE)){

                values.put(TodoProvider.DUETIME, System.currentTimeMillis()+10000);
                values.put(TodoProvider.STATUS, PENDING);
            }

            if(type.equals(DONE)){
                values.put(TodoProvider.STATUS,DONE);

            }

            Uri uri = Uri.withAppendedPath(TodoProvider.CONTENT_URI, "" + todo.id.get());
            context.getContentResolver().update(uri, values, TodoProvider.ID + "=" + todo.id.get(), null);
        }
    }


    public static List<TodoItem> getItemOnStatus(Context context, String status) {

        List<TodoItem> todoItemList = new ArrayList();

        String[] projection = {
                TodoProvider.ID,
                TodoProvider.NAME,
                TodoProvider.DESCRIPTION,
                TodoProvider.PRIORITY,
                TodoProvider.DUETIME,
                TodoProvider.STATUS
        };


        Cursor cursor = null;
        try {

            cursor = context.getContentResolver().
                    query(TodoProvider.CONTENT_URI,
                            projection,

                            TodoProvider.STATUS + "='"+status+"'",
                            null,
                            TodoProvider.DUETIME + " asc");


            if (cursor == null || !cursor.moveToFirst())
                return todoItemList;

            do {
                todoItemList.add(todoItemFromCursor(cursor) );
            } while (cursor.moveToNext());

            return todoItemList;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }


    public static int pendingToDue (Context context) {

        ContentValues values = new ContentValues();
        values.put(TodoProvider.STATUS, DUE );
        Uri uri = TodoProvider.CONTENT_URI;
        int count = context.getContentResolver().update(uri, values, TodoProvider.STATUS + " = '" + PENDING + "' and " +
                TodoProvider.DUETIME + " <=  " + System.currentTimeMillis()  , null);
        return  count ;

    }


    public static TodoItem todoItemFromCursor(Cursor cursor) {

        return new TodoItem(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3),  cursor.getLong(4), cursor.getString(5));

    }


    public static int snoozeItem(Context context) {

        ContentValues values = new ContentValues();
        values.put(TodoProvider.DUETIME, System.currentTimeMillis() + 10000 );
        values.put(TodoProvider.STATUS, PENDING );
        Uri uri = TodoProvider.CONTENT_URI;
        int count = context.getContentResolver().update(uri, values, TodoProvider.STATUS + " = '" + DUE + "'" , null);
        return  count;
    }


    public static int doneItem(Context context) {
        ContentValues values = new ContentValues();
        values.put(TodoProvider.STATUS, DONE );
        Uri uri = TodoProvider.CONTENT_URI;
        int count = context.getContentResolver().update(uri, values, TodoProvider.STATUS + " = '" + DUE + "'" , null);
        return  count;
    }


    public static String MStoDate(long mseconds){

        String format = "dd-MM-yyyy hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mseconds);
        return simpleDateFormat.format(calendar.getTime());

    }
}
