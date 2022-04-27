package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;

public class myAccount extends AppCompatActivity {
    Button logout,btnTasks,btnDrafts;
    SharedPreferences prf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        logout = findViewById(R.id.btn_logout);
        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = prf.edit();
                editor.clear();
                editor.commit();
                startActivity(i);

            }
        });
        btnTasks = findViewById(R.id.btn_tasks);
        btnTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FinalPage.class);
                startActivity(intent);
            }
        });
        btnDrafts = findViewById(R.id.btn_Drafts);
        btnDrafts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TabLayout_Home.class);
                startActivity(intent);
            }
        });
    }
}