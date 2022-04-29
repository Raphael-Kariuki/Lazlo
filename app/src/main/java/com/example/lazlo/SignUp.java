package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
/* added code */
import android.content.Intent;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
/**/
import android.os.Bundle;

public class SignUp extends AppCompatActivity {

    EditText username, email, password, confirmPassword;
    TextView switch2login;
    MaterialButton btnSignUp;
    DBHelper dbHelper;
    boolean b;
    TextInputLayout SignupUsername_inputLayout,SignupEmail_inputLayout,SignupPassword_inputLayout,SignupPasswordConfirm_inputLayout;
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

        SignupUsername_inputLayout = (TextInputLayout) findViewById(R.id.SignupUsername_inputLayout);
        SignupEmail_inputLayout = (TextInputLayout) findViewById(R.id.SignupEmail_inputLayout);
        SignupPassword_inputLayout = (TextInputLayout) findViewById(R.id.SignupPassword_inputLayout);
        SignupPasswordConfirm_inputLayout = (TextInputLayout) findViewById(R.id.SignupPasswordConfirm_inputLayout);

        SignupUsername_inputLayout.requestFocus();

        btnSignUp.setOnClickListener(view -> {
            String username1 = username.getText().toString().trim();
            String email1 = email.getText().toString().trim();
            String password1 = password.getText().toString().trim();
            String password2 = confirmPassword.getText().toString().trim();
            if(!username1.isEmpty()){
                if (!email1.isEmpty()){
                    if (Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
                        if (!password1.isEmpty()){
                            if (!password2.isEmpty()){
                                if (password1.equals(password2)){
                                    try {
                                       b = dbHelper.insertUserData(username1,email1,password1);

                                       if (b){
                                            Toast.makeText(SignUp.this,"User created",Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(SignUp.this, Login.class);
                                            startActivity(i);
                                       }else {
                                            Toast.makeText(SignUp.this, "Failed to insert the data", Toast.LENGTH_SHORT).show();
                                       }
                                    }catch (Exception e){
                                        Toast.makeText(SignUp.this,"Unique values error",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    SignupEmail_inputLayout.setErrorEnabled(false);
                                    SignupUsername_inputLayout.setErrorEnabled(false);
                                    SignupPassword_inputLayout.setErrorEnabled(false);
                                    SignupPasswordConfirm_inputLayout.setErrorEnabled(true);
                                    SignupPasswordConfirm_inputLayout.setError("Passwords don't match");

                                    //Toast.makeText(SignUp.this,"passwords don't match",Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                SignupEmail_inputLayout.setErrorEnabled(false);
                                SignupUsername_inputLayout.setErrorEnabled(false);
                                SignupPassword_inputLayout.setErrorEnabled(false);
                                SignupPasswordConfirm_inputLayout.setErrorEnabled(true);
                                SignupPasswordConfirm_inputLayout.setError("Password2 can't be blank");
                                //Toast.makeText(SignUp.this,"Password2 missing ",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            SignupEmail_inputLayout.setErrorEnabled(false);
                            SignupUsername_inputLayout.setErrorEnabled(false);
                            SignupPassword_inputLayout.setErrorEnabled(true);
                            SignupPassword_inputLayout.setError("Missing password entry");
                            SignupPasswordConfirm_inputLayout.setErrorEnabled(false);
                            //Toast.makeText(SignUp.this,"Password1 can't be blank",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        SignupEmail_inputLayout.setErrorEnabled(true);
                        SignupEmail_inputLayout.setError("Incorrect email format");
                        SignupUsername_inputLayout.setErrorEnabled(false);
                        SignupPassword_inputLayout.setErrorEnabled(false);
                        SignupPasswordConfirm_inputLayout.setErrorEnabled(false);
                        //Toast.makeText(SignUp.this,"Incorrect email format",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    SignupEmail_inputLayout.setErrorEnabled(true);
                    SignupEmail_inputLayout.setError("Email can't be blank");
                    SignupUsername_inputLayout.setErrorEnabled(false);
                    SignupPassword_inputLayout.setErrorEnabled(false);
                    SignupPasswordConfirm_inputLayout.setErrorEnabled(false);
                    //Toast.makeText(SignUp.this,"Email can't be blank",Toast.LENGTH_SHORT).show();
                }
            }else{
                SignupUsername_inputLayout.setErrorEnabled(true);
                SignupUsername_inputLayout.setError("user name can't be blank");
                SignupEmail_inputLayout.setErrorEnabled(false);
                SignupPassword_inputLayout.setErrorEnabled(false);
                SignupPasswordConfirm_inputLayout.setErrorEnabled(false);


                //Toast.makeText(SignUp.this,"user name can't be blank",Toast.LENGTH_SHORT).show();
            }



        });
        switch2login = findViewById(R.id.Switch2login);
        switch2login.setOnClickListener(view -> {
            Intent i = new Intent(SignUp.this, Login.class);
            startActivity(i);
        });
    }
}