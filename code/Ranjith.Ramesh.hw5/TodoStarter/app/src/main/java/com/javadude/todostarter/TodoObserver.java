package com.javadude.todostarter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class TodoObserver extends ContentObserver {


    static int alarmId = 2;

    private Context context;

    private AlarmManager alarmManager;

    public TodoObserver(Handler handler, Context context) {

        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange,null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        broadcast();

        List<TodoItem> todoItemList = new ArrayList<TodoItem>();
        List<TodoItem> todoDueItemList = Util.getItemOnStatus(context, Util.DUE);
        if( todoDueItemList != null && todoDueItemList.size() > 0 )
            todoItemList.addAll(todoDueItemList);
        List<TodoItem> todoPendingItemList = Util.getItemOnStatus(context, Util.PENDING);
        if( todoPendingItemList != null && todoPendingItemList.size() > 0 )
            todoItemList.addAll(todoPendingItemList);

        if (todoItemList.size() > 0 ) {

            if( !existAlarm() ) {

                if (alarmManager == null) {

                    alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                }

                setAlarm(todoItemList);
            } else {

            }
        }
        else {
            if( alarmManager == null )  {
                alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            }
            cancelAlarm();
        }

    }


    public void setAlarm(List<TodoItem> todoItemList) {
        if (todoItemList == null || todoItemList.size() == 0)
            return;
        long duetime = todoItemList.get(0).dueTime.get();
        long diff = duetime - System.currentTimeMillis();
        if( diff < 0 ) {
            diff = 0;
        }

        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
         alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
               SystemClock.elapsedRealtime() + diff, pendingIntent);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

        try {
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {

        }

    }


    public void broadcast() {
        Intent intent = new Intent("com.javadude.count");
        context.sendBroadcast(intent);
    }


    public boolean existAlarm() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Intent.ACTION_VIEW);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_NO_CREATE);
        if( pendingIntent != null )
            return true;
        return false;
    }


}
