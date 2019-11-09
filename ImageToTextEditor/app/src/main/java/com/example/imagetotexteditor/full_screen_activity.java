// 159336
//Assignment 3: Image To Text Editor
//Student ID:18032679
//Name:Jijin Johney Mundappallil

package com.example.imagetotexteditor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;



public class full_screen_activity extends AppCompatActivity  {
    public static Bitmap croppedImage;
    public static Uri uriImage;
    public CropImageView cropImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_view);
        getSupportActionBar().hide();
        CropImageDesired(full_screen_activity.this);

        initActivity();
    }
    private void initActivity() {
        // Access the image file and generate URI
        String image_path = getIntent().getExtras().getString("uri");
        File image_file = new File(image_path);
        uriImage = Uri.fromFile(image_file);

        // Set URI image to display
        cropImageView =  findViewById(R.id.cropImageView);
        cropImageView.setImageUriAsync(Uri.parse(uriImage.toString()));

        // floating action button passes the cropped image to next activity using next step
        FloatingActionButton fab = findViewById(R.id.next_step);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.next_step) {
                    cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
                        @Override
                        public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                            croppedImage = result.getBitmap();
                            nextStep();
                        }
                    });
                    cropImageView.getCroppedImageAsync();
                }
            }
        });
    }
    // send to binarize image
    private void nextStep() {
        Intent intent = new Intent(getApplicationContext(), BinarizeActivity.class);
        intent.putExtra("camera","off");
        startActivity(intent);
    }

    private void CropImageDesired(Context context) {
        final CharSequence[] options = { "Ok" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Crop Image for text recognition");

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
