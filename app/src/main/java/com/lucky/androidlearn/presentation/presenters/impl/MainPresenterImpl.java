package com.lucky.androidlearn.presentation.presenters.impl;


import com.lucky.androidlearn.domain.executor.Executor;
import com.lucky.androidlearn.domain.executor.MainThread;
import com.lucky.androidlearn.domain.interactors.SampleInteractor;
import com.lucky.androidlearn.domain.interactors.WelcomeInteractor;
import com.lucky.androidlearn.domain.interactors.impl.WelcomeInterceptorImpl;
import com.lucky.androidlearn.domain.repository.MessageRepository;
import com.lucky.androidlearn.presentation.presenters.MainPresenter;
import com.lucky.androidlearn.presentation.presenters.base.AbstractPresenter;

/**
 * Created by dmilicic on 12/13/15.
 */
public class MainPresenterImpl extends AbstractPresenter implements MainPresenter, WelcomeInteractor.Callback {

    private MainPresenter.View mView;
    private MessageRepository mMessageRepository;
    WelcomeInterceptorImpl interceptor;

    public MainPresenterImpl(Executor executor, MainThread mainThread, View view, MessageRepository messageRepository) {
        super(executor, mainThread);
        mView = view;
        mMessageRepository=messageRepository;
    }

    @Override
    public void resume() {
        mView.showProgress();
        interceptor=new WelcomeInterceptorImpl(mExecutor,mMainThread,this,mMessageRepository);
        //开始执行相关的任务 调用execute方法
        //不是调用run方法
        interceptor.execute();
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        interceptor.cancel();
    }

    @Override
    public void onError(String message) {
        mView.showError(message);
    }

    @Override
    public void onMessageRetrieved(String message) {
        mView.hideProgress();
        mView.displayMessage(message);
    }

    @Override
    public void onRetrieveError(String error) {
        mView.hideProgress();
        onError(error);
    }
}
