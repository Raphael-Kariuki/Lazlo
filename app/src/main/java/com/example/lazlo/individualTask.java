package com.example.lazlo;

import static com.example.lazlo.AddTasks.getDateFromString;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Objects;

public class individualTask extends AppCompatActivity {
  TextInputEditText individualTaskTitle_TextInputEdit, individualTaskDescription_TextInputEdit,
          individualTaskBills_TextInputEdit,individualTaskDateDeadline_TextInputEdit,individualTaskTimeDeadline_TextInputEdit;
  AutoCompleteTextView individualTaskCategory_TextInputEdit;
  AppCompatButton btnSave, btnShow;
  DBHelper dbHelper;
  long currentId;
  Cursor cursor;
  String selectedCategory,timeDate2update;
  LocalDateTime selected_date;
  Double Price;
  boolean f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_task);

        individualTaskTitle_TextInputEdit = findViewById(R.id.individualTaskTitle_TextInputEdit);
        individualTaskDescription_TextInputEdit = findViewById(R.id.individualTaskDescription_TextInputEdit);
        individualTaskCategory_TextInputEdit = findViewById(R.id.individualTaskCategory_TextInputEdit);
        individualTaskBills_TextInputEdit = findViewById(R.id.individualTaskBills_TextInputEdit);
        individualTaskDateDeadline_TextInputEdit = findViewById(R.id.individualTaskDateDeadline_TextInputEdit);
        individualTaskTimeDeadline_TextInputEdit = findViewById(R.id.individualTaskTimeDeadline_TextInputEdit);

        btnSave = findViewById(R.id.btnSave);
        btnShow = findViewById(R.id.btnShow);

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
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                String updateTime = Objects.requireNonNull(individualTaskTimeDeadline_TextInputEdit.getText()).toString().trim();
                System.out.println(updateDate + updateTime);


                /*
                * Formatting dates are tricky. What the code below does is take the time section
                * HH:ss PM/AM split it first to obtain "HH" and "mm PM".
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
                String new_date = parseDate(updateDate);



                String updateDateTime = new_date + " " +new_hour + ":" + new_minute ;
                System.out.println(updateDateTime);
                if (!updateTitle.isEmpty()){
                    if (!updateDescription.isEmpty()){
                        if (!updateCategory.isEmpty() && (updateCategory.equals("Shopping") || updateCategory.equals("Work") || updateCategory.equals("School") || updateCategory.equals("Business") || updateCategory.equals("Home") )){
                            AddTasks addTasks = new AddTasks();
                            if (!updatePrice.isEmpty() && addTasks.willPriceFormat(updatePrice)){
                                if (!updateDate.isEmpty() && addTasks.willDateFormat(updateDateTime)){
                                    LocalDateTime date_now = LocalDateTime.now();
                                    if (selected_date.compareTo(date_now) > 0 || selected_date.compareTo(date_now) == 0){
                                        try {
                                            f = dbHelper.update(currentId,null,updateTitle,updateDescription,updateCategory,updatePrice,updateDateTime);
                                            Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_LONG).show();
                                        }catch (Exception e){
                                            Toast.makeText(getApplicationContext(), "Update failure", Toast.LENGTH_LONG).show();
                                            System.out.println("Db update error: " + e);
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
    }
    public void showData(){
        String regex;
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

            individualTaskCategory_TextInputEdit.setText(cursor.getString(cursor.getColumnIndexOrThrow("TaskCategory")));

            individualTaskBills_TextInputEdit.setText(cursor.getString(cursor.getColumnIndexOrThrow("TaskAssociatedPrice")));

            String timeDateToFormat = cursor.getString(cursor.getColumnIndexOrThrow("TaskDeadline"));

            if(timeDateToFormat.contains("T")){
                regex = "T";
            }else{
                regex = " ";
            }
            String[] dateTime = timeDateToFormat.split(regex, 2);
            individualTaskDateDeadline_TextInputEdit.setText(dateTime[0]);
            individualTaskTimeDeadline_TextInputEdit.setText(dateTime[1]);

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
                AddTasks addTasks = new AddTasks();
                individualTaskTimeDeadline_TextInputEdit.setText(addTasks.FormatTime(hour, minute));
            }
        },hour, minute,false);
        timePickerDialog.show();
    }



    private String parseDate(String toDecideOn){
      String regex = "";
      String new_day = "",new_month = "",new_year = "",new_date;
        if (toDecideOn.contains("/")){
            regex = "/";
      }else if(toDecideOn.contains("-")){
            regex = "-";
      }
        String[] date = toDecideOn.split(regex,3);

        if (Integer.parseInt(date[0]) > 31){
            new_year = date[0];
            new_month = date[1];
            new_day = date[2];
        }else if(Integer.parseInt(date[0]) < 31){
            new_day = date[0];
            new_month = date[1];
            new_year = date[2];
        }
        new_date = new_day + "-" + new_month + "-" + new_year;
        return new_date;
    }

    }
