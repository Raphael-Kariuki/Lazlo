package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.NoSuchAlgorithmException;

public class forgotPassword extends AppCompatActivity {
    AppCompatButton btnResetPassword;
    TextInputEditText resetPasswordEmailInput;
    TextInputLayout resetPasswordEmailInputLayout;
    DBHelper dbHelper;
    AppCompatTextView resetSuccessText;

    LinearLayout showToResetPasswordLayout;
    String emailAddress;
    Cursor success;

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), Login.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        dbHelper = new DBHelper(this);

        //show layout containing email input field
        showToResetPasswordLayout = findViewById(R.id.showToResetPassword);
        showToResetPasswordLayout.setVisibility(View.VISIBLE);

        //display layout on success of password reset


        resetPasswordEmailInput = findViewById(R.id.resetPasswordEmailInput);
        resetPasswordEmailInputLayout = findViewById(R.id.resetPasswordEmailInputLayout);



        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailAddress = resetPasswordEmailInput.getText().toString().trim();
                if (!emailAddress.isEmpty()){
                    if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
                        if(emailExists(emailAddress)){
                            if (updateTempPassphrase(emailAddress)) {

                                Toast.makeText(forgotPassword.this, "Success, redirecting", Toast.LENGTH_SHORT).show();
                                int delayTime = 1;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent resetPassword = new Intent(getApplicationContext(), reset_password.class);
                                        resetPassword.putExtra("emailAddress", emailAddress);
                                        startActivity(resetPassword);
                                        finish();
                                    }
                                }, delayTime * 5000);
                            }else{
                                resetSuccessText.setText(R.string.Failed_password_reset);
                            }

                        }else{
                            resetPasswordEmailInputLayout.setErrorEnabled(true);
                            resetPasswordEmailInputLayout.setError("Email doesn't exist");
                        }
                    }else{
                        resetPasswordEmailInputLayout.setErrorEnabled(true);
                        resetPasswordEmailInputLayout.setError("Wrong email format");
                    }
                }else{
                    resetPasswordEmailInputLayout.setErrorEnabled(true);
                    resetPasswordEmailInputLayout.setError("Blank email");
                }

            }
        });
    }

    public boolean updateTempPassphrase(String emailAddress){
        boolean b = false;
        try {
            b = dbHelper.updateByEmail(emailAddress, HouseOfCommons.crypto("gloriana"));
            System.out.println("Update success");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Update error: " + e);
        }
        return b;
    }

    //function to check whether the email entered exists so as to provide a feedback to the user which would otherwise not be visible.
    //Called when the signup button is pressed.
    public boolean emailExists(String email){

        boolean a = false;
        try {
            success = dbHelper.getUserEmail();
        }catch (Exception e){
            e.printStackTrace();
        }
        while(success != null && success.moveToNext()){
            if(success.getString(success.getColumnIndexOrThrow("email")).equals(email)){
                success.close();
                Toast.makeText(this, "Email exists", Toast.LENGTH_SHORT).show();
                a =  true;
                break;
            }
        }
        return a;
    }

}