package com.javadude.todostarter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

public class TodoListActivity extends AppCompatActivity {
    // define an id for the loader we'll use to manage a cursor and stick its data in the list
    private static final int TODO_LOADER = 1;

    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        assert recyclerView != null;
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TodoAdapter(this,
                new TodoAdapter.OnItemClickedListener() {
                    @Override public void onItemClicked(long id) {
                        // start activity to edit the item
                        // we're creating a new item; just pass -1 as the id
                        Intent intent = new Intent(TodoListActivity.this, EditActivity.class);
                        intent.putExtra("itemId", id);
                        startActivity(intent);
                    }});
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start activity to edit the item
                // we're creating a new item; just pass -1 as the id
                Intent intent = new Intent(TodoListActivity.this, EditActivity.class);
                intent.putExtra("itemId", -1L);
                startActivity(intent);
            }
        });

        // start asynchronous loading of the cursor
        getSupportLoaderManager().initLoader(TODO_LOADER, null, loaderCallbacks);
    }

    // define a loader manager that will asynchronously retrieve data and when finished,
    //   update the list's adapter with the changes
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        // when the loader is created, setup the projection to retrieve from the database
        //   and create a cursorloader to request a cursor from a content provider (by URI)
        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
            String[] projection = {
                    TodoProvider.ID,
                    TodoProvider.NAME,
                    TodoProvider.DESCRIPTION,
                    TodoProvider.PRIORITY
            };
            return new CursorLoader(
                    TodoListActivity.this,
                    TodoProvider.CONTENT_URI, // note: this will register for changes
                    projection,
                    null, null, // groupby, having
                    TodoProvider.NAME + " asc");
        }

        // when the data has been loaded from the content provider, update the list adapter
        //   with the new cursor
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            adapter.swapCursor(cursor); // set the data
        }

        // if the loader has been reset, kill the cursor in the adapter to remove the data from the list
        @Override
        public void onLoaderReset(Loader<Cursor> cursor) {
            adapter.swapCursor(null); // clear the data
        }
    };

}
