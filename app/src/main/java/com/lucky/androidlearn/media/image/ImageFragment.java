package com.lucky.androidlearn.media.image;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lucky.androidlearn.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/12/24.
 */

public class ImageFragment extends PictureSelectFragment {

    @BindView(R.id.img_capture)
    ImageView imgCapture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_capture, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnPictureSelectedListener(new OnPictureSelectedListener() {
            @Override
            public void onPictureSelected(Uri fileUri, Bitmap bitmap) {
                //imgCapture.setImageBitmap(bitmap);
                String filePath = fileUri.getEncodedPath();
                String imagePath = Uri.decode(filePath);
                Picasso.with(getActivity()).load(fileUri).into(imgCapture);
            }
        });
    }

    @Override
    public int[] getPhotoMaxResultSize() {
        return new int[]{500, 500};
    }

    @OnClick(R.id.btn_inside_camera)
    public void OnInsideCameraClick(View view) {
        selectPicture();
    }
}
