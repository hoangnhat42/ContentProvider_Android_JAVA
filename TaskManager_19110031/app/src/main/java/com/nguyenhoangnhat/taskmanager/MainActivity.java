package com.nguyenhoangnhat.taskmanager;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    TasksAdapter adapter;
    RecyclerView rv_list;
    Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnEdit = (ImageButton) findViewById(R.id.btnEdit);
        rv_list = (RecyclerView) findViewById(R.id.task_list);
        rv_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv_list.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getLoaderManager().initLoader(0, null, this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View getEmpIdView = li.inflate(R.layout.dialog_task_details, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                // set dialog_task_details.xml to alertdialog builder
                alertDialogBuilder.setView(getEmpIdView);

                final EditText titleInput = (EditText) getEmpIdView.findViewById(R.id.editTextDialogTitleInput);
                final EditText descriptionInput = (EditText) getEmpIdView.findViewById(R.id.editTextDialogDescriptionInput);
                // set dialog message

                alertDialogBuilder
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alertDialogBuilder.setCancelable(true);
                            }
                        })

                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                insertTask(titleInput.getText().toString(), descriptionInput.getText().toString());
                                restartLoader();

                            }
                        }).create()
                        .show();
            }
        });



    }


    private void insertTask(String title, String description) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TITLE, title);
        values.put(DBOpenHelper.DESCRIPTION, description);
        Uri taskUri = getContentResolver().insert(TasksProvider.CONTENT_URI, values);
        Toast.makeText(this, "Created Task " + title, Toast.LENGTH_LONG).show();
    }

    private void deleteAllTasks() {

        getContentResolver().delete(TasksProvider.CONTENT_URI, null, null);
        restartLoader();
        Toast.makeText(this, "All Tasks Deleted", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.deleteAllTasks:
                deleteAllTasks();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, TasksProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        List<Task> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(DBOpenHelper.TITLE);
            int index2 = cursor.getColumnIndex(DBOpenHelper.DESCRIPTION);


            String title = cursor.getString(index1);
            String description = cursor.getString(index2);

            Task task = new Task(title, description);
            list.add(task);
        }

        adapter = new TasksAdapter(this, list);
        rv_list.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
