package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.security.NoSuchAlgorithmException;


public class reset_password extends AppCompatActivity {

    MaterialButton resetPassword;
    String passwordText, confirmPasswordText;
    TextInputEditText passwordTextView, confirmPasswordTextView;
    TextInputLayout passwordLayout, confirmPasswordLayout;
    DBHelper dbHelper;
    String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        emailAddress = getIntent().getStringExtra("emailAddress");

        dbHelper = new DBHelper(this);

        passwordTextView = findViewById(R.id.passwordTextView);
        confirmPasswordTextView = findViewById(R.id.confirmPasswordTextView);

        passwordLayout = findViewById(R.id.passwordLayout);
        passwordLayout.requestFocus();

        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);

        resetPassword = findViewById(R.id.btnNewPassword);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordText = passwordTextView.getText().toString().trim();
                confirmPasswordText = confirmPasswordTextView.getText().toString().trim();

                    if (!passwordText.isEmpty()){
                        if (!confirmPasswordText.isEmpty()){
                            if (passwordText.length() >= 8){
                                    if (passwordText.equals(confirmPasswordText)){
                                        SignUp signUp = new SignUp();
                                        if (signUp.passwordCheck(confirmPasswordText)){
                                            boolean b = false;
                                            try {
                                                b = dbHelper.updateByEmail(emailAddress, signUp.crypto(passwordText));
                                            } catch (NoSuchAlgorithmException e) {
                                                e.printStackTrace();
                                            }
                                            if (b){
                                                Toast.makeText(getApplicationContext(), "Success, redirecting to login", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), Login.class));
                                            }else {
                                                Toast.makeText(getApplicationContext(), "Password reset failed, kindly restart the process", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), forgotPassword.class));
                                            }

                                        }else{
                                            passwordLayout.setErrorEnabled(true);
                                            confirmPasswordLayout.setErrorEnabled(false);
                                            passwordLayout.setError("Password must contain at-least 2 of UPPERCASE," +
                                                    "lowercase, numbers and symbols");
                                        }
                                    }else{
                                        passwordLayout.setErrorEnabled(false);
                                        confirmPasswordLayout.setErrorEnabled(true);
                                        confirmPasswordLayout.setError("Passwords don't match");
                                    }

                            }else{
                                passwordLayout.setErrorEnabled(true);
                                confirmPasswordLayout.setErrorEnabled(false);
                                passwordLayout.setError("Password must be 8+ characters");
                            }
                        }else{
                            passwordLayout.setErrorEnabled(false);
                            confirmPasswordLayout.setErrorEnabled(true);
                            confirmPasswordLayout.setError("Enter a password");
                        }

                    }else{
                        passwordLayout.setErrorEnabled(true);
                        confirmPasswordLayout.setErrorEnabled(false);
                        passwordLayout.setError("Enter a password");
                    }


            }
        });
    }
}