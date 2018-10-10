package com.example.gaominyu.slease.Create;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gaominyu.slease.R;

import java.util.ArrayList;
import java.util.Iterator;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";
    private Button btnTakePhoto;
    private ImageButton btnNext;
    private FrameLayout photoFrame;
    private Camera camera;
    private CameraView cameraView;
    private LinearLayout linearLayout;
    private boolean safeToTakePicture = true;
    private final int maxSize = 9;
    private boolean isCallBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // Request permission to use camera if not allowed in system setting
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                1);

        // Check if this is call back activity by CreateActivity or new activity
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            isCallBack = extras.getBoolean("isCallBack");

        // Object instantiation
        btnTakePhoto = findViewById(R.id.photo_button1);
        photoFrame = findViewById(R.id.photo_frame);
        linearLayout = findViewById(R.id.photo_thumbnails);
        btnNext = findViewById(R.id.photo_button2);

        // Initialize list of images from previous activity
        if(isCallBack) {

            ImageHolder imageHolder = ImageHolder.getSingleton();
            if(!imageHolder.isEmpty()) {
                ArrayList<Bitmap> images = imageHolder.getImages();
                for (Iterator<Bitmap> it = images.iterator(); it.hasNext(); ) {
                    initLinearLayout(it.next());
                }
            }
        }

        // Set button1's text to the remaining number of space available for imageList
        btnTakePhoto.setText(String.valueOf(maxSize - linearLayout.getChildCount()));
        btnTakePhoto.setTextSize(30);

        // Check if button next should be made visible
        if(linearLayout.getChildCount() >0)
            btnNext.setVisibility(View.VISIBLE);

        // Open the camera
        camera = Camera.open();

        // Show a surface view of the camera inside our FrameLayout
        cameraView = new CameraView(this, camera);
        photoFrame.addView(cameraView);

        // Check if max 9 pics is met before allowing another picture to be taken
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This prevents camera crashing from too fast takePicture is being called.
                if (safeToTakePicture) {
                    captureImage(view);
                    safeToTakePicture = false;
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create static instance of ImageHolder class to hold the images temporarily
                ArrayList<Bitmap> images = new ArrayList<>();
                for(int i = 0 ; i < linearLayout.getChildCount(); i++) {
                    FrameLayout frameLayout = (FrameLayout)linearLayout.getChildAt(i);
                    ImageView imageView = (ImageView)frameLayout.getChildAt(0);
                    Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    images.add(bitmap);
                }
                ImageHolder imageHolder = ImageHolder.getSingleton();
                imageHolder.clearHolder();
                imageHolder.setImages(images);

                // Go to new CreateActivity for new lease or Go back to CreateActivity that called this
                if(isCallBack) {
                    setResult(Activity.RESULT_OK, null);
                    finish();
                } else {
                    Intent intent = new Intent(PhotoActivity.this, CreateActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    //call this to take picture
    protected  void captureImage(View view) {
        if(camera != null){
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                    camera.startPreview();

                    // add this image into the list of images taken
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    initLinearLayout(bmp);

                    // Update the button to show how many more pictures can you take
                    int diff = maxSize - linearLayout.getChildCount();
                    btnTakePhoto.setText(String.valueOf(diff));
                    if(diff <= 0) {
                        btnTakePhoto.setAlpha(.5f);
                        btnTakePhoto.setEnabled(false);
                    }

                    // Toggle the btnNext to appear because there is guaranteed content inside now
                    btnNext.setVisibility(View.VISIBLE);

                    // Toggle this safe catch back so that next image can be taken
                    safeToTakePicture = true;
                }
            });
        }
    }

    protected void initLinearLayout(Bitmap bmp) {

        // Append a new framelayout containing an imageView and a cross button in the
        // linearlayout of horizontalscrollview. Its imageView should contain the image
        // that we just took
        final FrameLayout frameLayout = new FrameLayout(getApplicationContext());
        linearLayout.addView(frameLayout);
        final ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 320,426, false));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(160, 160);
        lp.setMargins(10, 20, 10, 0);
        imageView.setLayoutParams(lp);
        frameLayout.addView(imageView);
        Button crossButton = new Button(getApplicationContext());
        crossButton.setBackgroundResource(R.drawable.cross16);
        crossButton.setLayoutParams(new FrameLayout.LayoutParams(40, 40));
        frameLayout.addView(crossButton);

        // Remove this entire framelayout when clicking its containing cross button and
        // delete its image file from imageList
        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeView(frameLayout);
                btnTakePhoto.setEnabled(true);
                btnTakePhoto.setAlpha(1);
                btnTakePhoto.setText(String.valueOf(maxSize - linearLayout.getChildCount()));
                if(linearLayout.getChildCount() <= 0) {
                    btnNext.setVisibility(View.INVISIBLE);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TO DO for previewing each image taken
            }
        });

        // To free up memory space from bitmap decoding
        if(bmp!=null) {
            bmp.recycle();
            bmp = null;
        }
    }
}
