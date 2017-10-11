package com.lucky.androidlearn.domain.interactors.impl;


import com.lucky.androidlearn.domain.executor.Executor;
import com.lucky.androidlearn.domain.executor.MainThread;
import com.lucky.androidlearn.domain.interactors.WelcomeInteractor;
import com.lucky.androidlearn.domain.interactors.base.AbstractInteractor;
import com.lucky.androidlearn.domain.repository.MessageRepository;
import timber.log.Timber;

/**
 * Created by zfz on 2017/1/1.
 */

public class WelcomeInterceptorImpl extends AbstractInteractor implements WelcomeInteractor {
    private MainThread mMainThread;
    private Callback mCallback;
    private MessageRepository mMessageRepository;

    public WelcomeInterceptorImpl(Executor threadExecutor, MainThread mainThread, Callback callback, MessageRepository messageRepository) {
        super(threadExecutor, mainThread);
        this.mMainThread=mainThread;
        this.mCallback=callback;
        this.mMessageRepository=messageRepository;
    }


    private void notifyError(){
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onRetrieveError(" Retrieve Message Error!");
            }
        });
    }

    private void postMessage(final String message){
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onMessageRetrieved(message);
            }
        });
    }

    /**
     * 在工作线程中处理相关的业务逻辑
     * 在这块就要用接口来调用
     * 我们只需要在MessageRepository的接口实现中
     * 进行相应的业务逻辑处理即可
     * 降低耦合
     * 如果后期有其他的处理 我们只需要实现MessageRepository接口
     * 就OK了
     * */
    @Override
    public void run() {
        String message=mMessageRepository.getWelcomeMessage();
        if("".equals(message) || message==null){
            notifyError();
            return;
        }
        postMessage(message);
    }

    @Override
    public void cancel() {
        super.cancel();
        Timber.e("isStillRunning---"+isRunning());
    }
}
