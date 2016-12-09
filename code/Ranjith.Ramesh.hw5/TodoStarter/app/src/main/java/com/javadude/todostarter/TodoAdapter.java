package com.javadude.todostarter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TodoAdapter extends CursorRecyclerViewAdapter<TodoAdapter.ViewHolder> {
    private Set<Integer> selectedRows = new HashSet<>();
    private Model model ;


    public interface OnItemClickedListener {
        void onItemClicked(long id);
    }
    private OnItemClickedListener onItemClickedListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public ViewHolder (View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }
        public ViewDataBinding getBinding() {
            return binding;
        }

        public TextView nameView;
        public TextView descriptionView;
        public TextView priorityView;
        public long id;

    }

    public TodoAdapter(Context context, OnItemClickedListener onItemClickedListener) {
        super(context, null);
        this.onItemClickedListener = onItemClickedListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        LayoutInflater inflator = LayoutInflater.from(parent.getContext());
        View view = inflator.inflate(R.layout.todo_card, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, Cursor cursor) {


        if(cursor != null){

            TodoItem todoItem = Util.todoItemFromCursor(cursor);
            holder.getBinding().setVariable(com.javadude.todostarter.BR.todoItem, todoItem);
            holder.getBinding().setVariable(com.javadude.todostarter.BR.model, model);
            holder.getBinding().setVariable(com.javadude.todostarter.BR.position, position);
            holder.getBinding().executePendingBindings();
            model.todoItems.add(todoItem);


        }

    }

    public Cursor swapCursor(Cursor newCursor) {
        selectedRows.clear();

        if(model == null){

            model = new Model() {
                @Override
                public void itemSelected(TodoItem item, int position) {

                    onItemClickedListener.onItemClicked(item.id.get());

                }
            };


        }

        return super.swapCursor(newCursor);
    }
}