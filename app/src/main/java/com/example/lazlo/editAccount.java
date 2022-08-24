package com.example.lazlo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class editAccount extends AppCompatActivity {
TextInputEditText editUsername_TextInputEdit,editAboutYou_TextInputEdit,editEmail_TextInput;
TextInputLayout editUsername_TextInputLayout,editAboutYou_TextInputLayout,editEmail_TextInputLayout;
DBHelper dbHelper;
SharedPreferences spf;
Double randUserId;
String uname, status, emailAddress;
Cursor success;
ImageView profilePicture;
DrawerLayout editAccountDrawerLayout;
NavigationView editAccountNavigationView;
ActionBarDrawerToggle editAccountActionBarDrawerToggle;

@Override
public boolean onCreateOptionsMenu(@NonNull Menu menu){
    getMenuInflater().inflate(R.menu.edit_account_menu, menu);
    return super.onCreateOptionsMenu(menu);
}

@Override
public  boolean onOptionsItemSelected(@NonNull MenuItem menuItem){
    if (editAccountActionBarDrawerToggle.onOptionsItemSelected(menuItem)){
        onBackPressed();
        return true;
    }else if (menuItem.getItemId() == R.id.saveAccountDetails){
        saveAccountDetails();
    }

    return super.onOptionsItemSelected(menuItem);
}
@Override
public  void onBackPressed(){

}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        editAccountDrawerLayout = findViewById(R.id.editAccountDrawerLayout);
        editAccountActionBarDrawerToggle = new ActionBarDrawerToggle(this,editAccountDrawerLayout,R.string.open_drawer,R.string.close_drawer);

        editAccountDrawerLayout.addDrawerListener(editAccountActionBarDrawerToggle);
        editAccountActionBarDrawerToggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");


        editAccountNavigationView = findViewById(R.id.editAccountNavigationView);
        editAccountNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_account:
                    startActivity(new Intent(getApplicationContext(), editAccount.class));
                    break;
                case R.id.nav_dashboard:
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    break;
                case R.id.nav_timeTracker:
                    startActivity(new Intent(getApplicationContext(),timeTracker.class));
                    break;
                case R.id.nav_addTasks:
                    startActivity(new Intent(getApplicationContext(), AddTasks.class));
                    break;
                case R.id.nav_pendingTasks:
                    startActivity(new Intent(getApplicationContext(), tasks.class));
                    break;
                case R.id.nav_completedTasks:
                    startActivity(new Intent(getApplicationContext(), completed.class));
                    break;
                case R.id.nav_draftTasks:
                    startActivity(new Intent(getApplicationContext(), DraftTasks.class));
                    break;
                case R.id.nav_security:
                    startActivity(new Intent(getApplicationContext(), inHousePasswordReset.class));
                    break;
                case R.id.nav_logout:
                    SharedPreferences prf;
                    prf = getSharedPreferences("user_details", MODE_PRIVATE);
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    SharedPreferences.Editor editor = prf.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(i);
                    break;
            }
                return false;
        });

        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId", null));
        uname = spf.getString("username", null);
        status = spf.getString("Status", null);
        emailAddress = spf.getString("email", null);

        dbHelper = new DBHelper(this);


        profilePicture = findViewById(R.id.profilePicture);

        editUsername_TextInputEdit = findViewById(R.id.editUsername_TextInputEdit);
        editAboutYou_TextInputEdit = findViewById(R.id.editAboutYou_TextInput);

        editUsername_TextInputLayout = findViewById(R.id.editUserName_TextInputLayout);
        editAboutYou_TextInputLayout = findViewById(R.id.editAboutYou_TextInputLayout);

        editEmail_TextInput = findViewById(R.id.editEmail_TextInput);
        editEmail_TextInputLayout = findViewById(R.id.editEmail_TextInputLayout);

        editUsername_TextInputEdit.setText(uname);
        editAboutYou_TextInputEdit.setText(status);
        editEmail_TextInput.setText(emailAddress);


        profilePicture.setOnClickListener(view -> chooseProfilePicture());

        setProfilePicture(randUserId);


    }
    private void setProfilePicture(Double randUserId){
        Cursor profilePictureCursor = null;
        byte[] profilePictureBytes = new byte[0];
        try {
            profilePictureCursor = dbHelper.getProfilePicture(randUserId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (profilePictureCursor != null && profilePictureCursor.moveToFirst()){
            profilePictureBytes = profilePictureCursor.getBlob(profilePictureCursor.getColumnIndexOrThrow("profilePicture"));
            profilePictureCursor.close();
        }
        if (profilePictureCursor != null && !profilePictureCursor.isClosed()) {
            profilePictureCursor.close();
        }
        if (profilePictureBytes != null){
            Bitmap profilePicture2Set = BitmapFactory.decodeByteArray(profilePictureBytes,0,profilePictureBytes.length);
            profilePicture.setImageBitmap(profilePicture2Set);
        }


    }
    private void chooseProfilePicture(){
        Intent profilePic = new Intent();
        profilePic.setType("image/*");
        profilePic.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(profilePic);

    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        if (selectedImageBitmap != null) {
                            selectedImageBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                        }
                        byte[] byteArray = stream.toByteArray();
                        boolean b =  dbHelper.updateProfilePicture(byteArray,randUserId);
                        if (b){
                            profilePicture.setImageBitmap(selectedImageBitmap);
                            Toast.makeText(this, "Upload success", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this, "upload failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
    public void saveAccountDetails() {
        if (!Objects.requireNonNull(editUsername_TextInputEdit.getText()).toString().trim().isEmpty()){
            if (editUsername_TextInputEdit.getText().toString().trim().length() <= 10){
                if (!Objects.requireNonNull(editAboutYou_TextInputEdit.getText()).toString().trim().isEmpty()){
                    if (!Objects.requireNonNull(editEmail_TextInput.getText()).toString().trim().isEmpty()){
                        if (Patterns.EMAIL_ADDRESS.matcher(editEmail_TextInput.getText().toString().trim()).matches()){
                            if (!editAboutYou_TextInputEdit.getText().toString().trim().equals(status)){
                                //status changed
                                editAboutYou_TextInputLayout.setErrorEnabled(false);
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
        String username = Objects.requireNonNull(editUsername_TextInputEdit.getText()).toString().trim();
        String emailAddress = Objects.requireNonNull(editEmail_TextInput.getText()).toString().trim();
        String Status = Objects.requireNonNull(editAboutYou_TextInputEdit.getText()).toString().trim();

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

        }
    }

    public void emailHasChanged_Update(Double randUserId){
        String emailAddress = Objects.requireNonNull(editEmail_TextInput.getText()).toString().trim();

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

        }
    }

    public void userNameHasChanged_Update(Double randUserId){
        String username = Objects.requireNonNull(editUsername_TextInputEdit.getText()).toString().trim();

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

        }
    }

    public void statusHasBeenChanged_Update(Double randUserId){
        String Status = Objects.requireNonNull(editAboutYou_TextInputEdit.getText()).toString().trim();

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