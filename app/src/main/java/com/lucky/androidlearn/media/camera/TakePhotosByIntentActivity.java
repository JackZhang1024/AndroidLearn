package com.lucky.androidlearn.media.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;

import com.lucky.androidlearn.R;

public class TakePhotosByIntentActivity extends ActionBarActivity {

    public static final int TAKE_PHOTO = 1;
    public ImageView ivShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto_camera);
        ivShow = (ImageView) findViewById(R.id.iv_show);
    }

    private String filePath = "";

    public void takephoto(View v) {
        Intent takephotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String CAMERA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/";
        String FILE_NAME = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA));
        String PICTURE_FORMAT = ".png";
        String path = CAMERA_PATH + FILE_NAME + PICTURE_FORMAT;
        filePath = path;
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        takephotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(takephotoIntent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bitmap = convertFile2BitMap(new File(filePath), 2);
                int angle = RotateImageUtil.readPictureDegree(filePath);
                Bitmap rotatedBitmap = RotateImageUtil.rotaingImageView(angle, bitmap);
                ivShow.setImageBitmap(rotatedBitmap);
                //保存旋转之后的图片
                String CAMERA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/";
                String FILE_NAME = "ROTATED_IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA));
                String PICTURE_FORMAT = ".png";
                String path = CAMERA_PATH + FILE_NAME + PICTURE_FORMAT;
                FileOutputStream fos = new FileOutputStream(path);
                rotatedBitmap.compress(CompressFormat.JPEG, 100, fos);
                convertFile2BitMap(new File(path), 2);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    public Bitmap convertFile2BitMap(File file, int sampleSize) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            Options opts = new Options();
            opts.inSampleSize = sampleSize;
            in = new FileInputStream(file);
            return bitmap = BitmapFactory.decodeStream(in, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

}
