package com.example.essaygov4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Initializing ButtonStart variable as a button
    private Button ButtonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ButtonStart onclick Listener =============================================================
        ButtonStart = findViewById(R.id.button_start);
        ButtonStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Sets where to go when the ButtonStart is clicked
                // from MainActivity to ImageActivity
                Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                startActivity(intent);  // starts the Intent | execute the intent : go to the ImageActivity
            }
        });//end of ButtonStart onclick Listener
    }//end of onCreate
}//end of MainActivity