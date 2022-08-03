package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class editAccount extends AppCompatActivity {
TextInputEditText editUsername_TextInputEdit,editAboutYou_TextInputEdit;
TextInputLayout editUsername_TextInputLayout,editAboutYou_TextInputLayout;
DBHelper dbHelper;
SharedPreferences spf;
Double randUserId;
String uname, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);


        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId", null));
        uname = spf.getString("username", null);
        status = spf.getString("Status", null);

        dbHelper = new DBHelper(this);

        editUsername_TextInputEdit = findViewById(R.id.editUsername_TextInputEdit);
        editAboutYou_TextInputEdit = findViewById(R.id.editAboutYou_TextInput);

        editUsername_TextInputLayout = findViewById(R.id.editUserName_TextInputLayout);
        editAboutYou_TextInputLayout = findViewById(R.id.editAboutYou_TextInputLayout);

        editUsername_TextInputEdit.setText(uname);
        editAboutYou_TextInputEdit.setText(status);





    }
    @Override
    public void onBackPressed(){
        if (!editUsername_TextInputEdit.getText().toString().trim().isEmpty()){
            if (!editAboutYou_TextInputEdit.getText().toString().trim().isEmpty()){

                String username = editUsername_TextInputEdit.getText().toString().trim();
                String Status = editAboutYou_TextInputEdit.getText().toString().trim();

                boolean b = false;

                try {
                    b = dbHelper.updateUserName(username,Status,randUserId);
                }catch (Exception e){
                    System.out.println("Exception: " + e);
                }

                if (b){
                    Toast.makeText(this, "Success updating username", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor =  spf.edit();
                    editor.putString("username", username);
                    editor.putString("Status", Status);
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), myAccount.class));
                }


            }else{
                editUsername_TextInputLayout.setErrorEnabled(false);
                editAboutYou_TextInputLayout.setError("Enter about you");
                editAboutYou_TextInputLayout.setErrorEnabled(true);

            }
        }else {
            editUsername_TextInputLayout.setErrorEnabled(true);
            editUsername_TextInputLayout.setError("Enter your new username");
            editAboutYou_TextInputLayout.setErrorEnabled(false);
        }
    }

}