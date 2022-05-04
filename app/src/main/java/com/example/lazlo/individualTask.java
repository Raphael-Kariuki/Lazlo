package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class individualTask extends AppCompatActivity {
  TextInputEditText individualTaskTitle_TextInputEdit, individualTaskDescription_TextInputEdit,
          individualTaskBills_TextInputEdit,individualTaskDeadline_TextInputEdit;
  AutoCompleteTextView individualTaskCategory_TextInputEdit;
  AppCompatButton Btnsave, Btnshow;
  DBHelper dbHelper;
  long currentId;
  Cursor cursor;
  String selectedCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_task);
        individualTaskTitle_TextInputEdit = findViewById(R.id.individualTaskTitle_TextInputEdit);
        individualTaskDescription_TextInputEdit = findViewById(R.id.individualTaskDescription_TextInputEdit);
        individualTaskCategory_TextInputEdit = findViewById(R.id.individualTaskCategory_TextInputEdit);
        individualTaskBills_TextInputEdit = findViewById(R.id.individualTaskBills_TextInputEdit);
        individualTaskDeadline_TextInputEdit = findViewById(R.id.individualTaskDeadline_TextInputEdit);
        Btnsave = findViewById(R.id.Btnsave);
        Btnshow = findViewById(R.id.Btnshow);
        dbHelper = new DBHelper(this);

        //populate category dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.categories, android.R.layout.simple_dropdown_item_1line);
        individualTaskCategory_TextInputEdit.setAdapter(adapter);
        individualTaskCategory_TextInputEdit.setOnItemClickListener((adapterView, view, i, l) -> selectedCategory = (String) adapterView.getItemAtPosition(i));


        currentId = this.getIntent().getLongExtra("my_id_extra",-1);
        if (currentId < 0){
            //do something as invalid id passed
            finish();
        }else {
            try {
                showData();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        Btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    dbHelper.update(currentId,null,individualTaskTitle_TextInputEdit.getText().toString(),individualTaskDescription_TextInputEdit.getText().toString());
                    Toast.makeText(getApplicationContext(),"Task updated successfully", Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    System.out.println("Error inserting: " + e);
                    Toast.makeText(getApplicationContext(),"Task update failure", Toast.LENGTH_LONG).show();
                }


            }
        });

    }
    public void showData(){
        System.out.println("Populating...");
        try {
            cursor = dbHelper.getTaskById(currentId);
            System.out.println("Success conn to db...with id: " + currentId);
        }catch (Exception e){
            Toast.makeText(this,"Error " + e + "occurred", Toast.LENGTH_LONG).show();
        }
        System.out.println("done...");
        if (cursor.moveToFirst()){
            System.out.println("Setting text...");
            individualTaskTitle_TextInputEdit.setText(cursor.getString(cursor.getColumnIndexOrThrow("TaskTitle")));
            System.out.println("TaskTitle" + cursor.getString(cursor.getColumnIndexOrThrow("TaskTitle")));
            individualTaskDescription_TextInputEdit.setText(cursor.getString(cursor.getColumnIndexOrThrow("TaskDescription")));
            System.out.println("TaskDescription" + cursor.getString(cursor.getColumnIndexOrThrow("TaskDescription")));
        }
        cursor.close();


    }

    }
