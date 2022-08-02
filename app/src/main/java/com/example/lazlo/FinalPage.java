package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

/*added code*/
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class FinalPage extends AppCompatActivity {
    ListView tasks_listView,task1;
    FloatingActionButton btn_addTasks,hamburger_menu;
    String s2;
    SharedPreferences session_prefs;
    DBHelper dbHelper;
    Cursor cursor;
    SimpleCursorAdapter simpleCursorAdapter;
    AppCompatTextView uname;
    AppCompatButton home, school,work,business,shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);



        tasks_listView = this.findViewById(R.id.task_listView);

        dbHelper = new DBHelper(this);
        SetOrRefreshListView();


        session_prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        s2 = session_prefs.getString("username",null);
        uname = findViewById(R.id.userName);
        uname.setText(s2);


        //method to populate list

        //populateTaskListView();

        // click to add task
        addNewTasks();


        //hamburger menu action

        hamburger_menu = findViewById(R.id.hamburger_menu);
        hamburger_menu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), myAccount.class);
            startActivity(intent);
        });
        home = findViewById(R.id.homeTask);
        work = findViewById(R.id.workTasks);
        school =findViewById(R.id.schoolTasks);
        business = findViewById(R.id.businessTasks);
        shopping = findViewById(R.id.shoppingTasks);



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateHomeTasks();
            }
        });
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateHomeShopping();
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateWorkTasks();
            }
        });
        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateSchoolTasks();
            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateBusinessTasks();
            }
        });


    }

    public void SetOrRefreshListView(){
        cursor = dbHelper.getAll(s2);
        taskListPopulate();

    }
    @Override
    protected void onResume(){
        super.onResume();
        SetOrRefreshListView();
        //refresh listview when returning to the activity
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        cursor.close();
        //clean up
    }




    private void addNewTasks(){
        btn_addTasks =  findViewById(R.id.btn_addTasks);
        btn_addTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddTasks.class);
                intent.putExtra("username", s2);
                startActivity(intent);
            }
        });
    }
    private  void populateHomeTasks(){
        cursor = dbHelper.getAllByCategories(s2,"Home");
        taskListPopulate();

    }
    private  void populateHomeShopping(){
        cursor = dbHelper.getAllByCategories(s2,"Shopping");
        taskListPopulate();

    }
    private  void populateWorkTasks(){
        cursor = dbHelper.getAllByCategories(s2,"Work");
        taskListPopulate();

    }
    private  void populateSchoolTasks(){
        cursor = dbHelper.getAllByCategories(s2,"School");
        taskListPopulate();

    }
    private  void populateBusinessTasks(){
        cursor = dbHelper.getAllByCategories(s2,"Business");
        taskListPopulate();

    }
    private void taskListPopulate(){
        if (simpleCursorAdapter == null){
            simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.userdata_listrow,cursor,new String[]{"TaskTitle","TaskDescription","TaskAssociatedPrice"},new int[]{R.id.taskTitle,R.id.taskDescription,R.id.TaskAssociatedPrice},0);
            tasks_listView.setAdapter(simpleCursorAdapter);
            tasks_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(),individualTask.class);
                    intent.putExtra("my_id_extra", l);
                    startActivity(intent);
                }
            });
            tasks_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(FinalPage.this);
                    builder.setCancelable(true);
                    builder.setTitle("Delete task?");
                    //TODO: get task title to aid user be sure of decision
                    builder.setMessage("Are you sure you want to delete?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbHelper.deleteTask(l);
                            Toast.makeText(FinalPage.this, "task delete successfully", Toast.LENGTH_SHORT).show();
                            SetOrRefreshListView();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();


                    return true;
                }
            });
        }else {
            simpleCursorAdapter.swapCursor(cursor);
        }
    }


}