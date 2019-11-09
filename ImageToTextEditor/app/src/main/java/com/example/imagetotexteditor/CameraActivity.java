// 159336
//Assignment 3: Image To Text Editor
//Student ID:18032679
//Name:Jijin Johney Mundappallil
package com.example.imagetotexteditor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;


public class CameraActivity extends AppCompatActivity {
    public CropImageView cropCameraImage;
    public static Bitmap croppedImagecamera;
    public static final int pic_id = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cropCameraImage = findViewById(R.id.cropImageView_Camera);
        Button mCameraButton = findViewById(R.id.camera_button);
        getSupportActionBar().hide();
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, pic_id);
            }
        });
        FloatingActionButton fin_cam = findViewById(R.id.camera_next);
        fin_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.camera_next) {
                    cropCameraImage.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
                        @Override
                        public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                            croppedImagecamera= result.getBitmap();
                            Intent intent = new Intent(getApplicationContext(), BinarizeActivity.class);
                            intent.putExtra("camera","on");
                            startActivity(intent);
                        }
                    });
                    cropCameraImage.getCroppedImageAsync();
                }
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String mPath = getImageUri(getApplicationContext(),bitmap);
            cropCameraImage.setImageUriAsync(Uri.parse(mPath));

        }
    }
    public String getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return path;
    }
}
