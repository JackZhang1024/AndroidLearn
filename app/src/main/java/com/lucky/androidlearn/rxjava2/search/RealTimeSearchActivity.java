package com.lucky.androidlearn.rxjava2.search;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.rxjava2.task.KeyWordsSearchRunnableTask;
import com.lucky.androidlearn.rxjava2.task.KeyWordsSearchTask;
import com.lucky.androidlearn.rxjava2.task.SearchCallBack;
import com.lucky.androidlearn.rxjava2.task.SearchTaskLauncher;
import com.lucky.androidlearn.rxjava2.task.TaskLauncher;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 实时搜索功能 类似联网查询
 *
 * @author zfz
 */
public class RealTimeSearchActivity extends AppCompatActivity {
    private static final String TAG = "RealTimeSearchActivity";
    @BindView(R.id.et_real_time_search)
    EditText mEditText;

    @BindView(R.id.tv_search_result)
    TextView mTvResult;

    MyHandler myHandler = new MyHandler(this);

    static class MyHandler extends Handler {
        WeakReference<Context> weakReference = null;

        public MyHandler(Context context) {
            weakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RealTimeSearchActivity activity = (RealTimeSearchActivity) weakReference.get();
            if (activity != null) {
                String searchResult = (String) msg.obj;
                activity.mTvResult.setText(searchResult);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_search);
        ButterKnife.bind(this);
        mEditText.addTextChangedListener(mEditTextWatcher);
    }

    EditTextWatcher mEditTextWatcher = new EditTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            super.onTextChanged(s, start, before, count);
            Log.e(TAG, "onTextChanged: " + s.toString());
            String keyWord = s.toString();
            //doSearch(keyWord);
            //doRunnableSearch(keyWord);
            doRunnableSearchTask(keyWord);
        }
    };

    private void doSearch(String keyWord) {
        try {
            List<String> keyWordsList = new TaskLauncher<List<String>>().execute(new KeyWordsSearchTask(keyWord));
            Log.e(TAG, "doSearch: " + keyWordsList.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doRunnableSearch(String keyWord) {
        new TaskLauncher<List<String>>().executeRunnable(new KeyWordsSearchRunnableTask(keyWord, new SearchCallBack() {
            @Override
            public void onCallBack(List<String> results, boolean abortDisplay) {
                Log.e(TAG, "onCallBack: " + results);
                Message message = new Message();
                message.obj = results.toString();
                myHandler.sendMessage(message);
            }
        }));
    }

    SearchTaskLauncher searchTaskLauncher = new SearchTaskLauncher();
    private void doRunnableSearchTask(String keyWord){
        searchTaskLauncher.executeSearchKeyWordTask(new KeyWordsSearchRunnableTask(keyWord, new SearchCallBack() {
            @Override
            public void onCallBack(List<String> results, boolean abortDisplay) {
                Log.e(TAG, "onCallBack: "+results+" abortDisplay "+abortDisplay);
                if (!abortDisplay){
                    Message message = new Message();
                    message.obj = results.toString();
                    myHandler.sendMessage(message);
                }
            }
        }));
    }



}
