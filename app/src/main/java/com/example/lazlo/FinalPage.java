package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

/*added code*/
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.lazlo.Sql.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class FinalPage extends AppCompatActivity {
    TextView hamburger_menu,uname;
    ListView tasks_listView;
    Button btn_addTasks;
    String s2;
    SharedPreferences session_prefs;
    @Override
    public void onBackPressed(){
        FinalPage.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);

       session_prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        /* if (session_prefs.contains("username") && session_prefs.contains("password")){
        }else {
            setContentView(R.layout.activity_main);
        }*/
        // text = findViewById(R.id.changeText);
        s2 = session_prefs.getString("username",null);
        uname = (TextView) findViewById(R.id.uname_view);
        uname.setText(s2);

        //method to populate list

        populateLandingPageListView();

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

    private void populateLandingPageListView() {
        DBHelper db = new DBHelper(this);
        ArrayList<HashMap<String, String>> taskList = db.getTasks(s2);
        tasks_listView = (ListView) findViewById(R.id.task_listView);
        ListAdapter adapter = new SimpleAdapter(this,taskList,R.layout.userdata_listrow,new String[]{"task_title","task_description"}, new int[]{R.id.taskTitle,R.id.taskDescription});
        tasks_listView.setAdapter(adapter);
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