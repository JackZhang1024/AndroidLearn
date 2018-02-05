package com.lucky.androidlearn.mvvm.learn;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.jingewenku.abrahamcaijin.commonutil.PicassoUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by zfz on 2018/2/5.
 */

public class Profile {

    private String userName;
    private String userAvatar;

    public Profile() {
    }

    public Profile(String userName, String userAvatar) {
        this.userName = userName;
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    // 利用注解@BindingAdapter给ImageView设置网络图片
    // 注解的属性是 useravatar 然后在布局文件中使用该属性
    @BindingAdapter("useravatar")
    public static void setUserAvatar(ImageView imageView, String userAvatar){
        Picasso.with(imageView.getContext()).load(userAvatar).into(imageView);
    }
}
