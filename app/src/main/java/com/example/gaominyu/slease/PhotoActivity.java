package com.example.gaominyu.slease;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";
    private FrameLayout photoFrame;
    private Camera camera;
    private CameraView cameraView;
    private LinearLayout linearLayout;
    private List<Bitmap> imageList;
    private boolean safeToTakePicture = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // Object instantiation
        Button btnTakePhoto = findViewById(R.id.photo_button1);
        photoFrame = findViewById(R.id.photo_frame);
        linearLayout = findViewById(R.id.photo_thumbnails);
        imageList = new ArrayList<>(4);

        // Set button1's text to the remaining number of space available for imageList
        btnTakePhoto.setText("9");
        btnTakePhoto.setTextSize(30);

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
    }

    //call this to take picture
    protected  void captureImage(View view) {
        if(camera != null){
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                    camera.startPreview();

                    // Append a new framelayout containing an imageView and a cross button in the linearlayout in horizontalscrollview
                    // Its imageView should contain the image that we just took
                    final FrameLayout frameLayout = new FrameLayout(getApplicationContext());
                    linearLayout.addView(frameLayout);
                    ImageView imageView = new ImageView(getApplicationContext());
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 320,426, false));
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(160, 160);
                    lp.setMargins(10, 20, 10, 0);
                    imageView.setLayoutParams(lp);
                    frameLayout.addView(imageView);
                    Button crossButton = new Button(getApplicationContext());
                    crossButton.setBackgroundResource(R.drawable.cross16);
                    crossButton.setLayoutParams(new FrameLayout.LayoutParams(40, 40));
                    frameLayout.addView(crossButton);

                    // Remove this entire framelayout when clicking its containing cross button and delete its image file from imageList
                    crossButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linearLayout.removeView(frameLayout);
                        }
                    });

                    // Save this image into the imageList so as to pass them later to another activity using intent
                    imageList.add(bmp);
                    if(bmp!=null)
                    {
                        bmp.recycle(); // To free up memory space due to large image decoding
                        bmp=null;
                    }

                    safeToTakePicture = true;
                }
            });
        }
    }
}
