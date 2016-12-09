package com.javadude.todostarter;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;

public class TodoItem {
   /* private long id;
    private String name;
    private String description;
    private int priority;*/

    public final ObservableLong id = new ObservableLong();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableInt priority = new ObservableInt();

    public final ObservableLong dueTime = new ObservableLong();
    public final ObservableField<String> changedDueTime = new ObservableField<>();
    public final ObservableField<String> status = new ObservableField<>();



    public TodoItem() {
    }
    public TodoItem(long id, String name, String description, int priority) {
        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
        this.priority.set(priority);


    }


    public TodoItem(long id, String name, String description, int priority, long dueTime, String status){

        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
        this.priority.set(priority);
        this.dueTime.set(dueTime);
        this.status.set(status);
        this.changedDueTime.set(Util.MStoDate(dueTime));


    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "name='" + name.get() + '\'' +
                ", id=" + id.get() +
                ", description=" + description.get() +
                ", priority=" + priority.get() +
                '}';
    }



}
