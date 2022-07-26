package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.os.Bundle;
import android.view.View;

import java.util.Date;

public class performTask extends AppCompatActivity {

    AppCompatImageButton btnStartTask2,btnPauseTask;
    AppCompatButton btnCancelDoingTask, btnCompleteDoingTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_task);

        //Handle button to process task durations
        btnStartTask2 = findViewById(R.id.btnStartTask2);
        btnPauseTask = findViewById(R.id.btnPauseTask);
        btnCancelDoingTask = findViewById(R.id.btnCancelDoingTask);
        btnCompleteDoingTask = findViewById(R.id.btnCompleteDoingTask);


        btnStartTask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                System.out.print("Start date " + date + date.getTime());
            }
        });


    }
}