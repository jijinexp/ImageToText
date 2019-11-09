// 159336
//Assignment 3: Image To Text Editor
//Student ID:18032679
//Name:Jijin Johney Mundappallil

package com.example.imagetotexteditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.IOException;

// Main Activity was based on the Gallery App from Assignment 2
public class MainActivity extends AppCompatActivity{
    private full_screen_activity activity;
    static final int REQUEST_PERMISSION_KEY = 1;
    private LruCache<String, Bitmap> memoryCache;
    GridView ImageGallery;
    Cursor mCursor;
    ImageAdapter mAdapter;
    int mPosition=0;


    void init() {
        // set cursor to query all images
        final String[] columns = { MediaStore.Images.Media.DATA};
        final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
        mCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        mCursor.moveToFirst();
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }
    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    //getImage Thumbnail corrects orientation and create thumbnail to be displayed on grid_view
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) throws IOException {
        Bitmap bitmap;
        int rotate = 0;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inJustDecodeBounds = false;
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        ExifInterface exif;
        exif = new ExifInterface(imagePath);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        bitmap= Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return bitmap;
    }
    // The adapter, this supplies data to the GridView,
    // select an image in the background using an AsyncTask
    public class ImageAdapter extends BaseAdapter {
        // Holds the image_view and it's position in the list
        class ViewHolder {
            int position;
            ImageView image;
        }
        @Override
        public int getCount() {
            return mCursor.getCount();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @SuppressLint("StaticFieldLeak")
        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder vh;
            if (convertView == null) {
                // if it's not recycled, inflate it from xml
                convertView = getLayoutInflater().inflate(R.layout.image_tab, viewGroup, false);
                // create a new ViewHolder for it
                vh=new ViewHolder();
                vh.image=convertView.findViewById(R.id.image_tab_button);
                // and set the tag to it
                convertView.setTag(vh);
            } else
                vh=(ViewHolder)convertView.getTag(); // otherwise get the view_holder
            // set it's position
            vh.position=i;
            // and erase the image so we don't see old photos
            vh.image.setImageBitmap(null);
            // make an AsyncTask to load the image
            final String imageKey = String.valueOf(i);

            final Bitmap bitmap = getBitmapFromMemCache(imageKey);
            if (bitmap != null) {
                vh.image.setImageBitmap(bitmap);
            } else {
                new AsyncTask<ViewHolder, Void, Bitmap>() {
                    private ViewHolder vh;

                    @Override
                    protected Bitmap doInBackground(ViewHolder... params) {
                        vh = params[0];
                        mCursor.moveToPosition(vh.position);
                        Bitmap bmp = null;
                        try {
                            String projection = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            // vh position might have changed
                            if (vh.position != i)
                                return null;
                            bmp = getImageThumbnail(projection,200,200);
                            addBitmapToMemoryCache(String.valueOf(vh.position), bmp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return bmp;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bmp) {
                        // only set the image_view if the position hasn't changed.
                        if (vh.position == i) {
                            vh.image.setImageBitmap(bmp);
                        }
                    }
                }.execute(vh);//executeOnExecutor(mExecutor,vh);
            }
            return convertView;
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        // save the list position
        mPosition=ImageGallery.getFirstVisiblePosition();
        // close the cursor (will be opened again in init() during onResume())
        mCursor.close();
    }
    @Override
    public void onResume() {
        super.onResume();
        // reinit in case things have changed
        init();
        // set the list position
        ImageGallery.setSelection(mPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        //check permission
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT > 23 && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        } else
            //initialise cursor to get all images
            init();
        ImageGallery = findViewById(R.id.grid_view);

        getSupportActionBar().hide();
        mAdapter = new ImageAdapter();
        ImageGallery.setAdapter(mAdapter);
        ImageGallery.setOnItemClickListener(myOnItemClickListener);


    }


    // listener passes image id clicked on to full_view image activity
    OnItemClickListener myOnItemClickListener = new OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mCursor.moveToPosition(position);
            String prompt = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA ));
            Intent intent = new Intent(getApplicationContext(), full_screen_activity.class);
            intent.putExtra("option","add");
            intent.putExtra("uri", prompt);
            startActivity(intent);

        }};




}
