package com.javadude.todostarter;

import android.databinding.ObservableArrayList;

/**
 * Created by ranji on 12/2/2016.
 */

public abstract class Model {

    public final ObservableArrayList<TodoItem> todoItems = new ObservableArrayList<>();
    public abstract void itemSelected(TodoItem item, int position);
}
