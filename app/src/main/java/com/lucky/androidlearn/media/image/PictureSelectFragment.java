package com.lucky.androidlearn.media.image;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.jingewenku.abrahamcaijin.commonutil.AppPhoneMgr;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.kevin.crop.UCrop;
import com.lucky.androidlearn.BuildConfig;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.widget.common.helper.AlertDialogHelper;
import com.lucky.androidlearn.widget.common.helper.BottomDialogHelper;

import java.io.File;
import java.io.IOException;

public abstract class PictureSelectFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 0;   // 相册选图标记
    private static final int CAMERA_REQUEST_CODE = 1;    // 相机拍照标记
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    // 拍照临时图片
    private String mTempPhotoPath;
    // 剪切后图像文件
    private Uri mDestinationUri;
    private Activity mActivity;
    private BottomDialogHelper bottomDialogHelper;
    /**
     * 图片选择的监听回调
     */
    private OnPictureSelectedListener mOnPictureSelectedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mTempPhotoPath = Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "photo.png";
        initBottomSheetDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void takePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            bottomDialogHelper.dismiss();
            Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri contentUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", new File(mTempPhotoPath));
                takeIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            } else {
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTempPhotoPath)));
            }
            startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
        }
    }

    // 只有在API大于23以上的时候才需要动态获取权限
    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            bottomDialogHelper.dismiss();
            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:   // 调用相机拍照
                    File temp = new File(mTempPhotoPath);
                    startCropActivity(Uri.fromFile(temp));
                    break;
                case GALLERY_REQUEST_CODE:  // 直接从相册获取
                    startCropActivity(data.getData());
                    break;
                case UCrop.REQUEST_CROP:    // 裁剪图片结果
                    handleCropResult(data);
                    break;
                case UCrop.RESULT_ERROR:    // 裁剪图片错误
                    handleCropError(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void startCropActivity(Uri uri) {
        String targetFileName = String.format("IMG_CROP_%s.png", System.currentTimeMillis());
        int[] maxResultSize = getPhotoMaxResultSize();
        Uri mDestinationUri = Uri.fromFile(new File(mActivity.getCacheDir(), targetFileName));
        UCrop.of(uri, mDestinationUri)
                .withAspectRatio(10, 10)
                .withMaxResultSize(maxResultSize[0], maxResultSize[1])
                .withTargetActivity(CropActivity.class)
                .start(mActivity, this);
    }

    /**
     * 设置最终显示的图片大小 单位像素
     * */
    public abstract int[] getPhotoMaxResultSize();

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        deleteTempPhotoFile();
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri && null != mOnPictureSelectedListener) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mOnPictureSelectedListener.onPictureSelected(resultUri, bitmap);
        } else {
            AppToastMgr.ToastShortCenter(mActivity, "无法剪切选择图片");
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result) {
        deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            AppToastMgr.ToastShortCenter(mActivity, cropError.getMessage());
        } else {
            AppToastMgr.ToastShortCenter(mActivity, "无法剪切选择图片");
        }
    }

    /**
     * 删除拍照临时文件
     */
    private void deleteTempPhotoFile() {
        File tempFile = new File(mTempPhotoPath);
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }

    /**
     * 设置图片选择的回调监听
     *
     * @param listener
     */
    public void setOnPictureSelectedListener(OnPictureSelectedListener listener) {
        this.mOnPictureSelectedListener = listener;
    }

    /**
     * 剪切图片
     */
    protected void selectPicture() {
        bottomDialogHelper.show();
    }

    /**
     * 图片选择的回调接口
     */
    public interface OnPictureSelectedListener {
        /**
         * 图片选择的监听回调
         *
         * @param fileUri
         * @param bitmap
         */
        void onPictureSelected(Uri fileUri, Bitmap bitmap);
    }

    public void initBottomSheetDialog() {
        bottomDialogHelper = new BottomDialogHelper(mActivity, new BottomDialogHelper.OnBottomDialogItemClick() {
            @Override
            public void onTakePhotoClick() {
                takePhoto();
            }

            @Override
            public void onPickPhotoClick() {
                pickFromGallery();
            }

            @Override
            public void onPickPreSetPhotoClick() {
                AppToastMgr.ToastShortCenter(mActivity, "打开预设图片");
            }

            @Override
            public void onCancelClick() {
                bottomDialogHelper.dismiss();
            }
        });
    }

    /**
     * 请求权限 如果权限被拒绝过，则提示用户需要权限
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            showSetRequestPermissionDialog(mActivity, "温馨提示", rationale);
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    protected void showSetRequestPermissionDialog(final Context context, String title, String message) {
        AlertDialogHelper dialogHelper = new AlertDialogHelper();
        dialogHelper.showAlert(context, title, message, new AlertDialogHelper.AlertDialogInterface() {
            @Override
            public void onNegativeButtonClick() {

            }

            @Override
            public void onPositiveButtonClick() {
                AppPhoneMgr.toAppSettingsApp(context);
            }
        });
    }
}