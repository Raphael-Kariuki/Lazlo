package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.NoSuchAlgorithmException;

public class forgotPassword extends AppCompatActivity {
    AppCompatButton btnResetPassword;
    TextInputEditText resetPasswordEmailInput;
    TextInputLayout resetPasswordEmailInputLayout;
    DBHelper dbHelper;
    String uname, passkey;
    AppCompatTextView resetSuccessText;

    LinearLayout showToResetPasswordLayout,showOnPasswordResetLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //show layout containing email input field
        showToResetPasswordLayout = findViewById(R.id.showToResetPassword);
        showToResetPasswordLayout.setVisibility(View.VISIBLE);

        //display layout on success of password reset
        showOnPasswordResetLayout = findViewById(R.id.showOnPasswordReset);
        showOnPasswordResetLayout.setVisibility(View.INVISIBLE);

        resetPasswordEmailInput = findViewById(R.id.resetPasswordEmailInput);
        resetPasswordEmailInputLayout = findViewById(R.id.resetPasswordEmailInputLayout);



        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(view -> resetPassword());

    }

    //handle password reset button click
    public void resetPassword(){
        String emailAddress = resetPasswordEmailInput.getText().toString().trim();
        Cursor cursor = null;
        try {
            cursor = dbHelper.getByEmail(emailAddress);
            
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        if (!emailAddress.isEmpty()){
            if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
                if (emailExists(cursor,emailAddress)){
                    if (!updateTempPassphrase(emailAddress)) {
                        resetSuccessText = findViewById(R.id.resetSuccess);
                        resetSuccessText.setText(R.string.Failed_password_reset);
                    }
                    showOnPasswordResetLayout.setVisibility(View.VISIBLE);
                }else{
                    resetPasswordEmailInputLayout.setErrorEnabled(true);
                    resetPasswordEmailInputLayout.setError("email address does not exist");
                }

            }else{
                resetPasswordEmailInputLayout.setErrorEnabled(true);
                resetPasswordEmailInputLayout.setError("Enter a valid email address");
            }
        }else{
            resetPasswordEmailInputLayout.setErrorEnabled(true);
            resetPasswordEmailInputLayout.setError("Enter an email address");
        }
    }
    public boolean emailExists(Cursor cursor,String emailAddress){
    if (cursor == null){
        return false;
    }
     return true;
    }

    public boolean updateTempPassphrase(String emailAddress){
        SignUp signUp = new SignUp();
        try {
            dbHelper.updateByEmail(emailAddress,signUp.crypto("gloriana"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

}