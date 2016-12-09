package com.javadude.todostarter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;

import java.util.List;


public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager notifyManager;

    static String ACTION_ALARM_RECEIVER = "alarm_receiver_action";
    static int id1 = 001;
    static int id2 = 002;

    @Override
    public void onReceive(Context context, Intent intent) {

        int updateCount = Util.pendingToDue(context);

        if( notifyManager == null ) {
            notifyManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        }
        if( updateCount > 0 ) {
            broadcast(context);
            notification(context);
        }

    }

    public void broadcast(Context context) {
        Intent intent = new Intent("com.javadude.count");
        context.sendBroadcast(intent);
    }



    private void notification(Context context) {
        List<TodoItem> todoItemList = Util.getItemOnStatus(context, Util.DUE);
        customNotification(context, todoItemList);
    }




    private void customNotification(Context context, List<TodoItem> todoItemList) {
        NotificationCompat.InboxStyle details = new NotificationCompat.InboxStyle()
                .setBigContentTitle("Top 5 items due");
         for(int ic=0; ic<todoItemList.size() && ic < 5; ic++ ) {
            details.addLine( todoItemList.get(ic).name.get());
        }

        int id = todoItemList.size();
        PendingIntent mainAction = createMainPending(context, 15, "todolist", Constants.MAIN_ACTION);
        PendingIntent doneAction = createPending(context, 25, "Done", Constants.DONE_ACTION);
        PendingIntent snoozeAction = createPending(context, 35, "Snooze", Constants.SNOOZE_ACTION);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setColor(Color.RED)
                .setStyle(details)
                .setNumber(23)
                .setOngoing(true)
                .setContentTitle(" Click to go to the App " + id)
                .setContentIntent(mainAction)
                .addAction(R.drawable.ic_notifications_active_black_24dp, "Done", doneAction)
                .addAction(R.drawable.ic_notifications_paused_black_24dp, "Snooze", snoozeAction)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notifyManager.notify(id1, notification);
    }

    private PendingIntent createMainPending(Context context, int id, String info, String actionName) {
        Intent intent = new Intent(context, TodoListActivity.class);
        intent.setAction(actionName);
        intent.putExtra("actionInfo", info);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(TodoListActivity.class);
        taskStackBuilder.addNextIntent(intent);
        return taskStackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private PendingIntent createPending(Context context, int id, String info, String actionName) {

        Intent intent = new Intent();
        intent.setAction(actionName);

        if(info.equals("Done")){
            Util.doneItem(context);
        }
        else{
            Util.snoozeItem(context);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }



}


