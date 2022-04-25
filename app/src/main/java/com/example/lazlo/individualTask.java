package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.lazlo.Sql.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class individualTask extends AppCompatActivity {
  EditText Ttitle, Tdescription;
  Button Btnsave, Btnshow;
  DBHelper dbHelper;
  long currentId
;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_task);
        Ttitle = this.findViewById(R.id.Ttitle);
        Tdescription = this.findViewById(R.id.Tdescription);
        Btnsave = this.findViewById(R.id.Btnsave);
        Btnshow = this.findViewById(R.id.Btnshow);
        dbHelper = new DBHelper(this);
        currentId = this.getIntent().getLongExtra("my_id_extra",-1);
        if (currentId < 0){
            //do something as invalid id passed
            finish();
        }
        showData();
        Btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.update(currentId,null,Ttitle.getText().toString(),Tdescription.getText().toString());
            }
        });

    }
    private void showData(){
        Cursor cursor = dbHelper.getTaskById(currentId);
        if (cursor.moveToFirst()){
            Ttitle.setText(cursor.getString(cursor.getColumnIndexOrThrow("TaskTitle")));
            Tdescription.setText(cursor.getString(cursor.getColumnIndexOrThrow("TaskDescription")));
        }
        cursor.close();
    }

    }
