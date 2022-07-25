package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class forgotPassword extends AppCompatActivity {
    AppCompatButton btnResetPassword;
    TextInputEditText resetPasswordEmailInput;
    TextInputLayout resetPasswordLayout;
    DBHelper dbHelper;
    String uname, passkey;

    LinearLayout showToResetPassword,showOnPasswordReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        showOnPasswordReset = findViewById(R.id.showOnPasswordReset);
        showOnPasswordReset.setVisibility(View.INVISIBLE);

        showToResetPassword = findViewById(R.id.showToResetPassword);
        showToResetPassword.setVisibility(View.VISIBLE);

        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(view -> {
            System.out.print("button clicked");
            resetPasswordEmailInput = findViewById(R.id.resetPassword);
            resetPasswordLayout = findViewById(R.id.resetPasswordLayout);
            String resetEmail = resetPasswordEmailInput.getText().toString().trim();
            System.out.print(resetEmail);
           /* Cursor cursor = dbHelper.getByEmail(resetEmail);

                if (!resetEmail.isEmpty()){
                    if (Patterns.EMAIL_ADDRESS.matcher(resetEmail).matches()){
                        if (cursor.getCount() != 0){
                            try {
                                SignUp signUp = new SignUp();
                                boolean success = dbHelper.updateByEmail(resetEmail, signUp.crypto("blah"));

                                if (success){
                                    showOnPasswordReset.setVisibility(View.VISIBLE);
                                    showToResetPassword.setVisibility(View.INVISIBLE);

                                }
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        }else {
                            resetPasswordLayout.setErrorEnabled(true);
                            resetPasswordLayout.setError("Enter an existing email address");
                        }
                    }else {
                        resetPasswordLayout.setErrorEnabled(true);
                        resetPasswordLayout.setError("Enter a valid email address");
                    }

                }else {
                    resetPasswordLayout.setErrorEnabled(true);
                    resetPasswordLayout.setError("Enter your email");
                }*/
        });
    }
    public boolean emailExists(Cursor cursor,String emailAddress){
        while (cursor.moveToNext()){
            if (!cursor.getString(cursor.getColumnIndexOrThrow("userName")).isEmpty()){
                return true;
            }else{
                resetPasswordLayout.setErrorEnabled(true);
                resetPasswordLayout.setError("Email does not exist");
                return false;
            }
        }
        return false;
    }
}