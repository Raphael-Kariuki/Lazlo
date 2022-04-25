package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

/*added code*/
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lazlo.Sql.DBHelper;

import java.util.Locale;


public class FinalPage extends AppCompatActivity {
    TextView hamburger_menu,uname;
    ListView tasks_listView,task1;
    Button btn_addTasks;
    String s2;
    SharedPreferences session_prefs;

    DBHelper dbHelper;
    Cursor cursor;
    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);

        tasks_listView = this.findViewById(R.id.task_listView);
        dbHelper = new DBHelper(this);
        SetOrRefreshListView();













        session_prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        s2 = session_prefs.getString("username",null);
        uname = (TextView) findViewById(R.id.uname_view);
        uname.setText(s2);


        //method to populate list

        //populateTaskListView();

        // click to add task
        addNewTasks();


        //hamburger menu action

        hamburger_menu = (TextView) findViewById(R.id.hamburger_menu);
        hamburger_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), myAccount.class);
                startActivity(intent);
            }
        });

    }
    private void SetOrRefreshListView(){
        cursor = dbHelper.getAll();
        if (simpleCursorAdapter == null){
            simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,cursor,new String[]{"TaskTitle","TaskDescription"},new int[]{android.R.id.text1,android.R.id.text2},0);
            tasks_listView.setAdapter(simpleCursorAdapter);
            tasks_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(),individualTask.class);
                    intent.putExtra("my_id_extra", l);
                    startActivity(intent);
                }
            });
        }

    }
   /*private void populateLandingPageListView() {
        DBHelper db = new DBHelper(this);
        ArrayList<HashMap<String, String>> taskList = db.getTasks(s2);
        tasks_listView = (ListView) findViewById(R.id.task_listView);
        ListAdapter adapter = new SimpleAdapter(this,taskList,R.layout.userdata_listrow,new String[]{"task_title","task_description"}, new int[]{R.id.taskTitle,R.id.taskDescription});
        tasks_listView.setAdapter(adapter);
    }*/
    private void populateTaskListView(){
        DBHelper db = new DBHelper(this);
        Cursor taskCursor = db.getAll();
        class TasksCursorAdapter extends CursorAdapter{

            public TasksCursorAdapter(Context context, Cursor cursor){
                super(context, cursor, 0);
            }
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent){
                return LayoutInflater.from(context).inflate(R.layout.userdata_listrow, parent, false);
            }
            @Override
            public void bindView(View view, Context context, Cursor cursor){

                String title = cursor.getString(cursor.getColumnIndexOrThrow("TaskTitle")).toUpperCase(Locale.ROOT);
                //String Description = cursor.getString(cursor.getColumnIndexOrThrow("TaskDescription"));

                TextView taskTitle;
                taskTitle = (TextView) view.findViewById(R.id.taskTitle);
                //TextView taskDescription = (TextView) view.findViewById(R.id.taskDescription);

                taskTitle.setText(title);
                //taskDescription.setText(Description);
            }
        }
        TasksCursorAdapter tasksCursorAdapter = new TasksCursorAdapter(this, taskCursor);
        tasks_listView = (ListView) findViewById(R.id.task_listView);
        tasks_listView.setAdapter(tasksCursorAdapter);
        tasks_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dbHelper.deleteTask(l);
                populateTaskListView();
            }
        });
    }

    private void addNewTasks(){
        btn_addTasks = (Button) findViewById(R.id.btn_addTasks);
        btn_addTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddTasks.class);
                intent.putExtra("email", s2);
                startActivity(intent);
            }
        });
    }

}