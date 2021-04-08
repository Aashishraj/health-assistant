package com.example.health;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.health.ml.Generated;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Skin_problem extends AppCompatActivity {
    private static final int CAMERA_PERM_CODE =101 ;
    public static final int CAMERA_REQUEST_CODE = 102;

    Button predict_btn;
    Button gallery_btn;
    Button result_btn;
    public ImageView selectedImage;
    String currentPhotoPath;
    public TextView output;
    public Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_problem);
        selectedImage = (ImageView)findViewById(R.id.disease);
        predict_btn=findViewById(R.id.predict_btn);
        gallery_btn=findViewById(R.id.gallery_btn);
        result_btn=findViewById(R.id.result_btn);
        output=(TextView) findViewById(R.id.output);

        predict_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Context context;
                Toast.makeText(Skin_problem.this, "OPENING CAMERA", Toast.LENGTH_SHORT).show();
                askCameraPermissions();
            }
        });

        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Skin_problem.this, "OPENING GALLERY", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

        result_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Context context;
                Toast.makeText(Skin_problem.this, "Get results.", Toast.LENGTH_SHORT).show();
                image=Bitmap.createScaledBitmap(image,64,64,true);
                try {
                    Generated model = Generated.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 64, 64, 3}, DataType.FLOAT32);


                    TensorImage tensorimage=new TensorImage(DataType.FLOAT32);
                    tensorimage.load(image);
                    ByteBuffer byteBuffer=tensorimage.getBuffer();

                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    Generated.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    // Releases model resources if no longer used.

                    String[] out={"Acne and Rosacea Photos",
                    "Bullous Disease Photos",
                    "Cellulitis Impetigo and other Bacterial Infections",
                   "Eczema Photos",
                    "Melanoma Skin Cancer Nevi and Moles",
                    "Nail Fungus and other Nail Disease",
                    "Seborrheic Keratoses and other Benign Tumors"};




//                    maxmap.put(0,"Acne and Rosacea Photos");
//                    maxmap.put(1,"Bullous Disease Photos");
//                    maxmap.put(2,"Cellulitis Impetigo and other Bacterial Infections");
//                    maxmap.put(3,"Eczema Photos");
//                    maxmap.put(4,"Melanoma Skin Cancer Nevi and Moles");
//                    maxmap.put(5,"Nail Fungus and other Nail Disease");
//                    maxmap.put(6,"Seborrheic Keratoses and other Benign Tumors");

                    String[] maxoutput=new String[3];
                    float[] maxaccuracy=new float[3];
                    HashMap<Float, Integer> maxmapoutput = new HashMap<Float, Integer>();
                    HashMap<Integer, String> maxmap = new HashMap<Integer, String>();
                    for(int i=0;i<out.length;i++) {
                        maxmap.put(i,out[i]);
                    }

                    for(int i=0;i<outputFeature0.getFloatArray().length;i++){
                        maxmapoutput.put(outputFeature0.getFloatArray()[i],i);
                    }

                    float[] arr=outputFeature0.getFloatArray();
                    String all="";
                    for(int i=0;i<arr.length;i++){
                        all+= out[i]+" : = "+arr[i]+"\n";
                    }
                    Log.d("tag3", all);
                    Arrays.sort(arr);
                    int j=0;
                    for(int i=arr.length-1;i>=0;i--){
                        if(j==3)
                            break;
                        maxaccuracy[j]=arr[i];
                        i--;
                        j++;
                    }

                    String answer="";
                    for(int i=0;i<j;i++){
                        maxoutput[i]= maxmap.get(maxmapoutput.get(maxaccuracy[i]));
                        answer+= maxoutput[i] + " = " +  String.valueOf(maxaccuracy[i]) + "\n";
                    }
                    Log.d("tag2", answer);

                    output.setText(answer);
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }
        });
    }

    private void askCameraPermissions() {
        Context context;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
        else
        {
            openCamera();
        }
    }

    private void openCamera() {
        Toast.makeText(this, "Camera has been opened.", Toast.LENGTH_SHORT).show();
        Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode==RESULT_OK)  {
            image = (Bitmap) (data.getExtras().get("data"));
            selectedImage.setImageBitmap(image);
        }

        if(requestCode == 100 && resultCode==RESULT_OK)
        {
            selectedImage.setImageURI(data.getData());

            Uri uri = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            return;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "application.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


}