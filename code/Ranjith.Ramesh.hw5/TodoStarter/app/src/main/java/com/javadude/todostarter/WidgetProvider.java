package com.javadude.todostarter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

public class WidgetProvider extends AppWidgetProvider {


    public static final String SNOOZE_CLICK = "com.javadude.todostarter.ACTION_ON_ImG_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i], -1);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SNOOZE_CLICK)) {

            if (Util.getItemOnStatus(context, Util.DUE).size() > 0) {
                int count = Util.snoozeItem(context);

            } else {

            }
        }
        else{
            if ("com.javadude.count".equals(intent.getAction())) {
                int count = intent.getIntExtra("count", -1);
                ComponentName componentName = new ComponentName(context, WidgetProvider.class);
                AppWidgetManager instance = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = instance.getAppWidgetIds(componentName);
                for(int i = 0; i < appWidgetIds.length; i++) {
                    updateAppWidget(context, instance, appWidgetIds[i], count);
                }
            } else {
                super.onReceive(context, intent);
            }

        }
    }


    TodoObserver todoObserver;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        final ContentResolver contentResolver = context.getContentResolver();
        if (todoObserver == null) {
             todoObserver = new TodoObserver(new Handler(), context);
        }
        contentResolver.registerContentObserver(TodoProvider.CONTENT_URI, true, todoObserver);

    }

    @Override
    public void onDisabled(Context context) {


        final ContentResolver contentResolver = context.getContentResolver();
        contentResolver.unregisterContentObserver(todoObserver);
        if (todoObserver != null) {
            todoObserver = null;
        }


        super.onDisabled(context);
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int count) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        List<TodoItem> todoItemList = Util.getItemOnStatus(context, Util.DUE);

        count = todoItemList.size();

        String quantityString = context.getResources().getQuantityString(R.plurals.dueitem, count , count);
        remoteViews.setTextViewText(R.id.text,  quantityString);

        int color = Color.BLUE;

        remoteViews.removeAllViews(R.id.grid);
        RemoteViews block = new RemoteViews(context.getPackageName(), R.layout.block);
        block.setInt(R.id.addblock,"setBackgroundColor", color);
        remoteViews.addView(R.id.grid, block);

        Intent intentForEditActivity = new Intent(context, EditActivity.class);
        PendingIntent pIntentEditActivity = PendingIntent.getActivity(context, 0, intentForEditActivity, 0);
        block.setOnClickPendingIntent(R.id.addblock, pIntentEditActivity);
        appWidgetManager.updateAppWidget(appWidgetId, block);


        Intent intentForDuetime = new Intent(context, WidgetProvider.class);
        intentForDuetime.setAction(SNOOZE_CLICK);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, intentForDuetime, 0);
        remoteViews.setOnClickPendingIntent(R.id.snoozedue, actionPendingIntent);

        Intent intent = new Intent(context, TodoListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

}