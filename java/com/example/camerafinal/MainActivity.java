package com.example.camerafinal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    private FaceDetector faceDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Face Detector
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();
        faceDetector = FaceDetection.getClient(options);

        Button openCameraButton = findViewById(R.id.open_camera_button);
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    openCamera();
                } else {
                    requestCameraPermission();
                }
            }
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                processImage(imageBitmap);
            }
        }
    }

    private void processImage(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        faceDetector.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<com.google.mlkit.vision.face.Face>>() {
                    @Override
                    public void onSuccess(List<com.google.mlkit.vision.face.Face> faces) {
                        if (faces.size() > 0) {
                            com.google.mlkit.vision.face.Face face = faces.get(0);
                            float smileProb = face.getSmilingProbability();
                            if (smileProb >= 0.5) {
                                tellHappyJoke();
                            } else {
                                tellSadJoke();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "No face detected", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error processing image", Toast.LENGTH_SHORT).show();
                });
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void tellHappyJoke() {
        String[] happyJokes = {
                "Why did the tomato turn red? Because it saw the salad dressing!",
                "Where do cows go on dates? To the moo-vies",
                "How can you tell if a plant is good at math? It has square roots.",
                "What did one plate say to the other plate? Dinner's on me!",
                "What did the tree say when spring arrived? What a re-leaf!"
        };


        // Randomly select a joke from the array
        int randomIndex = new Random().nextInt(happyJokes.length);
        String selectedJoke = happyJokes[randomIndex];

        // Display the selected joke
        Toast.makeText(this, "You're Happy", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, selectedJoke, Toast.LENGTH_LONG).show();
    }

    private void tellSadJoke() {
        String[] sadJokes = {
                "Why did the tree cry? Because problems growing from roots",
                "Why was the math book sad? Because had too many problems!",
                "Did you hear about the broken pencil?! It was pointless",
                "What did the ocean say to the beach? Nothing. It just waved",
                "Why lemon break up with the lime?Because it found someone bitter"
        };
        // Randomly select a joke from the array
        int randomIndex = new Random().nextInt(sadJokes.length);
        String selectedJoke = sadJokes[randomIndex];

        // Display the selected joke
        Toast.makeText(this, "You're Sad", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, selectedJoke, Toast.LENGTH_LONG).show();

    }
}
