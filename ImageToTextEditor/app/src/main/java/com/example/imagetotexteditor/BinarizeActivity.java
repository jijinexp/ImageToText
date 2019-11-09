// 159336
//Assignment 3: Image To Text Editor
//Student ID:18032679
//Name:Jijin Johney Mundappallil
package com.example.imagetotexteditor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.googlecode.leptonica.android.GrayQuant;
import com.googlecode.leptonica.android.Pix;
import java.util.List;


public class BinarizeActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    public static Bitmap crpbitmap;
    private ImageView img;
    private Pix pix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bin);
        getSupportActionBar().hide();
        final Intent cIntent = getIntent();
        final String mMode = cIntent.getStringExtra("camera");
        img = findViewById(R.id.croppedImage);
        //Checks if the mode is camera or the user has chosen a gallery image
        if(mMode.equals("on")) {

            pix = com.googlecode.leptonica.android.ReadFile.readBitmap(CameraActivity.croppedImagecamera);
        }else {
            pix = com.googlecode.leptonica.android.ReadFile.readBitmap(full_screen_activity.croppedImage);
        }
               // Invoking otsu method to binarize image
        TextOptimizer textOptimizer = new TextOptimizer();
        int threshold = textOptimizer.scaleImage(pix.getData());
        // Increasing value of threshold is better
        threshold += 20;
        crpbitmap = com.googlecode.leptonica.android.WriteFile.writeBitmap
                (GrayQuant.pixThresholdToBinary(pix, threshold));
        img.setImageBitmap(crpbitmap);
        // Seekbar for tuning image
        AppCompatSeekBar seekBar = findViewById(R.id.umbralization);
        // show dialog
        TuneImageDialog(BinarizeActivity.this);
        seekBar.setProgress((50 * threshold) / 254);
        seekBar.setOnSeekBarChangeListener(this);
        Button fab = findViewById(R.id.process_text);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.process_text) {
                    // firebaseImage Text recognition function initilaized
                    detectText();


                }
            }
        });

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    //@Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    //@Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        crpbitmap = com.googlecode.leptonica.android.WriteFile.writeBitmap(
                GrayQuant.pixThresholdToBinary(pix, ((254 * seekBar.getProgress()) / 50)));
        img.setImageBitmap(crpbitmap);
    }
    public void detectText(){
        if (crpbitmap==null){
            Toast.makeText(getApplicationContext(), "Bitmap non-existent",Toast.LENGTH_LONG).show();
        }else{
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(crpbitmap);
            FirebaseVisionTextRecognizer firebaseVisionTextRecognizer= FirebaseVision.getInstance().getOnDeviceTextRecognizer();
            firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(
                    new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {

                            processTextRecognitionResult(firebaseVisionText);
                        }
                    }
            ).addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }

    }
    private void processTextRecognitionResult(FirebaseVisionText Text){
        String text;
        StringBuilder sb1 = new StringBuilder();
        List<FirebaseVisionText.TextBlock> blocks=Text.getTextBlocks();
        // if no text found
        if(blocks.size()==0){
            Toast.makeText(getApplicationContext(),"Not text found",Toast.LENGTH_LONG).show();
            return;
        }
        // gets all the text blocks and create a string of the output using StringBuilder
        for (FirebaseVisionText.TextBlock block:Text.getTextBlocks()){
            text =block.getText();
            sb1.append(text);
        }
        Intent txtIntent = new Intent(getApplicationContext(), EditorActivity.class);
        // Sends option as new add and the text to be set
        txtIntent.putExtra("option","add");
        txtIntent.putExtra("message",sb1.toString());
        startActivity(txtIntent);

    }
    private void TuneImageDialog(Context context) {
        final CharSequence[] options = { "Ok" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enhance Image using Seek Bar and press Process Text for Output");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Ok")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

}
