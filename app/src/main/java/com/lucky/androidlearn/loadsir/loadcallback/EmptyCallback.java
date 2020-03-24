package com.lucky.androidlearn.loadsir.loadcallback;

import com.kingja.loadsir.callback.Callback;
import com.lucky.androidlearn.R;

public class EmptyCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_empty;
    }

}
