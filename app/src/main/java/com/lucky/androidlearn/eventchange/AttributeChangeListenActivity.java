package com.lucky.androidlearn.eventchange;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Vector;


// 监听属性变化
public class AttributeChangeListenActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AttributeChangeListen";
    private Button mBtnAttributeChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventchangelisten);
        mBtnAttributeChange = findViewById(R.id.btn_attribute_listen);
        mBtnAttributeChange.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_attribute_listen:
                EventProducer producer = new EventProducer();
                producer.addListener(new EventConsumer());
                //producer.setValue(10);
                producer.mValue = 20;
                break;
        }
    }

    // 用于在设置属性值的时候,触发事件
    class ValueEventObject extends EventObject {

        private int value;

        public ValueEventObject(Object source) {
            this(source, 0);
        }

        public ValueEventObject(Object source, int value) {
            super(source);
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    interface ValueChangeListener extends EventListener {
        public void perform(ValueEventObject valueEventObject);
    }

    // 事件注册
    public class EventRegister {
        private Vector<ValueChangeListener> mListeners = new Vector<>();

        public void addListener(ValueChangeListener valueChangeListener) {
            mListeners.addElement(valueChangeListener);
        }

        public void removeListener(ValueChangeListener valueChangeListener) {
            mListeners.removeElement(valueChangeListener);
        }

        // 触发事件
        public void fireEvent(ValueEventObject valueEventObject) {
            for (ValueChangeListener listener : mListeners) {
                listener.perform(valueEventObject);
            }
        }

    }


    // 事件生产者 负责事件注册
    public class EventProducer {
        private EventRegister mEventRegister = new EventRegister();
        private int mValue;


        public void setValue(int value) {
            if (mValue != value) {
                ValueEventObject valueEventObject = new ValueEventObject(this, value);
                fireEvent(valueEventObject);
                mValue = value;
            }
        }


        public void addListener(ValueChangeListener valueChangeListener) {
            mEventRegister.addListener(valueChangeListener);
        }

        public void removeListener(ValueChangeListener valueChangeListener) {
            mEventRegister.removeListener(valueChangeListener);
        }

        public void fireEvent(ValueEventObject valueEventObject) {
            mEventRegister.fireEvent(valueEventObject);
        }

    }

    // 事件消费者 负责接收事件结果
    public class EventConsumer implements ValueChangeListener {

        @Override
        public void perform(ValueEventObject valueEventObject) {
            Log.e(TAG, "perform: " + valueEventObject.value);
        }
    }


}
