package com.lucky.androidlearn.media.camera;


import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import com.lucky.androidlearn.R;

/**
 * 此类是进行拍照的例子
 *
 * @author administor
 */
public class TakePhotosByCameraActivity extends Activity {
    private SurfaceView surfaceview;
    private SurfaceHolder surfaceholder;
    private int screenwidth, screenheight;
    private Camera camera;
    private boolean ispreview = false;//判断是否处于预览状态
    private WindowManager windowmanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_takephotos);
//		takePhotos();
        takePhotosByIntent();
    }

    public void takePhotosByIntent() {
        //拍照 REQUEST_CODE_TAKE_PICTURE 为返回的标识    Mms.ScrapSpace.CONTENT_URI
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //"android.media.action.IMAGE_CAPTURE";
        intent.putExtra("out", Uri.parse("content://mms/scrapSpace")); // output,Uri.parse("content://mms/scrapSpace");
        startActivityForResult(intent, 1);
    }

    @SuppressWarnings("deprecation")
    public void takePhotos() {
        //获取窗口管理器
        windowmanger = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowmanger.getDefaultDisplay();
        //获取屏幕的宽和高
        screenwidth = display.getWidth();
        screenheight = display.getHeight();
        //获取界面中的surfaceView
        surfaceview = (SurfaceView) findViewById(R.id.surfaceView1);
        surfaceholder = surfaceview.getHolder();
        surfaceholder.addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                if (camera != null) {
                    if (ispreview) {
                        camera.stopPreview();
                        camera.release();
                        camera = null;
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                initCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                // TODO Auto-generated method stub
            }
        });
        surfaceholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (camera != null && event.getRepeatCount() == 0) {
                    //拍摄照片
                    camera.takePicture(null, null, myjpegcallback);
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    PictureCallback myjpegcallback = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            //将所拍摄的图片保存到外部存储目录下
            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            //加载Activity_takephotos_save布局资源
            View view = getLayoutInflater().inflate(R.layout.activity_takephotos_save, null);
            //找到布局上的editText和imageview
            final EditText edittext = (EditText) view.findViewById(R.id.path_edit);
            ImageView imageview = (ImageView) view.findViewById(R.id.imageview_show);
            System.out.println("---->" + imageview);
            //显示刚才拍摄的图片
            imageview.setImageBitmap(bitmap);
            //用对话框显示组件
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(TakePhotosByCameraActivity.this);
            builder.setView(view);
            builder.setPositiveButton("点击保存", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        File dirPath = Environment.getExternalStorageDirectory();
                        System.out.println("-->" + dirPath.getAbsolutePath());
                        File file = new File(dirPath, edittext.getText().toString().trim() + ".jpg");
                        //设置图片的输出流对象
                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap.compress(CompressFormat.JPEG, 100, fos);
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("取消保存", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }

            });
            builder.create().show();
            camera.stopPreview();
            camera.startPreview();
            ispreview = true;
        }
    };

    @SuppressWarnings("deprecation")
    protected void initCamera() {
        if (!ispreview) {
            camera = Camera.open();
        }
        if (!ispreview && camera != null) {//如果不是在预览,同时camera 不是空
            try {
                //当获取一个对象的属性的时候，可以试试对象的静态方法，或者是对象，挨个点
                Camera.Parameters parameters = camera.getParameters();
                //设置图片的预览大小
                parameters.setPreviewSize(screenwidth, screenheight - 100);
                //每秒显示四帧
                parameters.setPreviewFrameRate(4);
                //设置图片的格式
                parameters.setPictureFormat(PixelFormat.JPEG);
                //设置图片的质量
                parameters.set("jpeg-quality", 95);
                //设置图片的大小
                parameters.setPictureSize(screenwidth, screenheight - 100);
                //给Camera设置属性
                camera.setParameters(parameters);
                camera.setPreviewDisplay(surfaceholder);
                //开始预览
                camera.startPreview();
                //自动对焦
                camera.autoFocus(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ispreview = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
        System.exit(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        System.out.println("" + uri.toString());
    }
}
