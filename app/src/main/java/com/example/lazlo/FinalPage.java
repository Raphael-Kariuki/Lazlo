package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

/*added code*/
import android.content.Intent;
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
    TextView hamburger_menu;
    ListView tasks_listView;
    Button btn_addTasks;
    String s2;
    @Override
    public void onBackPressed(){
        FinalPage.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);
        // text = findViewById(R.id.changeText);
        Intent intent = getIntent();
        s2 = intent.getStringExtra("email");
        //text.setText(s2); */


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
        ArrayList<HashMap<String, String>> usersList = db.getUserData();
        tasks_listView = (ListView) findViewById(R.id.task_listView);
        ListAdapter adapter = new SimpleAdapter(this,usersList,R.layout.userdata_listrow,new String[]{"username","email","password"}, new int[]{R.id.user_name,R.id.user_email,R.id.user_password});
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