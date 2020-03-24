package com.lucky.androidlearn.mvvm.learn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;

import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.databinding.ActivityMvvmLearnBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0603/2992.html
 * http://blog.csdn.net/qq_33689414/article/details/52205734
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0131/3930.html
 * <p>
 * Created by zfz on 2018/2/5.
 */

public class MVVMLearnActivity extends AppCompatActivity {
    private static final String TAG = "MVVMLearnActivity";
    ActivityMvvmLearnBinding mvvmLearnBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过DataBing加载的布局都会生成一个以Bing结尾的Bing对象，例如ActivityMvvmLearnBing, 这个类的名称和加载的布局名称有关
        mvvmLearnBinding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm_learn);
        // 绑定基本数据类型
        mvvmLearnBinding.setContent("七里香");
        mvvmLearnBinding.setEnabled(true);
        mvvmLearnBinding.btnBasicEnable.setOnClickListener((view) -> {
            AppToastMgr.shortToast(MVVMLearnActivity.this, "我被点击了...");
        });
        UserEntity userEntity = new UserEntity();
        userEntity.setNickname("小丑");
        userEntity.setUsername("劳斯克斯");
        userEntity.setAge(23);
        userEntity.setAdult(true);
        mvvmLearnBinding.setUser(userEntity);
        mvvmLearnBinding.setProfile(new Profile("jay", "https://p1.ssl.qhmsg.com/t013ea7c622f8b91ed8.jpg"));
        mvvmLearnBinding.btnChangeText.setOnClickListener((view) -> {
            mvvmLearnBinding.tvNickname.setText("大家好，我是周杰伦");
        });

        // 绑定时间
        mvvmLearnBinding.setTitle1("Title1");
        mvvmLearnBinding.setTitle2("Title2");
        mvvmLearnBinding.setTitle3("Title3");
        mvvmLearnBinding.setTitle4("Title4");

        mvvmLearnBinding.setEvent(new EventListener() {
            @Override
            public void onClick1(View view) {
                mvvmLearnBinding.setTitle1("点击了Title1");
            }

            @Override
            public void onClick2(View view) {
                mvvmLearnBinding.setTitle2("点击了Title2");
            }

            @Override
            public void onClick3(String string) {
                mvvmLearnBinding.setTitle3(string);
            }
        });
        // DataBing操作符使用
        mvvmLearnBinding.setOperation0(10);

        // 自动更新数据
        notifyDataChange();

        // 设置集合数据
        setDataSet();

        setObservableCollection();

        onDataBindingIncludeClick();
    }

    // 事件绑定
    public interface EventListener {
        void onClick1(View view);

        void onClick2(View view);

        void onClick3(String string);
    }

    // 数据更新
    public void notifyDataChange(){
        PersonObservable personObservable = new PersonObservable();
        personObservable.setName("周杰伦");
        personObservable.setNickname("Jay");
        mvvmLearnBinding.setPerson(personObservable);
        mvvmLearnBinding.btnChangeNameNickname.setOnClickListener((view)->{
            // 这么做的操作就是不用再一次重新给TextView上设置数据
            personObservable.setName("狄仁杰");
            personObservable.setNickname("阁老");
        });
    }

    public void setDataSet(){
        ArrayList<String> list = new ArrayList<>();
        list.add("我是list第一个条数据");
        mvvmLearnBinding.setList(list);

        Map<String, String> map = new HashMap<>();
        map.put("name", "周杰伦");
        mvvmLearnBinding.setMap(map);

        String[] array = new String[]{"七里香", "告白气球"};
        mvvmLearnBinding.setArray(array);
    }

    public void setObservableCollection(){
        ObservableArrayList<String> observableList = new ObservableArrayList<>();
        observableList.add("我是observableList中第一条数据");
        mvvmLearnBinding.setObservablelist(observableList);

        ObservableArrayMap<String, String> observableArrayMap = new ObservableArrayMap<>();
        observableArrayMap.put("name", "周杰伦");
        observableArrayMap.put("gender", "male");
        mvvmLearnBinding.setObservablemap(observableArrayMap);

        mvvmLearnBinding.btnChangeObservableCollection.setOnClickListener((view)->{
            observableList.add(0, "七里香");
            observableList.add(1, "东风破");
            observableArrayMap.put("name", "蔡依林");
            observableArrayMap.put("gender", "female");
        });
    }

    // DataBinding和include标签结合使用
    private void onDataBindingIncludeClick(){
        mvvmLearnBinding.btnDatabindingInclude.setOnClickListener((view)->{
            startActivity(new Intent(this, DataBindingIncludeActivity.class));
        });
    }


}
