package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class entrySplash extends AppCompatActivity {
    AppCompatImageButton unlock;
    boolean backPressed = false;

    @Override
    public void onBackPressed(){
        if (backPressed){
            super.onBackPressed();

        }else{
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            backPressed = true;
        }

}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_splash);


        unlock = findViewById(R.id.unlock);
        unlock.setOnLongClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
            return true;
        });

    }
}