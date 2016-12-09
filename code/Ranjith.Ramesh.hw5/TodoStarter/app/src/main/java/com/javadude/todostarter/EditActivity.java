package com.javadude.todostarter;

import android.databinding.DataBindingUtil;
import android.databinding.tool.DataBinder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.javadude.todostarter.databinding.ActivityEditBinding;

public class EditActivity extends AppCompatActivity {

    private ActivityEditBinding binding;

    private TodoItem todoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        long itemId = getIntent().getLongExtra("itemId", -1L);


        if (itemId != -1) {
            this.todoItem = Util.findTodo(this, itemId);


        } else {
            this.todoItem = new TodoItem();
            this.todoItem.id.set(-1L);
        }

        binding.editLayout.setItem(todoItem);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {

            case R.id.done:

                Util.updateTodo(this, this.todoItem,"save");
                finish();
                return true;

            case R.id.cancel:

                finish();
                return true;
            case R.id.snooz:
                Util.updateTodo(this,this.todoItem,Util.SNOOZE);
                finish();
                return true;

            case R.id.finish:
                Util.updateTodo(this,this.todoItem,Util.DONE);

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
