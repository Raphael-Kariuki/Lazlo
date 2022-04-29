package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
/* added code */

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    /* added code */
   MaterialButton login, sign_up;
    DBHelper dbHelper;

    @Override
    public void onBackPressed(){
        MainActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        login =  findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        sign_up = findViewById(R.id.btn_sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}