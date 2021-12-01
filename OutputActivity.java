package com.example.essaygov4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class OutputActivity extends AppCompatActivity {

    // Initializing buttons and textView variables
    private Button ButtonBackToImage, ButtonCopy;
    private TextView TextViewOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);


        TextViewOutput = findViewById(R.id.textView_output);

        if (! Python.isStarted()) {//this will create python
            Python.start(new AndroidPlatform(this));
        }

        //create python instance
        Python py = Python.getInstance();
        //create python object
        PyObject pyobj = py.getModule("helloworld");  //give the name of python file

        //call python function
        PyObject functionobj = pyobj.callAttr("main");

        //set returned Text to TextView
        TextViewOutput.setText(functionobj.toString());



        //Button back to image - onclick listener ===================================================
        ButtonBackToImage = findViewById(R.id.button_backtoimage);
        ButtonBackToImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Sets where to go when the ButtonBackToImage is clicked
                // from OutputActivity to ImageActivity
                Intent intent = new Intent(OutputActivity.this, ImageActivity.class);
                startActivity(intent);  // starts the Intent | execute the intent : go to the ImageActivity
            }
        });// end of button back to image - onclick listener



        //button to Copy the text in the TextView ==================================================
        ButtonCopy = findViewById(R.id.button_copy);
        ButtonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ask for the clipboard manager service
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                // ...will take the text/string displayed in the textView
                ClipData clipData = ClipData.newPlainText("TextView", TextViewOutput.getText().toString());

                assert clipboardManager != null;
                clipboardManager.setPrimaryClip(clipData);

                // a popup message to indicated the successful copy of the text
                Toast.makeText(OutputActivity.this,"Copied", Toast.LENGTH_LONG).show();
            }
        });// end of ButtonCopy - onclick listener
    }// end of onCreate
}// end of OutputActivity