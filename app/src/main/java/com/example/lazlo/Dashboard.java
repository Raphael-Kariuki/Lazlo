package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class Dashboard extends AppCompatActivity {
MaterialAutoCompleteTextView monthsSelectionDropDownOnDashBoard;
String selectedMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        monthsSelectionDropDownOnDashBoard = findViewById(R.id.monthsSelectionDropDownOnDashBoard);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Months, android.R.layout.simple_dropdown_item_1line);
        monthsSelectionDropDownOnDashBoard.setAdapter(adapter);
        monthsSelectionDropDownOnDashBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMonth = (String) adapterView.getItemAtPosition(i);
            }
        });
    }
}