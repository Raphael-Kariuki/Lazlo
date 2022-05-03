package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lazlo.Sql.DBHelper;

public class Drafts extends AppCompatActivity {
    ListView draftsListView;
    Cursor cursor;
    String s2;
    SimpleCursorAdapter simpleCursorAdapter;
    DBHelper dbHelper;
    SharedPreferences drafts_sprefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);
        dbHelper = new DBHelper(this);
        draftsListView = findViewById(R.id.draftTask_listView);
        drafts_sprefs = getSharedPreferences("user_details", MODE_PRIVATE);
        s2 = drafts_sprefs.getString("username",null);
        cursor = dbHelper.getAllDrafts(s2);
        SetOrRefreshListView();

    }
    public void SetOrRefreshListView(){
        cursor = dbHelper.getAllDrafts(s2);
        taskListPopulate();

    }
    private void taskListPopulate(){
        if (simpleCursorAdapter == null){
            simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.userdata_listrow,cursor,new String[]{"TaskTitle","TaskDescription","TaskAssociatedPrice"},new int[]{R.id.taskTitle,R.id.taskDescription,R.id.TaskAssociatedPrice},0);
            draftsListView.setAdapter(simpleCursorAdapter);
            draftsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(),individualTask.class);
                    intent.putExtra("my_id_extra", l);
                    startActivity(intent);
                }
            });
            draftsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    dbHelper.deleteDraftTask(l);
                    SetOrRefreshListView();
                    return true;
                }
            });
        }else {
            simpleCursorAdapter.swapCursor(cursor);
        }
    }
}