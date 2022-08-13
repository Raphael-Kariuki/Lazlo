package com.example.lazlo;

import static com.example.lazlo.AddTasks.getDateFromString;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class individualTask extends AppCompatActivity {
  TextInputEditText individualTaskTitle_TextInputEdit, individualTaskDescription_TextInputEdit,
          individualTaskBills_TextInputEdit,individualTaskDateDeadline_TextInputEdit,individualTaskTimeDeadline_TextInputEdit;
  AutoCompleteTextView individualTaskCategory_TextInputEdit;
  MaterialButton btnSave;
  AppCompatImageButton btnStartTask;
  DBHelper dbHelper;
  long currentId;
  Cursor cursor;
  String selectedCategory,timeDate2update,Titre, Description, Category, Bills, Deadline;
  LocalDateTime selected_date;
  Double randomTaskId, randUserId;
  SharedPreferences spf;
  boolean f;
  String updateDateTime;

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), PendingTasks.class));
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_task);


        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("View and edit individual task");

        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId",null));

        individualTaskTitle_TextInputEdit = findViewById(R.id.individualTaskTitle_TextInputEdit);
        individualTaskDescription_TextInputEdit = findViewById(R.id.individualTaskDescription_TextInputEdit);
        individualTaskCategory_TextInputEdit = findViewById(R.id.individualTaskCategory_TextInputEdit);
        individualTaskBills_TextInputEdit = findViewById(R.id.individualTaskBills_TextInputEdit);
        individualTaskDateDeadline_TextInputEdit = findViewById(R.id.individualTaskDateDeadline_TextInputEdit);
        individualTaskTimeDeadline_TextInputEdit = findViewById(R.id.individualTaskTimeDeadline_TextInputEdit);

        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DBHelper(this);



        btnStartTask = findViewById(R.id.btnStartTask);




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

        //needs to be below obtaining the currentId from final page else we'll be sending blanks
        btnStartTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startTask = new Intent(getApplicationContext(), performTask.class );
                startTask.putExtra("taskId", currentId);
                startTask.putExtra("randomTaskId", randomTaskId);
                startTask.putExtra("taskTitle", Titre);
                startTask.putExtra("taskDescription", Description);
                startTask.putExtra("taskCategory", Category);
                startTask.putExtra("taskBills", Bills);
                if (updateDateTime != null){
                    startTask.putExtra("taskDeadline", updateDateTime);
                }else{
                    startTask.putExtra("taskDeadline", Deadline);
                }
                startActivity(startTask);
            }
        });

        individualTaskTimeDeadline_TextInputEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();
            }
        });
        individualTaskDateDeadline_TextInputEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                //date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(individualTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
                        individualTaskDateDeadline_TextInputEdit.setText(formattedDay + "-" + formattedMonth + "-" + year);
                    }
                }, mYear,mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateTitle = individualTaskTitle_TextInputEdit.getText().toString().trim();
                String updateDescription = individualTaskDescription_TextInputEdit.getText().toString().trim();
                String updateCategory = individualTaskCategory_TextInputEdit.getText().toString().trim();
                String updatePrice = individualTaskBills_TextInputEdit.getText().toString().trim();
                String updateDate = individualTaskDateDeadline_TextInputEdit.getText().toString().trim();
                String updateTime = individualTaskTimeDeadline_TextInputEdit.getText().toString().trim();


                /*
                * Formatting dates are tricky.
                * What the code below does is take the time section HH:ss PM/AM split it first to obtain "HH" and "mm PM".
                * The further split "mm PM/AM" to "mm" and "PM/AM"
                * Format the hour by adding a zero when hour is below 9, then split "mm PM" to obtain minutes
                * */
                String[] timeDeh = updateTime.split(":", 2);

                String new_hour, new_minute;
                if(Integer.parseInt(timeDeh[0]) < 10 && timeDeh[0].length() < 2){
                    new_hour = "0" + timeDeh[0];
                }else{
                    new_hour = timeDeh[0];
                }
                new_minute = timeDeh[1].split(" ", 2)[0];
                //===============================

                //this is necessary when the user doesn't make a change on the date
                /*
                * The db output of date is yyyy-MM-dd while the dateDialog one is dd-MM-yyyy, so this is there to cater
                * for all situations, if the first digit after stripping the date is less than 31 then format is dd-MM-yyyy meaning user has changed
                * the date , however if the first digit is greater than 31 then the format is yyyy-MM-dd meaning the user hasn't made any change to the date.
                * It is as from the db
                * */
                HouseOfCommons houseOfCommons = new HouseOfCommons();
                String new_date = houseOfCommons.parseDate(updateDate);


                //combine the date and time ready for formatting

                updateDateTime = new_date + " " +new_hour + ":" + new_minute ;
                if (!updateTitle.isEmpty()){
                    if (!updateDescription.isEmpty()){
                        if (!updateCategory.isEmpty() && (updateCategory.equals("Shopping") || updateCategory.equals("Work") || updateCategory.equals("School") || updateCategory.equals("Business") || updateCategory.equals("Home") )){
                            AddTasks addTasks = new AddTasks();
                            if (!updatePrice.isEmpty() && addTasks.willPriceFormat(updatePrice)){
                                if (!updateDate.isEmpty() && willDateFormat(updateDateTime)){
                                    LocalDateTime date_now = LocalDateTime.now();
                                    if (selected_date.compareTo(date_now) > 0 || selected_date.compareTo(date_now) == 0){
                                        try {
                                            f = dbHelper.updateTask(currentId,null,updateTitle,updateDescription,updateCategory,updatePrice,selected_date);
                                            Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_LONG).show();
                                        }catch (Exception e){
                                            Toast.makeText(getApplicationContext(), "Update failure", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "Wrong date", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Wrong price syntax", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Empty category", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Empty description", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Empty title", Toast.LENGTH_LONG).show();
                }




            }
        });


    }
    public void showData(){
        String regex;
        System.out.println("Populating...");
        try {
            cursor = dbHelper.getTaskById(currentId, randUserId);
            System.out.println("Success conn to db...with id: " + currentId);
        }catch (Exception e){
            Toast.makeText(this,"Error " + e + "occurred", Toast.LENGTH_LONG).show();
        }
        System.out.println("done...");
        if (cursor.moveToFirst()){
            System.out.println("Setting text...");
            randomTaskId = cursor.getDouble(cursor.getColumnIndexOrThrow("randTaskId"));

            Titre = cursor.getString(cursor.getColumnIndexOrThrow("TaskTitle"));
            individualTaskTitle_TextInputEdit.setText(Titre);
            System.out.println("TaskTitle" + cursor.getString(cursor.getColumnIndexOrThrow("TaskTitle")));


            Description = cursor.getString(cursor.getColumnIndexOrThrow("TaskDescription"));
            individualTaskDescription_TextInputEdit.setText(Description);
            System.out.println("TaskDescription" + cursor.getString(cursor.getColumnIndexOrThrow("TaskDescription")));

            Category = cursor.getString(cursor.getColumnIndexOrThrow("TaskCategory"));
            individualTaskCategory_TextInputEdit.setText(Category);

            Bills = cursor.getString(cursor.getColumnIndexOrThrow("TaskAssociatedPrice"));
            individualTaskBills_TextInputEdit.setText(Bills);

            String timeDateToFormat = cursor.getString(cursor.getColumnIndexOrThrow("TaskDeadline"));


            if(timeDateToFormat.contains("T")){
                regex = "T";
            }else{
                regex = " ";
            }
            String[] dateTime = timeDateToFormat.split(regex, 2);
            individualTaskDateDeadline_TextInputEdit.setText(dateTime[0]);
            individualTaskTimeDeadline_TextInputEdit.setText(dateTime[1]);

            Deadline = dateTime[0] + " " + dateTime[1];

        }
        cursor.close();


    }
    private void selectTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if (hour < 10){
                    timeDate2update = "0" + hour + ":" + minute;
                }else{
                    timeDate2update = hour + ":" + minute;
                }
                HouseOfCommons houseOfCommons = new HouseOfCommons();
                individualTaskTimeDeadline_TextInputEdit.setText(houseOfCommons.FormatTime(hour, minute));
            }
        },hour, minute,false);
        timePickerDialog.show();
    }

    private boolean willDateFormat(String selectedDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm");
        try {
            selected_date = getDateFromString(selectedDate, dateTimeFormatter);
            return true;
        }catch (IllegalArgumentException e){
            System.out.println("Date Exception" + e);
            return false;
        }
    }





    }
