package com.example.skintest;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skintest.ml.Mdl;
import com.example.skintest.ml.Mdll;
import com.example.skintest.ml.Model;
import com.example.skintest.ml.Modell;
import com.example.skintest.ml.Modelll;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

    Button camera, gallery;
    ImageView imageView;
    TextView result;

    TextView confidenceScore;

    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = findViewById(R.id.button);
        gallery = findViewById(R.id.button2);

        imageView = findViewById(R.id.imageView);
        result = findViewById(R.id.result);

        confidenceScore = findViewById(R.id.confidenceScore);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
    }

    public void classifyImage(Bitmap image) {
        try {
//            Modell model = Modell.newInstance(getApplicationContext());
//
//            // Creates inputs for reference.
//            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 32, 32, 3}, DataType.FLOAT32);
//            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
//            byteBuffer.order(ByteOrder.nativeOrder());
//
//            int[] intValues = new int[imageSize * imageSize];
//            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
//
//            int pixel = 0;
//
//            for(int i = 0; i < imageSize; i++) {
//                for(int j = 0; j < imageSize; j++) {
//                    int val = intValues[pixel++]; // RGB
//                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
//                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
//                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
//                }
//            }
//
//            inputFeature0.loadBuffer(byteBuffer);
//
//            // Runs model inference and gets result.
//            Modell.Outputs outputs = model.process(inputFeature0);
//            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
//            float[] confidences = outputFeature0.getFloatArray();
//
//            result.setText("" + confidences.length + confidences[0] + confidences[1] + confidences[2]);
//
//            // find the index of the class with biggest confidence
//            int maxPos = 0;
//            float maxConfidence = 0;
//            for(int i = 0; i < confidences.length; i++) {
//                if(confidences[i] > maxConfidence) {
//                    maxConfidence = confidences[i];
//                    maxPos = i;
//                }
//            }
//
//            String[] classes = {"Benign", "Malignant", "Unknown"};
//            //result.setText(classes[maxPos]);
//            // Releases model resources if no longer used.
//            model.close();
//        } catch (IOException e) {
//            // TODO Handle the exception
//        }

            Mdll model = Mdll.newInstance(getApplicationContext()); // model name to change here

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3},
                    DataType.FLOAT32); // image size to declare here according to android import
            // model code suggestion
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(  4 * imageSize * imageSize * 3);
            // image size to declare according according to declared inputFeature0 size
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Mdll.Outputs outputs = model.process(inputFeature0); // model name to change here
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.

            Log.e("confidences[0]", Float.toString(confidences[0])); // to check the
            // size of
            Log.e("confidences[1]", Float.toString(confidences[1])); // to check the size of
            // confidences in console

            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Malignant", "Benign"};
            result.setText(classes[maxPos]);
            confidenceScore.setText(Float.toString(maxConfidence));

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == 3) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            } else {
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}