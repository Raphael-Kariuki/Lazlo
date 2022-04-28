package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

/* added code */

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lazlo.Sql.DBHelper;
/**/
import android.os.Bundle;

public class SignUp extends AppCompatActivity {

    EditText username, email, password, confirmPassword;
    TextView switch2login;
    Button btnSignUp;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = findViewById(R.id.SignupUsername_input);
        email = findViewById(R.id.SignupEmail_input);
        password = findViewById(R.id.SignupPassword_input);
        confirmPassword = findViewById(R.id.SignupPasswordConfirm_input);
        btnSignUp = findViewById(R.id.btnSignUp_signUpPage);
        dbHelper = new DBHelper(this);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username1 = username.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                String password1 = password.getText().toString().trim();
                String password2 = confirmPassword.getText().toString().trim();
                boolean b = dbHelper.insertUserData(username1,email1,password1);
                if (!password2.equals(password1)){
                    Toast.makeText(SignUp.this,"Passwords don't match, button poof",Toast.LENGTH_SHORT).show();
                    btnSignUp.setEnabled(false);
                }
                if (b){
                    Toast.makeText(SignUp.this,"Data inserted",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUp.this, Login.class);
                    startActivity(i);
                }else {
                    Toast.makeText(SignUp.this, "Failed to insert the data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        switch2login = findViewById(R.id.Switch2login);
        switch2login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this, Login.class);
                startActivity(i);
            }
        });
    }
}