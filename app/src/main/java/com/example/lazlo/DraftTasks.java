package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lazlo.Sql.DBHelper;

public class DraftTasks extends AppCompatActivity {
    ListView draftsListView;
    Cursor cursor;
    Double randUserId;
    SimpleCursorAdapter simpleCursorAdapter;
    DBHelper dbHelper;
    SharedPreferences drafts_sharedPrefs;

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), Account.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_tasks);
        dbHelper = new DBHelper(this);
        draftsListView = findViewById(R.id.draftTask_listView);
        drafts_sharedPrefs = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(drafts_sharedPrefs.getString("randomUserId",null));

        cursor = dbHelper.getAllDrafts(randUserId);
        SetOrRefreshListView();

    }
    public void SetOrRefreshListView(){
        cursor = dbHelper.getAllDrafts(randUserId);
        taskListPopulate();

    }
    private void taskListPopulate(){
        if (simpleCursorAdapter == null){
            simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.userdata_listrow,cursor,new String[]{"TaskTitle","TaskDescription","TaskAssociatedPrice"},new int[]{R.id.taskTitle,R.id.taskDescription,R.id.TaskAssociatedPrice},0);
            draftsListView.setAdapter(simpleCursorAdapter);
            draftsListView.setOnItemClickListener((adapterView, view, i, l) -> {
                Intent intent = new Intent(getApplicationContext(),individualTask.class);
                intent.putExtra("my_id_extra", l);
                startActivity(intent);
            });
            draftsListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                boolean b = false;
                try {
                    b = dbHelper.deleteDraftTask(l);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (b){
                    SetOrRefreshListView();
                }
                return true;
            });
        }else {
            simpleCursorAdapter.swapCursor(cursor);
        }
    }
}