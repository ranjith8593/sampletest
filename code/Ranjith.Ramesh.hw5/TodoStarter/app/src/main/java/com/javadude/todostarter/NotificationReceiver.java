package com.javadude.todostarter;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by ranji on 12/2/2016.
 */


public class NotificationReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        TodoObserver todoObserver = new TodoObserver(new Handler(), context);
        final ContentResolver contentResolver = context.getContentResolver();
        contentResolver.registerContentObserver(TodoProvider.CONTENT_URI, true, todoObserver);

        String action = intent.getAction();
        if (Constants.DONE_ACTION.equals(action)) {
            Util.doneItem(context);

        }

        if (Constants.SNOOZE_ACTION.equals(action)) {
            Util.snoozeItem(context);
        }


        contentResolver.unregisterContentObserver(todoObserver);



    }

}

