package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textfield.TextInputEditText;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddTasks extends AppCompatActivity {
    TextInputEditText task_title,task_description,priceAutocompleteView;
    AutoCompleteTextView select_date;
    DatePickerDialog datePickerDialog;
    ImageButton btn_saveTasks, btn_cancelTaskCreation;
    DBHelper dbHelper;
    SharedPreferences tasks_sharedPrefs;
    LocalDate selected_date,date_now;
    AutoCompleteTextView tasksCategories;
    String selected_category;
    Double Price;

//method to parse date input from adding task

    public static LocalDate getDateFromString(String string,DateTimeFormatter dateTimeFormatter){
        return LocalDate.parse(string, dateTimeFormatter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        task_title = (TextInputEditText) findViewById(R.id.taskTitleAutoCompleteView);
        task_description = (TextInputEditText) findViewById(R.id.taskDescriptionAutoCompleteView);
        select_date = (AutoCompleteTextView) findViewById(R.id.selectDate_AutocompleteView);
        priceAutocompleteView = (TextInputEditText) findViewById(R.id.priceAutoCompleteView);
        tasksCategories = (AutoCompleteTextView) findViewById(R.id.tasksAutoCompleteView);
        btn_saveTasks = (ImageButton) findViewById(R.id.btn_saveTask);
        dbHelper = new DBHelper(this);

        //process dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.categories, android.R.layout.simple_dropdown_item_1line);
        tasksCategories.setAdapter(adapter);
        tasksCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_category = (String) adapterView.getItemAtPosition(i);
                //Toast.makeText(getApplicationContext(),"Selected category: " + selected_category, Toast.LENGTH_LONG).show();
            }
        });


        //process date picker
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //date picker dialog

                datePickerDialog = new DatePickerDialog(AddTasks.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        select_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        System.out.println("Date is: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear,mMonth, mDay);
                datePickerDialog.show();
            }
        });



        btn_saveTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the string username broadcast from login to stand in as the
                //determiner of who enters tasks. Should be replaced by the username or userId
                tasks_sharedPrefs = getSharedPreferences("user_details",MODE_PRIVATE);

//get inputs to insert to db
                String USERNAME = tasks_sharedPrefs.getString("username",null);
                String taskTitle_String = task_title.getText().toString().trim();
                String taskDescription_String = task_description.getText().toString().trim();
                String selectedDate_String = select_date.getText().toString().trim();
                String TaskAssociatedPrice =  priceAutocompleteView.getText().toString().trim();


                //process inputs

                if (taskTitle_String.isEmpty())





                //caveat for avoiding null value sent to db, more memory of course
                //TODO:accept empty values on task addition
               if (TaskAssociatedPrice.equals("")){
                   Price = 0.0;
               }else {
                   Price = Double.parseDouble(TaskAssociatedPrice);
               }

                //Process dates


                date_now = LocalDate.now();

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/L/yyyy");
                try {
                    selected_date = getDateFromString(selectedDate_String, dateTimeFormatter);
                    System.out.println(selected_date);
                    System.out.println(date_now);

                }catch (IllegalArgumentException e){
                    System.out.println("Exception" + e);
                }

               if (selected_date.compareTo(date_now) < 0){
                   Toast.makeText(getApplicationContext(), "Choose another date", Toast.LENGTH_LONG).show();
                   select_date.setText("");

               }
                if (selected_date.compareTo(date_now) > 0 || selected_date.compareTo(date_now) == 0){
                    try {
                        //insert task to db if dates are cool
                        boolean b = dbHelper.insertTasks(USERNAME, taskTitle_String,taskDescription_String,selected_category,Price,selected_date);
                        if (taskTitle_String.equals("") || taskDescription_String.equals("") || selectedDate_String.equals("")){
                            Toast.makeText(getApplicationContext(), "Missing content", Toast.LENGTH_LONG).show();
                        }
                        if (b){
                            //put out notification of success and redirect to taskview
                            Toast.makeText(getApplicationContext(), "Task inserted successfully", Toast.LENGTH_LONG).show();
                            finish();


                        }else{
                            Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        System.out.println("Error: " + e);
                    }

                }
















            }
        });

    }


    }
