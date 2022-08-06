package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
/* added code */
import android.content.Intent;
import android.database.Cursor;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
/**/
import android.os.Bundle;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    EditText username, email, password, confirmPassword;
    TextView switch2login;
    MaterialButton btnSignUp;
    DBHelper dbHelper;
    boolean b;
    TextInputLayout SignupUsername_inputLayout,SignupEmail_inputLayout,SignupPassword_inputLayout,SignupPasswordConfirm_inputLayout;

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //process the user input views
        username = findViewById(R.id.SignupUsername_input);
        email = findViewById(R.id.SignupEmail_input);
        password = findViewById(R.id.SignupPassword_input);
        confirmPassword = findViewById(R.id.SignupPasswordConfirm_input);
        btnSignUp = findViewById(R.id.btnSignUp_signUpPage);

        //initialize the db class to be used in db transactions
        dbHelper = new DBHelper(this);



        //process layout to be used in setting errors
        SignupUsername_inputLayout = findViewById(R.id.SignupUsername_inputLayout);
        SignupEmail_inputLayout = findViewById(R.id.SignupEmail_inputLayout);
        SignupPassword_inputLayout = findViewById(R.id.SignupPassword_inputLayout);
        SignupPasswordConfirm_inputLayout = findViewById(R.id.SignupPasswordConfirm_inputLayout);

        //user name input layout should grab focus so that user can instantly enter input once the activity is rendered
        SignupUsername_inputLayout.requestFocus();





        //realtime checking of whether the password entered meets the required length of 8 on focus change
        password.setOnFocusChangeListener((view, hasFocus) -> {
            if (!username.getText().toString().trim().isEmpty() && !email.getText().toString().trim().isEmpty() && !hasFocus){
                if (password.getText().toString().trim().length() < 8){
                    SignupPassword_inputLayout.setCounterEnabled(true);
                    SignupPassword_inputLayout.setErrorEnabled(true);
                    SignupPassword_inputLayout.setError("Password must be at-least 8 characters long");
                }

            }else{
                SignupPassword_inputLayout.setErrorEnabled(false);
                SignupPassword_inputLayout.setCounterEnabled(false);
            }
        });


        //process sign up
        btnSignUp.setOnClickListener(view -> {



            //import class with common methods
            houseOfCommons commons = new houseOfCommons();
            Double randomUserId = commons.generateRandomId();

            String username1 = username.getText().toString().trim();
            String email1 = email.getText().toString().trim();
            String password1 = password.getText().toString().trim();
            String password2 = confirmPassword.getText().toString().trim();


            if(!username1.isEmpty()){ //if username is not empty, proceed
                if (!email1.isEmpty()){     //if email is not empty, proceed
                    if (Patterns.EMAIL_ADDRESS.matcher(email1).matches()){      //if email input matches a email syntax "**@**.**", proceed
                        if (!emailExists(email1)){
                            SignupUsername_inputLayout.setErrorEnabled(false);
                            SignupEmail_inputLayout.setErrorEnabled(false);
                            SignupPassword_inputLayout.setErrorEnabled(false);
                            SignupPasswordConfirm_inputLayout.setErrorEnabled(false);
                        if (!password1.isEmpty()){      //if pass 1 is not empty, proceed
                            if (!password2.isEmpty()){      //if pass 2 is not empty, proceed
                                if (password1.equals(password2)){       //if pass 1 matches pass 2, proceed
                                    if (passwordCheck(password1)){      //run password against function that checks that the password is in the required format
                                        if (!isUserNameExist(username1)){
                                            SignupUsername_inputLayout.setErrorEnabled(false);
                                            SignupEmail_inputLayout.setErrorEnabled(false);
                                            SignupPassword_inputLayout.setErrorEnabled(false);
                                            SignupPasswordConfirm_inputLayout.setErrorEnabled(false);

                                                try {
                                                    //initialize the db insert function, return true if successful and otherwise
                                                    b = dbHelper.insertUserData(username1,randomUserId,email1,crypto(password1),null);
                                                }catch (Exception e){
                                                    Toast.makeText(SignUp.this, "Database error", Toast.LENGTH_SHORT).show();
                                                }

                                                if (b){
                                                    //if true inform user then redirect to the login page
                                                    Toast.makeText(SignUp.this,"Account created",Toast.LENGTH_SHORT).show();

                                                    Intent i = new Intent(SignUp.this, Login.class);
                                                    startActivity(i);
                                                }else {
                                                    //if db insert fails for completedTaskCreationDate reason or the other inform user
                                                    Toast.makeText(SignUp.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                                                }

                                        }else{

                                            //show error on non-existing username
                                            SignupUsername_inputLayout.setErrorEnabled(true);
                                            SignupUsername_inputLayout.setError("user name exists");
                                            SignupEmail_inputLayout.setErrorEnabled(false);
                                            SignupPassword_inputLayout.setErrorEnabled(false);
                                            SignupPasswordConfirm_inputLayout.setErrorEnabled(false);
                                        }
                                    }else{

                                        //show error on improperly setup password
                                        SignupEmail_inputLayout.setErrorEnabled(false);
                                        SignupUsername_inputLayout.setErrorEnabled(false);
                                        SignupPasswordConfirm_inputLayout.setErrorEnabled(false);
                                        SignupPassword_inputLayout.setErrorEnabled(true);
                                        SignupPassword_inputLayout.setError("Passwords must contain uppercase. lowercase, symbols and numbers");
                                    }
                                }else{

                                    //show error on non-matching passwords
                                    SignupEmail_inputLayout.setErrorEnabled(false);
                                    SignupUsername_inputLayout.setErrorEnabled(false);
                                    SignupPassword_inputLayout.setErrorEnabled(false);
                                    SignupPasswordConfirm_inputLayout.setErrorEnabled(true);
                                    SignupPasswordConfirm_inputLayout.setError("Passwords don't match");
                                }
                            }else{

                                //show error blank confirm password
                                SignupEmail_inputLayout.setErrorEnabled(false);
                                SignupUsername_inputLayout.setErrorEnabled(false);
                                SignupPassword_inputLayout.setErrorEnabled(false);
                                SignupPasswordConfirm_inputLayout.setErrorEnabled(true);
                                SignupPasswordConfirm_inputLayout.setError("Password2 can't be blank");
                            }
                        }else{

                            //show error on blank password
                            SignupEmail_inputLayout.setErrorEnabled(false);
                            SignupUsername_inputLayout.setErrorEnabled(false);
                            SignupPassword_inputLayout.setErrorEnabled(true);
                            SignupPassword_inputLayout.setError("Missing password entry");
                            SignupPasswordConfirm_inputLayout.setErrorEnabled(false);
                        }
                        }else{

                            //show error on non-existing email address
                            SignupEmail_inputLayout.setErrorEnabled(true);
                            SignupEmail_inputLayout.setError("Email exists");
                            SignupUsername_inputLayout.setErrorEnabled(false);
                            SignupPassword_inputLayout.setErrorEnabled(false);
                            SignupPasswordConfirm_inputLayout.setErrorEnabled(false);
                        }
                        }else{

                        //show error on an improper email address
                        SignupEmail_inputLayout.setErrorEnabled(true);
                        SignupEmail_inputLayout.setError("Incorrect email format");
                        SignupUsername_inputLayout.setErrorEnabled(false);
                        SignupPassword_inputLayout.setErrorEnabled(false);
                        SignupPasswordConfirm_inputLayout.setErrorEnabled(false);
                        }
                        }else {

                        //show error on missing/blank email address
                        SignupEmail_inputLayout.setErrorEnabled(true);
                        SignupEmail_inputLayout.setError("Email can't be blank");
                        SignupUsername_inputLayout.setErrorEnabled(false);
                        SignupPassword_inputLayout.setErrorEnabled(false);
                        SignupPasswordConfirm_inputLayout.setErrorEnabled(false);
                        }
                        }else{

                        //show error on a blank/missing username
                        SignupUsername_inputLayout.setErrorEnabled(true);
                        SignupUsername_inputLayout.setError("user name can't be blank");
                        SignupEmail_inputLayout.setErrorEnabled(false);
                        SignupPassword_inputLayout.setErrorEnabled(false);
                        SignupPasswordConfirm_inputLayout.setErrorEnabled(false);
            }
        });

        //process view that when clicked redirects to the login page
        switch2login = findViewById(R.id.Switch2login);
        switch2login.setOnClickListener(view -> {
            Intent i = new Intent(SignUp.this, Login.class);
            startActivity(i);
        });
    }


    //function to ensure a secure password is set matching the specified regex
    //Regex is as follows:
        /*Must contain a digit in the range 0-9
        Must contain uppercase and lowercase characters
        Must contain a symbols
        Must be between 8 and 20 characters
        * */
    public boolean passwordCheck(String passphrase){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_=+<>?.;,:'|/`]).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }



    /*
    Function to encrypt the user provided password
    SHA-512 is in play
    The user provided password is converted to bytes, encrypted with sha-512 then converted to hex
    * */
    public String crypto(String passphrase) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] getBytes = md.digest(passphrase.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : getBytes){
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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

    //function to check whether the email entered exists so as to provide a feedback to the user which would otherwise not be visible.
    //Called when the signup button is pressed.
    public boolean emailExists(String email){
        Cursor success = null;
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