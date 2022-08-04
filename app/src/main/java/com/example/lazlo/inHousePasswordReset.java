package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.NoSuchAlgorithmException;

public class inHousePasswordReset extends AppCompatActivity {

    MaterialButton inHouse_resetPassword;
    String inHouse_passwordText, inHouse_confirmPasswordText;
    TextInputEditText inHouse_passwordTextView, inHouse_confirmPasswordTextView;
    TextInputLayout inHouse_passwordLayout, inHouse_confirmPasswordLayout;
    DBHelper dbHelper;
    SharedPreferences spf;
    Double randUserId;

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), myAccount.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_house_password_reset);

        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId", null));

        dbHelper = new DBHelper(this);

        inHouse_passwordTextView = findViewById(R.id.inHouse_passwordTextView);
        inHouse_confirmPasswordTextView = findViewById(R.id.inHouse_confirmPasswordTextView);


        inHouse_passwordLayout = findViewById(R.id.inHouse_passwordLayout);
        inHouse_passwordLayout.requestFocus();

        inHouse_confirmPasswordLayout = findViewById(R.id.inHouse_confirmPasswordLayout);


        inHouse_resetPassword = findViewById(R.id.inHouse_btnNewPassword);

        inHouse_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inHouse_passwordText = inHouse_passwordTextView.getText().toString().trim();
                inHouse_confirmPasswordText = inHouse_confirmPasswordTextView.getText().toString().trim();

                if (!inHouse_passwordText.isEmpty()){
                    if (!inHouse_confirmPasswordText.isEmpty()){
                        if (inHouse_passwordText.length() >= 8){
                            if (inHouse_passwordText.equals(inHouse_confirmPasswordText)){
                                SignUp signUp = new SignUp();
                                if (signUp.passwordCheck(inHouse_confirmPasswordText)){
                                    boolean b = false;
                                    try {
                                        b = dbHelper.updateByRandomUserId(randUserId, signUp.crypto(inHouse_passwordText));
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    }
                                    if (b){
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), myAccount.class));
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Password reset failed, kindly restart the process", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), inHousePasswordReset.class));
                                    }

                                }else{
                                    inHouse_passwordLayout.setErrorEnabled(true);
                                    inHouse_confirmPasswordLayout.setErrorEnabled(false);
                                    inHouse_passwordLayout.setError("Password must contain at-least 2 of UPPERCASE," +
                                            "lowercase, numbers and symbols");
                                }
                            }else{
                                inHouse_passwordLayout.setErrorEnabled(false);
                                inHouse_confirmPasswordLayout.setErrorEnabled(true);
                                inHouse_confirmPasswordLayout.setError("Passwords don't match");
                            }

                        }else{
                            inHouse_passwordLayout.setErrorEnabled(true);
                            inHouse_confirmPasswordLayout.setErrorEnabled(false);
                            inHouse_passwordLayout.setError("Password must be 8+ characters");
                        }
                    }else{
                        inHouse_passwordLayout.setErrorEnabled(false);
                        inHouse_confirmPasswordLayout.setErrorEnabled(true);
                        inHouse_confirmPasswordLayout.setError("Enter a password");
                    }

                }else{
                    inHouse_passwordLayout.setErrorEnabled(true);
                    inHouse_confirmPasswordLayout.setErrorEnabled(false);
                    inHouse_passwordLayout.setError("Enter a password");
                }
            }
        });
    }
}