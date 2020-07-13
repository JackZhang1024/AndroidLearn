package com.lucky.androidlearn.rxjava2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.rxjava2.model.BaseParam;
import com.lucky.androidlearn.rxjava2.model.User;
import com.lucky.androidlearn.rxjava2.model.UserParam;
import com.lucky.androidlearn.rxjava2.task.LoginTask;
import com.lucky.androidlearn.rxjava2.task.TaskLauncher;
import com.lucky.androidlearn.rxjava2.task.UserProfileTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava 操作符使用
 * <p>
 * 用户登录操作
 *
 * @author zfz
 * Created by zfz on 2018/3/17.
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
//        Observable.just(1, 2, 3)
//                .map(new Function<Integer, String>() {
//                    @Override
//                    public String apply(Integer integer) throws Exception {
//                        return String.format("我是第%s", integer);
//                    }
//                })
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Log.e(TAG, "accept: " + s);
//                    }
//                });
        mapStudents();
    }

    @OnClick(R.id.btn_operator_flatmap)
    public void onFlatMapClick() {
        //flatMapStudents();
        //onElementAtClick();
        //onDistinctClick();
        //onSkipClick();
        //onTakeClick();
        onTimeOutClick();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        loginAndGetProfile();
    }

    @OnClick(R.id.btn_operator_buffer)
    public void onBufferClick() {
        Observable.just(1, 2, 3, 4, 5, 6).buffer(3).subscribe(new Observer<List<Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Integer> integers) {
                for (Integer i : integers) {
                    Log.e(TAG, "onNext: " + i);
                }
                Log.e(TAG, "onNext: -----------");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @OnClick(R.id.btn_operator_filter)
    public void onFilterClick() {
        Observable.just(1, 2, 3, 4, 5).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer > 3;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void onElementAtClick() {
        Observable.just(1, 2, 3, 4).elementAt(2).subscribe(new MaybeObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                Log.e(TAG, "onSuccess: " + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void onDistinctClick() {
        Observable.just(1, 2, 3, 1, 3, 5).distinct().subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void onSkipClick() {
        Observable.just(1, 2, 3, 5).skip(2).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "skip onNext: " + integer); // skip onNext: 3 / skip onNext: 5
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void onTakeClick() {
        Observable.just(1, 2, 3, 5).take(2).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "take onNext: " + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void onTimeOutClick() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 4; i++) {
                    try {
                        Thread.sleep(i * 100);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    e.onNext(i);
                }
                e.onComplete();
            }
        }).timeout(200, TimeUnit.MILLISECONDS, Observable.just(10, 11)).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: "+integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
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

    public void flatMapStudents() {
        List<Student> students = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            Student student = new Student("学生 " + index);
            student.code = index;
            student.addCourses();
            students.add(student);
        }
        Disposable disposable = Observable.fromIterable(students).concatMap(new Function<Student, ObservableSource<Course>>() {
            @Override
            public ObservableSource<Course> apply(Student student) throws Exception {
                Log.e(TAG, "apply: student.name " + student.name);
                //return Observable.fromIterable(student.courses);
                return getObservable(student);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Course>() {
            @Override
            public void accept(Course course) throws Exception {
                Log.e(TAG, "accept: student " + course.studentName + "-" + course.getName());
            }
        });
    }

    private Observable<Course> getObservable(Student student) {
        return Observable.create(new ObservableOnSubscribe<Course>() {
            @Override
            public void subscribe(ObservableEmitter<Course> e) throws Exception {
                e.onNext(student.getCourses().get(0));
                if (student.code != 0) { // 1, 2 随机延时
                    Thread.sleep(1000);
                }
                e.onNext(student.getCourses().get(1));
                e.onComplete();
            }
        });
    }

    public void mapStudents() {
        List<Student> students = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            Student student = new Student("学生 " + index);
            student.code = index;
            student.addCourses();
            students.add(student);
        }
        Disposable disposable = Observable.fromIterable(students).map(new Function<Student, List<Course>>() {
            @Override
            public List<Course> apply(Student student) throws Exception {
                Log.e(TAG, "apply: student " + student.name);
                return student.getCourses();
            }
        }).subscribe(new Consumer<List<Course>>() {
            @Override
            public void accept(List<Course> courses) throws Exception {
                for (Course course : courses) {
                    Log.e(TAG, "accept: student " + course.studentName + "-" + course.getName());
                }
            }
        });
    }


    class Course {
        private String name;
        private String studentName;
        private int timeLen;

        public Course(String studentName, String name) {
            this.studentName = studentName;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTimeLen() {
            return timeLen;
        }

        public void setTimeLen(int timeLen) {
            this.timeLen = timeLen;
        }
    }

    class Student {
        public String name;
        public int code;
        private List<Course> courses = new ArrayList<>();
        private String[] courseArr = new String[]{"语文", "数学", "英语"};

        public Student(String name) {
            this.name = name;
        }

        public List<Course> addCourses() {
            int index = new Random().nextInt(3);
            courses.add(new Course(name, courseArr[index]));
            int index2 = new Random().nextInt(3);
            courses.add(new Course(name, courseArr[index2]));
            return courses;
        }

        public List<Course> getCourses() {
            return courses;
        }
    }

}
