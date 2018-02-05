package com.lucky.androidlearn.rxjava2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.rxjava2.model.BaseParam;
import com.lucky.androidlearn.rxjava2.model.User;
import com.lucky.androidlearn.rxjava2.model.UserParam;
import com.lucky.androidlearn.rxjava2.task.LoginTask;
import com.lucky.androidlearn.rxjava2.task.TaskLauncher;
import com.lucky.androidlearn.rxjava2.task.UserProfileTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava 操作符使用
 * <p>
 * 用户登录操作
 *
 * @author zfz
 *         Created by zfz on 2018/3/17.
 */

public class RxJavaOperatorActivity extends AppCompatActivity {

    private static final String TAG = "RxJavaOperators";

    @BindView(R.id.tv_user_profile)
    TextView tvUserProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_operators);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_operator_map)
    public void operatorMap() {
        Observable.just(1, 2, 3)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return String.format("我是第%s", integer);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept: " + s);
                    }
                });
    }


    @OnClick(R.id.btn_login)
    public void login() {
        loginAndGetProfile();
    }

    public UserParam getUserParam() {
        return new UserParam("jack", "123456");
    }

    // FlatMap可以多次发射
    public void loginAndGetProfile() {
        Observable.just(getUserParam())
                .flatMap(new Function<UserParam, ObservableSource<BaseParam>>() {
                    @Override
                    public ObservableSource<BaseParam> apply(UserParam userParam) throws Exception {
                        LoginTask loginTask = new LoginTask(getUserParam());
                        BaseParam param = new TaskLauncher<BaseParam>().execute(loginTask);
                        return Observable.just(param);
                    }
                })
                .flatMap(new Function<BaseParam, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(BaseParam baseParam) throws Exception {
                        User user = new TaskLauncher<User>().execute(new UserProfileTask(baseParam));
                        return Observable.just(user);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        tvUserProfile.setText(user.toString());
                    }
                });
    }


}
