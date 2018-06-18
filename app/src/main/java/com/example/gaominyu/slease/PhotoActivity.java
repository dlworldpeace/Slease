package com.example.gaominyu.slease;

import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.Duration;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";
    FrameLayout photoFrame;
    Camera camera;
    CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // Object instantiation
        Button btnTakePhoto = findViewById(R.id.photo_button1);
        photoFrame = findViewById(R.id.photo_frame);

        // Open the camera
        camera = Camera.open();

        // Show a surface view of the camera inside our FrameLayout
        cameraView = new CameraView(this, camera);
        photoFrame.addView(cameraView);

        // Check if max 9 pics is met before allowing another picture to be taken
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage(view);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 0);
            }
        });
    }

    public  void captureImage(View view) {
        if(camera != null){
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                    // Save this picture to the page's list of thumbnails
                    Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_LONG).show();

                    // Restart camera after taking one picture
                    camera.startPreview();
                }
            });
        }
    }
}
