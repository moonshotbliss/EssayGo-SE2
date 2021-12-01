package com.example.essaygov4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageActivity extends AppCompatActivity {


    // Initializing Buttons and imageView (where the chosen image will be displayed)
    private Button ButtonBackToMain, ButtonChooseImg, ButtonContinue;
    private ImageView ImageViewPickedImg;

    // Initializing bitmap_image variable as Bitmap
    private Bitmap bitmap_image = null;

    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;

    String cameraPermission[];
    String storagePermission[];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        ImageViewPickedImg = findViewById(R.id.imageview_pickedimg);



        //Button back to main - onclick listener ===================================================
        ButtonBackToMain = findViewById(R.id.button_backtomain);
        ButtonBackToMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Sets where to go when the ButtonBackToMain is clicked
                // from ImageActivity to MainActivity
                Intent intent = new Intent(ImageActivity.this, MainActivity.class);
                startActivity(intent);  // starts the Intent | execute the intent : go to the MainActivity
            }
        });// end of button back to main - onclick listener



        //Button Choose Image - onclick listener ===================================================
        ButtonChooseImg = findViewById(R.id.button_chooseimg);
        ButtonChooseImg.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int picd = 0;
                if(picd == 0){
                    if(!checkCameraPermission()){   // to check if camera does not have permission yet
                        requestCameraPermission();  // if not.... ask for permission
                    }else{                          // if the camera already has permission...
                        pickFromGallery();          // call the function pickFromGallery
                    }
                }else if(picd == 1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }
            }
        });// end of button choose image - onclick listener



        //Button Continue - onclick Listener =======================================================
        ButtonContinue = findViewById(R.id.button_continue);
        ButtonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap_image == null){  // checking if the bitmap_image is null
                                            // if null or empty... a message will pop up
                    Toast.makeText(ImageActivity.this,"Error! No Image Found.", Toast.LENGTH_LONG).show();
                }else{
                    // Convert to byte array
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap_image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();


                    Intent intent = new Intent(ImageActivity.this, OutputActivity.class);
                    intent.putExtra("image",byteArray);
                    startActivity(intent);
                }
            }
        });// end of button continue - onclick listener
    }// end of onCreate





    // start of checkCameraPermission ==============================================================
    private boolean checkCameraPermission() {
        // to check for the camera permission
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        // to check for the storage permission
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;   // returns boolean value
    }// end of checkCameraPermission

    @RequiresApi(api = Build.VERSION_CODES.M)
    // Function to Request Camera Permission
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }





    // start of checkStoragePermission =============================================================
    private boolean checkStoragePermission() {
        // to check for the storage permission
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;  // returns boolean value
    }// end of checkStoragePermission

    @RequiresApi(api = Build.VERSION_CODES.M)
    // Function to Request Storage Permission
    private void requestStoragePermission() { requestPermissions(storagePermission, STORAGE_REQUEST); }





    private void pickFromGallery() {
        CropImage.activity().start(this);
    }





    // start of onActivityResult ===================================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                try {
                    // Converts the imageUri to bitmap
                    bitmap_image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    // Sets/Display the selected image (bitmap image) to the imageView
                    ImageViewPickedImg.setImageBitmap(bitmap_image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }// end of onActivityResult





    // start of onRequestPermissionsResult =========================================================
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if(grantResults.length > 0){
                    boolean camera_accepted = grantResults[0]==(PackageManager.PERMISSION_GRANTED);
                    boolean storage_accepted = grantResults[1]==(PackageManager.PERMISSION_GRANTED);

                    if(camera_accepted && storage_accepted){
                        pickFromGallery();
                    }else{
                        // If camera & storage permission is not Enabled...
                        // ...a message will pop up
                        Toast.makeText(this, "Please Enable Camera & Storage Permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST:{
                if(grantResults.length > 0){
                    boolean storage_accepted = grantResults[0]==(PackageManager.PERMISSION_GRANTED);
                    if(storage_accepted){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "Please enable Storage Permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }// end of onRequestPermissionsResult
}// end of ImageActivity