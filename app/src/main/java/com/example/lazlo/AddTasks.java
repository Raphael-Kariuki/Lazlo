package com.example.lazlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class AddTasks extends AppCompatActivity {
    TextInputEditText task_title,task_description,select_date,priceAutocompleteView,selectTime_AutocompleteView,predictedDuration_TextInputEditText;
    DatePickerDialog datePickerDialog;
    MaterialButton btn_saveTasks;
    DBHelper dbHelper;
    SharedPreferences tasks_sharedPrefs;
    LocalDateTime selected_date,date_now;
    AutoCompleteTextView tasksCategories,predictedDurationUnits_AutoCompleteTextView;
    String selected_category;
    String selected_time;
    String predictedDurationUnits;
    Double Price;
    TextInputLayout taskTitle_TextLayout,taskDescription_TextLayout,tasksCategoryTextLayout,
            price_TextLayout,selectedDate_TextInputLayout,selectedTime_TextInputLayout,predictedDuration_TextInputLayout,predictedDurationUnits_TextInputLayout;
    boolean b,d;






    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), tasks.class));

    }

    /*
    * function override to save draft task when the user enters some values and doesn't save the task but rather exists the activity.
    * This is to aid the user to pick up from where they left*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (task_title.getText().toString().isEmpty() && task_description.getText().toString().isEmpty()) {
                startActivity(new Intent(getApplicationContext(), tasks.class));

            } else {
                try {
                    Double  randUserId = Double.parseDouble(tasks_sharedPrefs.getString("randomUserId", null));
                    String taskTitle_String = task_title.getText().toString().trim();
                    String taskDescription_String = task_description.getText().toString().trim();
                    String selectedDate_String = select_date.getText().toString().trim();
                    String TaskAssociatedPrice = priceAutocompleteView.getText().toString().trim();
                    String selectedCategory_string = tasksCategories.getText().toString().trim();
                    d = dbHelper.insertDraftTasks(randUserId, taskTitle_String, taskDescription_String, selectedCategory_string, TaskAssociatedPrice, selectedDate_String);
                    if (d) {
                        Toast.makeText(getApplicationContext(), "Draft saved successfully", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Draft not saved", Toast.LENGTH_LONG).show();
                    }
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        //initialize db class to use in sql transactions
        dbHelper = new DBHelper(this);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        //set actionBat title
        actionBar.setTitle("Create new task");


        //obtain TextInputViews
        task_title =  findViewById(R.id.taskTitleAutoCompleteView);
        task_title.requestFocus();
        task_description = findViewById(R.id.taskDescriptionAutoCompleteView);
        select_date =  findViewById(R.id.selectDate_AutocompleteView);
        priceAutocompleteView = findViewById(R.id.priceAutoCompleteView);
        tasksCategories = findViewById(R.id.tasksAutoCompleteView);
        selectTime_AutocompleteView = findViewById(R.id.selectTime_AutocompleteView);
        predictedDuration_TextInputEditText = findViewById(R.id.predictedDuration_TextInputEditText);
        predictedDurationUnits_AutoCompleteTextView = findViewById(R.id.predictedDurationUnits_AutoCompleteTextView);




        //obtain the layout
        taskTitle_TextLayout = findViewById(R.id.taskTitle_TextLayout);
        taskDescription_TextLayout = findViewById(R.id.taskDescription_TextLayout);
        tasksCategoryTextLayout = findViewById(R.id.tasksCategoryTextLayout);
        price_TextLayout = findViewById(R.id.price_TextLayout);
        selectedDate_TextInputLayout = findViewById(R.id.selectedDate_TextInputLayout);
        selectedTime_TextInputLayout = findViewById(R.id.selectedTime_TextInputLayout);
        predictedDuration_TextInputLayout = findViewById(R.id.predictedDuration_TextInputLayout);
        predictedDurationUnits_TextInputLayout = findViewById(R.id.predictedDurationUnits_TextInputLayout);

        //obtain the save task button
        btn_saveTasks = findViewById(R.id.btn_saveTask);



        // get the string username broadcast from login to stand in as the
        //determiner of who enters tasks. Should be replaced by the username or userId
        tasks_sharedPrefs = getSharedPreferences("user_details",MODE_PRIVATE);




        //populate the category dropdown

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.categories, android.R.layout.simple_dropdown_item_1line);
        tasksCategories.setAdapter(adapter);
        tasksCategories.setOnItemClickListener((adapterView, view, i, l) -> selected_category = (String) adapterView.getItemAtPosition(i));

        //populate the duration units dropdown
        ArrayAdapter<CharSequence> unitsAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.durationUnits, android.R.layout.simple_dropdown_item_1line);
        predictedDurationUnits_AutoCompleteTextView.setAdapter(unitsAdapter);
        predictedDurationUnits_AutoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> predictedDurationUnits = (String) adapterView.getItemAtPosition(i));






        //process date selection by calling function that further handles that
        select_date.setOnClickListener(view -> selectDate());

        //process time selection by calling function that further handles that
        selectTime_AutocompleteView.setOnClickListener(view -> selectTime());



        //process on save button click
        btn_saveTasks.setOnClickListener(view -> {

            //get inputs to insert to db
            String randUserId = tasks_sharedPrefs.getString("randomUserId", null);
            String taskTitle_String = Objects.requireNonNull(task_title.getText()).toString().trim();
            String taskDescription_String = Objects.requireNonNull(task_description.getText()).toString().trim();
            String selectedDate_String = Objects.requireNonNull(select_date.getText()).toString().trim();
            String TaskAssociatedPrice =  Objects.requireNonNull(priceAutocompleteView.getText()).toString().trim();
            String selectedCategory_string = tasksCategories.getText().toString().trim();
            String selectedTime_String = Objects.requireNonNull(selectTime_AutocompleteView.getText()).toString().trim();
            String selectedDateTime = selectedDate_String + " " + selected_time;
            String predictedDuration = Objects.requireNonNull(predictedDuration_TextInputEditText.getText()).toString().trim();
            String predictedDurationUnits = predictedDurationUnits_AutoCompleteTextView.getText().toString().trim();

            //process inputs
            if (!taskTitle_String.isEmpty()){
                if (!taskDescription_String.isEmpty()){
                    if (!selectedCategory_string.isEmpty()){
                        if ( (selectedCategory_string.equals("Shopping") || selectedCategory_string.equals("Work") || selectedCategory_string.equals("School") || selectedCategory_string.equals("Business") || selectedCategory_string.equals("Home") )){
                            if (!TaskAssociatedPrice.isEmpty() && HouseOfCommons.priceCheck(TaskAssociatedPrice)){
                                willPriceFormat(TaskAssociatedPrice);
                                if (!predictedDuration.isEmpty()){
                                    if (HouseOfCommons.durationCheck(predictedDuration)){
                                        if (!predictedDurationUnits.isEmpty()){
                                            if (!selectedDate_String.isEmpty() && HouseOfCommons.dateCheck(selectedDate_String)){
                                                if (!selectedTime_String.isEmpty() && HouseOfCommons.timeCheck(selectedTime_String)){
                                                    if (willDateFormat(selectedDateTime)){
                                                        date_now = LocalDateTime.now();
                                                        if (selected_date.compareTo(date_now) > 0 || selected_date.compareTo(date_now) == 0) {
                                                            try {

                                                                String duration = HouseOfCommons.processPredictedDuration(predictedDuration,predictedDurationUnits);
                                                                Double randomTaskId = HouseOfCommons.generateRandomId();
                                                                Integer defaultTaskState = 0;
                                                                String defaultParentTaskId = "0.0";
                                                                b = dbHelper.insertTasks(randomTaskId,Double.parseDouble(randUserId), taskTitle_String, taskDescription_String, selected_category, Price, selected_date,new Date().getTime(),duration,defaultTaskState,defaultParentTaskId);

                                                            }catch(Exception e){
                                                                System.out.println("Db insertion error: " + e);
                                                            }
                                                            if (b){
                                                                Toast.makeText(getApplicationContext(), "Task inserted successfully", Toast.LENGTH_LONG).show();
                                                                Intent categoryStringToSendToPendingTasks = new Intent(getApplicationContext(), tasks.class);
                                                                categoryStringToSendToPendingTasks.putExtra("tempCategory", selected_category);
                                                                startActivity(categoryStringToSendToPendingTasks);
                                                                //finish();
                                                            }else {
                                                                Toast.makeText(getApplicationContext(), "Task insert failure", Toast.LENGTH_LONG).show();
                                                            }
                                                        }else{
                                                            Toast.makeText(getApplicationContext(), "Choose another date", Toast.LENGTH_LONG).show();
                                                            select_date.setText("");
                                                        }
                                                    }else{
                                                        selectedDate_TextInputLayout.setErrorEnabled(true);
                                                        selectedDate_TextInputLayout.setError("select date");
                                                        selectedTime_TextInputLayout.setErrorEnabled(true);
                                                        selectedTime_TextInputLayout.setError("select time");
                                                        taskTitle_TextLayout.setErrorEnabled(false);
                                                        taskDescription_TextLayout.setErrorEnabled(false);
                                                        price_TextLayout.setErrorEnabled(false);
                                                        tasksCategoryTextLayout.setErrorEnabled(false);
                                                        predictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                                        predictedDuration_TextInputLayout.setErrorEnabled(false);
                                                    }

                                                }else{
                                                    selectedDate_TextInputLayout.setErrorEnabled(false);
                                                    selectedTime_TextInputLayout.setErrorEnabled(true);
                                                    selectedTime_TextInputLayout.setError("select time");
                                                    taskTitle_TextLayout.setErrorEnabled(false);
                                                    taskDescription_TextLayout.setErrorEnabled(false);
                                                    price_TextLayout.setErrorEnabled(false);
                                                    tasksCategoryTextLayout.setErrorEnabled(false);
                                                    predictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                                    predictedDuration_TextInputLayout.setErrorEnabled(false);
                                                }


                                            }else{
                                                selectedDate_TextInputLayout.setErrorEnabled(true);
                                                selectedDate_TextInputLayout.setError("Blank deadline");
                                                taskTitle_TextLayout.setErrorEnabled(false);
                                                taskDescription_TextLayout.setErrorEnabled(false);
                                                price_TextLayout.setErrorEnabled(false);
                                                tasksCategoryTextLayout.setErrorEnabled(false);
                                                selectedTime_TextInputLayout.setErrorEnabled(false);
                                                predictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                                predictedDuration_TextInputLayout.setErrorEnabled(false);
                                            }
                                        }else {
                                            selectedDate_TextInputLayout.setErrorEnabled(false);
                                            selectedTime_TextInputLayout.setErrorEnabled(false);
                                            taskTitle_TextLayout.setErrorEnabled(false);
                                            taskDescription_TextLayout.setErrorEnabled(false);
                                            price_TextLayout.setErrorEnabled(false);
                                            tasksCategoryTextLayout.setErrorEnabled(false);
                                            predictedDuration_TextInputLayout.setErrorEnabled(false);
                                            predictedDurationUnits_TextInputLayout.setErrorEnabled(true);
                                            predictedDurationUnits_TextInputLayout.setError("Choose a unit of duration");
                                        }

                                    }else{
                                        selectedDate_TextInputLayout.setErrorEnabled(false);
                                        selectedTime_TextInputLayout.setErrorEnabled(false);
                                        taskTitle_TextLayout.setErrorEnabled(false);
                                        taskDescription_TextLayout.setErrorEnabled(false);
                                        price_TextLayout.setErrorEnabled(false);
                                        tasksCategoryTextLayout.setErrorEnabled(false);
                                        predictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                        predictedDuration_TextInputLayout.setErrorEnabled(true);
                                        predictedDuration_TextInputLayout.setError("Enter a valid duration amount");
                                    }


                                }else{
                                    selectedDate_TextInputLayout.setErrorEnabled(false);
                                    selectedTime_TextInputLayout.setErrorEnabled(false);
                                    taskTitle_TextLayout.setErrorEnabled(false);
                                    taskDescription_TextLayout.setErrorEnabled(false);
                                    price_TextLayout.setErrorEnabled(false);
                                    tasksCategoryTextLayout.setErrorEnabled(false);
                                    predictedDuration_TextInputLayout.setErrorEnabled(true);
                                    predictedDuration_TextInputLayout.setError("Empty predicted duration");
                                    predictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                    predictedDuration_TextInputLayout.setErrorEnabled(false);
                                }

                            }else{
                                price_TextLayout.setErrorEnabled(true);
                                price_TextLayout.setError("Enter a money figure");
                                taskTitle_TextLayout.setErrorEnabled(false);
                                taskDescription_TextLayout.setErrorEnabled(false);
                                tasksCategoryTextLayout.setErrorEnabled(false);
                                selectedDate_TextInputLayout.setErrorEnabled(false);
                                selectedTime_TextInputLayout.setErrorEnabled(false);
                                predictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                                predictedDuration_TextInputLayout.setErrorEnabled(false);
                            }

                        }  else{
                            tasksCategoryTextLayout.setErrorEnabled(true);
                            tasksCategoryTextLayout.setError("Choose a category from the dropdown");
                            taskTitle_TextLayout.setErrorEnabled(false);
                            taskDescription_TextLayout.setErrorEnabled(false);
                            price_TextLayout.setErrorEnabled(false);
                            selectedDate_TextInputLayout.setErrorEnabled(false);
                            selectedTime_TextInputLayout.setErrorEnabled(false);
                            predictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                            predictedDuration_TextInputLayout.setErrorEnabled(false);
                        }


                    }else{
                        tasksCategoryTextLayout.setErrorEnabled(true);
                        tasksCategoryTextLayout.setError("Blank category");
                        taskTitle_TextLayout.setErrorEnabled(false);
                        taskDescription_TextLayout.setErrorEnabled(false);
                        price_TextLayout.setErrorEnabled(false);
                        selectedDate_TextInputLayout.setErrorEnabled(false);
                        selectedTime_TextInputLayout.setErrorEnabled(false);
                        predictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                        predictedDuration_TextInputLayout.setErrorEnabled(false);
                    }
                }else{
                    taskDescription_TextLayout.setErrorEnabled(true);
                    taskDescription_TextLayout.setError("Blank description");
                    taskTitle_TextLayout.setErrorEnabled(false);
                    tasksCategoryTextLayout.setErrorEnabled(false);
                    price_TextLayout.setErrorEnabled(false);
                    selectedDate_TextInputLayout.setErrorEnabled(false);
                    selectedTime_TextInputLayout.setErrorEnabled(false);
                    predictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                    predictedDuration_TextInputLayout.setErrorEnabled(false);
                }

            }else{
                taskTitle_TextLayout.setErrorEnabled(true);
                taskTitle_TextLayout.setError("Blank title");
                taskDescription_TextLayout.setErrorEnabled(false);
                tasksCategoryTextLayout.setErrorEnabled(false);
                price_TextLayout.setErrorEnabled(false);
                selectedDate_TextInputLayout.setErrorEnabled(false);
                selectedTime_TextInputLayout.setErrorEnabled(false);
                predictedDurationUnits_TextInputLayout.setErrorEnabled(false);
                predictedDuration_TextInputLayout.setErrorEnabled(false);
                }




        });

    }


    public boolean willDateFormat(String selectedDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm");
        try {
            selected_date = HouseOfCommons.getDateFromString(selectedDate, dateTimeFormatter);
            return true;
        }catch (IllegalArgumentException e){
            System.out.println("Date Exception" + e);
            return false;
        }
    }

    public void willPriceFormat(String priceToParse){
                if (!priceToParse.isEmpty()){
                    try {
                        Price = Double.parseDouble(priceToParse);
                    }catch(java.lang.NumberFormatException e){
                        e.printStackTrace();
                    }
                }else {
                    Price = 0.0;
                }


    }

        private void selectTime(){
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour1, minute1) -> {
                String formattedHour, formattedMinute;
                if (hour1 < 10){
                    formattedHour = "0" + hour1;
                }else{
                    formattedHour = "" + hour1;
                }
                if (minute1 < 10 ){
                    formattedMinute = "0" + minute1;
                }else{
                    formattedMinute = "" + minute1;
                }
                selected_time = formattedHour + ":" + formattedMinute;
                HouseOfCommons houseOfCommons = new HouseOfCommons();
                selectTime_AutocompleteView.setText(houseOfCommons.FormatTime(hour1, minute1));
            },hour, minute,false);
            timePickerDialog.show();
        }
        private void selectDate(){
            final Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            //date picker dialog
            datePickerDialog = new DatePickerDialog(AddTasks.this, (view1, year, monthOfYear, dayOfMonth) -> {
                String formattedMonth,formattedDay;
                if (monthOfYear + 1 <= 9){
                    formattedMonth = "0" + (monthOfYear + 1) ;
                }else{
                    formattedMonth = String.valueOf(monthOfYear + 1);
                }
                if(dayOfMonth < 10){
                    formattedDay = "0" + dayOfMonth;
                }else{
                    formattedDay = String.valueOf(dayOfMonth);
                }
                select_date.setText(String.format(new Locale("en", "KE"), "%s-%s-%s",formattedDay,formattedMonth,year ));
            }, mYear,mMonth, mDay);
            datePickerDialog.show();
        }
    }
