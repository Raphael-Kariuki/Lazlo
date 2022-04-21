package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

/*added code*/
import android.content.Intent;
import android.widget.TextView;
import android.os.Bundle;

public class FinalPage extends AppCompatActivity {
    TextView text;

    @Override
    public void onBackPressed(){
        FinalPage.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);
        text = findViewById(R.id.changeText);
        Intent intent = getIntent();
        String s2 = intent.getStringExtra("email");
        text.setText(s2);
    }
}