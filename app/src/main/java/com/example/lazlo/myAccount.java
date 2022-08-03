package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class myAccount extends AppCompatActivity {
    AppCompatButton logout,btnTasks,btnDrafts,btnDashboard, btnEditAccount;
    SharedPreferences prf;
    LinearLayout imgStatusNameLayout;
    SharedPreferences spf;
    MaterialTextView usernameInMyAccount,accountStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        usernameInMyAccount = findViewById(R.id.usernameInMyAccount);

        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        usernameInMyAccount.setText(spf.getString("username",null));

        accountStatus = findViewById(R.id.accountStatus);
        accountStatus.setText(spf.getString("Status", null));


        imgStatusNameLayout = findViewById(R.id.imgStatusNameLayout);
        imgStatusNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), editAccount.class));
            }
        });

        btnEditAccount = findViewById(R.id.btnEditAccount);
        btnEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), editAccount.class));
            }
        });

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

        btnDashboard = findViewById(R.id.btnDashBoard);
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
            }
        });
        btnDrafts = findViewById(R.id.btn_Drafts);
        btnDrafts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Drafts.class);
                    intent.putExtra("username", prf.getString("username",null));
                    startActivity(intent);
                }
            });
    }
}