package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

/*added code*/
import android.content.Intent;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.os.Bundle;

public class FinalPage extends AppCompatActivity {
    //TextView text;
    TextView hamburger_menu;
    @Override
    public void onBackPressed(){
        FinalPage.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);
        /* text = findViewById(R.id.changeText);
        Intent intent = getIntent();
        String s2 = intent.getStringExtra("email");
        text.setText(s2); */

        hamburger_menu = findViewById(R.id.hamburger_menu);
        hamburger_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(FinalPage.this, myAccount.class);
                startActivity(a);
            }
        });
    }
}