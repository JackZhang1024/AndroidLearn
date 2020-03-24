package com.lucky.androidlearn.mvc;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.mvc.model.OnRequestCallBack;
import com.lucky.androidlearn.mvc.model.WeatherModel;
import com.lucky.androidlearn.mvc.model.WeatherModelImpl;
import com.lucky.androidlearn.mvc.model.bean.WeatherModelBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Retrofit 必学必会知识
 * https://www.jianshu.com/p/308f3c54abdd
 *
 * Okhttp学习
 * http://blog.csdn.net/hello_1s/article/details/76641527
 *
 * MVC for Android

 在Android开发中，比较流行的开发框架模式采用的是MVC框架模式，采用MVC模式的好处是便于UI界面部分的显示和业务逻辑，数据处理分开。
 那么Android项目中哪些代码来充当M,V,C角色呢？

 M层：适合做一些业务逻辑处理，比如数据库存取操作，网络操作，复杂的算法，耗时的任务等都在model层处理。
 V层：应用层中处理数据显示的部分，XML布局可以视为V层，显示Model层的数据结果。
 C层：在Android中，Activity处理用户交互问题，因此可以认为Activity是控制器，Activity读取V视图层的数据（eg.读取当前EditText控件的数据），
 控制用户输入（eg.EditText控件数据的输入），并向Model发送数据请求（eg.发起网络请求等）。

 Activity持有了WeatherModel模型的对象，
 当用户有点击Button交互的时候，Activity作为Controller控制层读取View视图层EditTextView的数据，
 然后向Model模型发起数据请求，也就是调用WeatherModel对象的方法 getWeather（）方法。
 当Model模型处理数据结束后，通过接口OnWeatherListener通知View视图层数据处理完毕，
 View视图层该更新界面UI了。然后View视图层调用displayResult（）方法更新UI。
 至此，整个MVC框架流程就在Activity中体现出来了。

 利用MVC设计模式，使得这个天气预报小项目有了很好的可扩展和维护性，当需要改变UI显示的时候，
 无需修改Contronller（控制器）Activity的代码和Model（模型）WeatherModel模型中的业务逻辑代码，很好的将业务逻辑和界面显示分离。
 在Android项目中，业务逻辑，数据处理等担任了Model（模型）角色，
 XML界面显示等担任了View（视图）角色，Activity担任了Contronller（控制器）角色。
 contronller（控制器）是一个中间桥梁的作用，通过接口通信来协同 View（视图）和Model（模型）工作，起到了两者之间的通信作用。
 什么时候适合使用MVC设计模式？当然一个小的项目且无需频繁修改需求就不用MVC框架来设计了，那样反而觉得代码过度设计，代码臃肿。
 一般在大的项目中，且业务逻辑处理复杂，页面显示比较多，需要模块化设计的项目使用MVC就有足够的优势了。

 在MVC模式中我们发现，其实控制器Activity主要是起到解耦作用，将View视图和Model模型分离，虽然Activity起到交互作用，
 但是找Activity中有很多关于视图UI的显示代码，因此View视图和Activity控制器并不是完全分离的，
 也就是说一部分View视图和Contronller控制器Activity是绑定在一个类中的。
 MVC的优点：
 (1)耦合性低。所谓耦合性就是模块代码之间的关联程度。利用MVC框架使得View（视图）层和Model（模型）层可以很好的分离，
 这样就达到了解耦的目的，所以耦合性低，减少模块代码之间的相互影响。
 (2)可扩展性好。由于耦合性低，添加需求，扩展代码就可以减少修改之前的代码，降低bug的出现率。
 (3)模块职责划分明确。主要划分层M,V,C三个模块，利于代码的维护。

 * Created by zfz on 2018/2/2.
 */
public class MVCMainActivity extends AppCompatActivity implements OnRequestCallBack{
    private static final String TAG = "MVCMainActivity";
    private static final String BASE_URL = "http://tj.nineton.cn/";

    @BindView(R.id.tv_query_result)
    TextView mTvWeatherResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvc);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_query_weather)
    public void onWeatherQueryClick(){
        WeatherModel weatherModel = new WeatherModelImpl();
        weatherModel.getWeather("", this);
    }

    @Override
    public void onRequestSuccess(String message) {
        mTvWeatherResult.setText(message);
    }

    @Override
    public void onRequestFail(String error) {

    }

    @Override
    public void onRequestSuccess(WeatherModelBean weatherModelBean) {

    }
}
