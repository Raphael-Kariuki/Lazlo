package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class editAccount extends AppCompatActivity {
TextInputEditText editUsername_TextInputEdit,editAboutYou_TextInputEdit,editEmail_TextInput;
TextInputLayout editUsername_TextInputLayout,editAboutYou_TextInputLayout,editEmail_TextInputLayout;
DBHelper dbHelper;
SharedPreferences spf;
Double randUserId;
String uname, status, emailAddress;
Cursor success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);


        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId", null));
        uname = spf.getString("username", null);
        status = spf.getString("Status", null);
        emailAddress = spf.getString("email", null);

        dbHelper = new DBHelper(this);

        editUsername_TextInputEdit = findViewById(R.id.editUsername_TextInputEdit);
        editAboutYou_TextInputEdit = findViewById(R.id.editAboutYou_TextInput);

        editUsername_TextInputLayout = findViewById(R.id.editUserName_TextInputLayout);
        editAboutYou_TextInputLayout = findViewById(R.id.editAboutYou_TextInputLayout);

        editEmail_TextInput = findViewById(R.id.editEmail_TextInput);
        editEmail_TextInputLayout = findViewById(R.id.editEmail_TextInputLayout);

        editUsername_TextInputEdit.setText(uname);
        editAboutYou_TextInputEdit.setText(status);
        editEmail_TextInput.setText(emailAddress);





    }
    @Override
    public void onBackPressed() {
        if (!editUsername_TextInputEdit.getText().toString().trim().isEmpty()){
            if (editUsername_TextInputEdit.getText().toString().trim().length() <= 10){
                if (!editAboutYou_TextInputEdit.getText().toString().trim().isEmpty()){
                    if (!editEmail_TextInput.getText().toString().trim().isEmpty()){
                        if (Patterns.EMAIL_ADDRESS.matcher(editEmail_TextInput.getText().toString().trim()).matches()){
                            if (!editAboutYou_TextInputEdit.getText().toString().trim().equals(status)){
                                //status changed
                                if (!editUsername_TextInputEdit.getText().toString().trim().equals(uname)){
                                    //username has been changed
                                    if (!isUserNameExist(editUsername_TextInputEdit.getText().toString().trim())){
                                        //new user name is good to go
                                        if (!editEmail_TextInput.getText().toString().trim().equals(emailAddress)){
                                            //email is changed
                                            if (!emailExists(editEmail_TextInput.getText().toString().trim())){
                                                emailHasBeenChangedUsernameHasBeenChangedStatusHasBeenChanged_Update(randUserId);
                                            }else{
                                                //email exists, feedback
                                                editEmail_TextInputLayout.setErrorEnabled(true);
                                                editEmail_TextInputLayout.setError("Enter a registered email address");
                                                editUsername_TextInputLayout.setErrorEnabled(false);
                                                editAboutYou_TextInputLayout.setErrorEnabled(false);
                                            }

                                        }else{
                                            //email is unchanged, proceed without checking whether it exists
                                            statusHasBeenChanged_Update(randUserId);
                                            userNameHasChanged_Update(randUserId);
                                        }

                                        }else{
                                        //new user name exists, feedback
                                        editEmail_TextInputLayout.setErrorEnabled(false);
                                        editUsername_TextInputLayout.setErrorEnabled(true);
                                        editUsername_TextInputLayout.setError("Enter a different username");
                                        editAboutYou_TextInputLayout.setErrorEnabled(false);
                                    }

                                }else{
                                    //username is un-changed, proceed without checking whether it exists
                                    if (!editEmail_TextInput.getText().toString().trim().equals(emailAddress)){
                                        //email is changed
                                        if (!emailExists(editEmail_TextInput.getText().toString().trim())){
                                            emailHasChanged_Update(randUserId);
                                        }else{
                                            //email exists, feedback
                                            editEmail_TextInputLayout.setErrorEnabled(true);
                                            editEmail_TextInputLayout.setError("Enter a registered email address");
                                            editUsername_TextInputLayout.setErrorEnabled(false);
                                            editAboutYou_TextInputLayout.setErrorEnabled(false);
                                        }
                                    }else{
                                        //email is unchanged, proceed without checking whether it exists
                                        userNameHasChanged_Update(randUserId);
                                        statusHasBeenChanged_Update(randUserId);
                                    }
                                }
                            }else{
                                //status unchanged
                                if (!editUsername_TextInputEdit.getText().toString().trim().equals(uname)){
                                    //username has been changed
                                    if (!isUserNameExist(editUsername_TextInputEdit.getText().toString().trim())){
                                        //new user name is good to go
                                        if (!editEmail_TextInput.getText().toString().trim().equals(emailAddress)){
                                            //email is changed
                                            if (!emailExists(editEmail_TextInput.getText().toString().trim())){
                                                userNameHasChanged_Update(randUserId);
                                                emailHasChanged_Update(randUserId);
                                            }else{
                                                //email exists, feedback
                                                editEmail_TextInputLayout.setErrorEnabled(true);
                                                editEmail_TextInputLayout.setError("Enter a registered email address");
                                                editUsername_TextInputLayout.setErrorEnabled(false);
                                                editAboutYou_TextInputLayout.setErrorEnabled(false);
                                            }
                                        }else{
                                            //email is unchanged, proceed without checking whether it exists
                                            statusHasBeenChanged_Update(randUserId);
                                            userNameHasChanged_Update(randUserId);
                                        }

                                    }else{
                                        //new user name exists, feedback
                                        editEmail_TextInputLayout.setErrorEnabled(false);
                                        editUsername_TextInputLayout.setErrorEnabled(true);
                                        editUsername_TextInputLayout.setError("Enter a different username");
                                        editAboutYou_TextInputLayout.setErrorEnabled(false);
                                    }

                                }else{
                                    ///username is un-changed, proceed without checking whether it exists
                                    if (!editEmail_TextInput.getText().toString().trim().equals(emailAddress)){
                                        //email is changed
                                        if (!emailExists(editEmail_TextInput.getText().toString().trim())){
                                            emailHasChanged_Update(randUserId);
                                        }else{
                                            //email exists, feedback
                                            editEmail_TextInputLayout.setErrorEnabled(true);
                                            editEmail_TextInputLayout.setError("Enter a registered email address");
                                            editUsername_TextInputLayout.setErrorEnabled(false);
                                            editAboutYou_TextInputLayout.setErrorEnabled(false);
                                        }
                                    }else{
                                        //email is unchanged, proceed without checking whether it exists
                                        statusHasBeenChanged_Update(randUserId);
                                        userNameHasChanged_Update(randUserId);
                                    }
                                }
                            }

                        }else{
                            editEmail_TextInputLayout.setErrorEnabled(true);
                            editEmail_TextInputLayout.setError("Enter an valid address");
                            editUsername_TextInputLayout.setErrorEnabled(false);
                            editAboutYou_TextInputLayout.setErrorEnabled(false);
                        }

                        }else{
                        editEmail_TextInputLayout.setErrorEnabled(true);
                        editEmail_TextInputLayout.setError("Enter an email address");
                        editUsername_TextInputLayout.setErrorEnabled(false);
                        editAboutYou_TextInputLayout.setErrorEnabled(false);
                    }
                    }else{
                    editEmail_TextInputLayout.setErrorEnabled(false);
                    editUsername_TextInputLayout.setErrorEnabled(false);
                    editAboutYou_TextInputLayout.setError("Enter about you");
                    editAboutYou_TextInputLayout.setErrorEnabled(true);
                }
            }else{
                editEmail_TextInputLayout.setErrorEnabled(false);
                editUsername_TextInputLayout.setErrorEnabled(true);
                editUsername_TextInputLayout.setCounterEnabled(true);
                editUsername_TextInputLayout.setCounterMaxLength(10);
                editUsername_TextInputLayout.setError("Username shouldn't exceed 10 characters");
                editAboutYou_TextInputLayout.setErrorEnabled(false);
            }
        }else{
            editEmail_TextInputLayout.setErrorEnabled(false);
            editUsername_TextInputLayout.setErrorEnabled(true);
            editUsername_TextInputLayout.setError("Enter your new username");
            editAboutYou_TextInputLayout.setErrorEnabled(false);
        }
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

    //function to check whether the entered username exists in the db so as to provide feedback to the user
    public boolean isUserNameExist(String username){
        Cursor success = null;
        try {
            success = dbHelper.getUserDetailsByUserName(username);

        }catch (Exception e){
            e.printStackTrace();
        }

        if (success != null && success.getCount() == 0 ){
            success.close();
            return false;
        }else{
            return true;
        }
    }

    //username,Status,randUserId
    public void emailHasBeenChangedUsernameHasBeenChangedStatusHasBeenChanged_Update(Double randUserId){
        String username = editUsername_TextInputEdit.getText().toString().trim();
        String emailAddress = editEmail_TextInput.getText().toString().trim();
        String Status = editAboutYou_TextInputEdit.getText().toString().trim();

        boolean b = false;

        try {
            b = dbHelper.updateUserNameAndEmailAndStatus(username,Status,randUserId,emailAddress);
        }catch (Exception e){
            System.out.println("Exception: " + e);
        }

        if (b){
            Toast.makeText(this, "Success updating username", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor =  spf.edit();
            editor.putString("username", username);
            editor.putString("email", emailAddress);
            editor.apply();

            startActivity(new Intent(getApplicationContext(), Account.class));
        }
    }

    public void emailHasBeenChangedUsernameHasBeenChanged_Update(Double randUserId){
        String username = editUsername_TextInputEdit.getText().toString().trim();
        String emailAddress = editEmail_TextInput.getText().toString().trim();

        boolean b = false;

        try {
            b = dbHelper.updateUserNameAndEmail(username,randUserId,emailAddress);
        }catch (Exception e){
            System.out.println("Exception: " + e);
        }

        if (b){
            Toast.makeText(this, "Success updating username", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor =  spf.edit();
            editor.putString("username", username);
            editor.putString("email", emailAddress);
            editor.apply();

            startActivity(new Intent(getApplicationContext(), Account.class));
        }
    }
    public void emailHasChanged_Update(Double randUserId){
        String emailAddress = editEmail_TextInput.getText().toString().trim();

        boolean b = false;

        try {
            b = dbHelper.updateEmailOnly(randUserId,emailAddress);
        }catch (Exception e){
            System.out.println("Exception: " + e);
        }

        if (b){
            Toast.makeText(this, "Success updating email", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor =  spf.edit();
            editor.putString("email", emailAddress);
            editor.apply();

            startActivity(new Intent(getApplicationContext(), Account.class));
        }
    }

    public void userNameHasChanged_Update(Double randUserId){
        String username = editUsername_TextInputEdit.getText().toString().trim();

        boolean b = false;

        try {
            b = dbHelper.updateUserNameOnly(username,randUserId);
        }catch (Exception e){
            System.out.println("Exception: " + e);
        }

        if (b){
            Toast.makeText(this, "Success updating username", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor =  spf.edit();
            editor.putString("username", username);
            editor.apply();

            startActivity(new Intent(getApplicationContext(), Account.class));
        }
    }

    public void statusHasBeenChanged_Update(Double randUserId){
        String Status = editAboutYou_TextInputEdit.getText().toString().trim();

        boolean b = false;

        try {
            b = dbHelper.updateStatusOnly(Status,randUserId);
        }catch (Exception e){
            System.out.println("Exception: " + e);
        }

        if (b){
            Toast.makeText(this, "Success updating status", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor =  spf.edit();
            editor.putString("Status", Status);
            editor.apply();

        }
    }

}