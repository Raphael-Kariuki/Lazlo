package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

/* added code */

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.EditText;

import android.widget.TextView;
import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import android.os.Bundle;

import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    //variables instantiation
    Double randomUserId;
    EditText username, password;
    MaterialButton btnSubmitLoginCredentials;
    TextView createAccount, forgotPassword;
    DBHelper dbHelper;
    SharedPreferences sharedPreferences;
    TextInputLayout loginUserName_inputLayout, loginPassword_inputLayout;
    String status, emailAddress;


    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize db class
        dbHelper = new DBHelper(this);

        //process input views
        username = findViewById(R.id.loginUserName_input);
        password = findViewById(R.id.loginPassword_input);

        //process layout to be used in setting errors
        loginUserName_inputLayout = findViewById(R.id.loginUserName_inputLayout);
        loginPassword_inputLayout = findViewById(R.id.loginPassword_inputLayout);

        //user name input layout should grab focus so that user can instantly enter input once the activity is rendered
        loginUserName_inputLayout.requestFocus();

        //process login button
        btnSubmitLoginCredentials = findViewById(R.id.btnSubmit_login);

        //create intent to redirect to dashboard activity. created up top as it's required in 2 diff places
        Intent intent = new Intent(Login.this, Dashboard.class);

        /*shared preferences are used to store variables persistently, even after uses closes the app. Only cleared when they logout.
        preferred to global variables as global variables are lost when user closes the app*/

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);


        //check if the sharedPreferences has a username or password. If exists, session still exists, return to that
        if (sharedPreferences.contains("username") && sharedPreferences.contains("password")) {
            startActivity(intent);
        }


        //process login
        btnSubmitLoginCredentials.setOnClickListener(view -> {
            String unameCheck = username.getText().toString().trim();
            String passCheck = password.getText().toString().trim();

            //make a db hit to obtain details by username
            Cursor cursor = dbHelper.getUserDetailsByUserName(unameCheck);
            if (!unameCheck.isEmpty()) {         //if username is non0empty, proceed
                if (!passCheck.isEmpty()) {          //if password is non0empty, proceed
                    if (cursor.getCount() != 0) {        //if cursor is non0empty(Means username exists in db), proceed
                        try {

                            //ensure username and password are tied, meaning they belong to one user, proceed
                            if (loginCheck(cursor, passCheck)) {
                                dbHelper.close();

                                //open and edit sharedPrefs to insert username and randomly created unique userId
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", unameCheck);
                                editor.putString("randomUserId", String.valueOf(randomUserId));
                                editor.putString("Status", status);
                                editor.putString("email", emailAddress);
                                editor.apply();

                                //set the fields to ""
                                username.setText("");
                                password.setText("");

                                //true when there exists tasks which can populate the database
                                if (dbHelper.getTasks(randomUserId) != null && dbHelper.getTasks(randomUserId).getCount() != 0) {
                                    dbHelper.close();
                                    //redirect to dashboard
                                    startActivity(intent);
                                } else {
                                    //redirect to final page to start adding tasks which will populate the dashboard
                                    Intent toFinalPageCozNewUser = new Intent(getApplicationContext(), PendingTasks.class);
                                    startActivity(toFinalPageCozNewUser);
                                }

                            } else {
                                //show error on wrong password
                                loginPassword_inputLayout.setErrorEnabled(true);
                                loginPassword_inputLayout.setError("Wrong password");
                                loginUserName_inputLayout.setErrorEnabled(false);
                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //show error on non-existing username
                        loginUserName_inputLayout.setErrorEnabled(true);
                        loginUserName_inputLayout.setError("Username doesn't exist");
                        loginPassword_inputLayout.setErrorEnabled(false);
                    }
                } else {

                    //show error on blank password
                    loginPassword_inputLayout.setErrorEnabled(true);
                    loginPassword_inputLayout.setError("No blank password");
                    loginUserName_inputLayout.setErrorEnabled(false);
                }
            } else {

                //show error on wrong username
                loginUserName_inputLayout.setErrorEnabled(true);
                loginUserName_inputLayout.setError("No blank username");
                loginPassword_inputLayout.setErrorEnabled(false);
            }
        });


        // process  view that when clicked redirects to create account activity
        createAccount = findViewById(R.id.createAccount2);
        createAccount.setOnClickListener(view -> {
            Intent intent1 = new Intent(Login.this, SignUp.class);
            startActivity(intent1);
        });


        //process view that when clicked redirects to forgot password
        forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(view -> {
            Intent resetPassword = new Intent(getApplicationContext(), com.example.lazlo.forgotPassword.class);
            startActivity(resetPassword);
        });
    }

    //function that checks whether the username entered by user exists in the db, followed by matching entered password
    // with the existing finally contributing to access grant .
    //Import signUp class to use it's crypto module
    public boolean loginCheck(Cursor cursor, String passCheck) throws NoSuchAlgorithmException {

        //import class to access it's crypto function
        SignUp signUp = new SignUp();

        //initialize a boolean so that it can assigned a value in a loop and accessed outside it
        boolean a = false;

        //iterate through the non-null cursor
        if (cursor != null && cursor.moveToNext()) {
            //check whether the value at the password column matches that entered
            if (cursor.getString(cursor.getColumnIndexOrThrow("password")).equals(signUp.crypto(passCheck))) {

                //obtain randomUser Id to be stored in shared preferences
                randomUserId = cursor.getDouble(cursor.getColumnIndexOrThrow("randUserId"));
                status =  cursor.getString(cursor.getColumnIndexOrThrow("Status"));
                emailAddress = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                //close db connection
                cursor.close();


                a = true;
            }
        }

        //return true to signal user is authenticated
        return a;
    }
}