package com.lucky.androidlearn.json;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lucky.androidlearn.R;

import org.json.JSONObject;

public class JsonLearnActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "JsonLearnActivity";
    private Button mBtnParseJson;
    private Button mBtnJson2Nodes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        mBtnParseJson = findViewById(R.id.btn_json);
        mBtnJson2Nodes = findViewById(R.id.btn_json_to_nodes);
        mBtnParseJson.setOnClickListener(this);
        mBtnJson2Nodes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_json:
                parseJson();
                break;
            case R.id.btn_json_to_nodes:
                startActivity(new Intent(this, JsonToNodesActivity.class));
                break;
        }
    }

    private void parseJson() {
        try {
            //String strNotification = "{\\\"lUserId\\\":\\\"13663667\\\",\\\"nOpenMode\\\":\\\"0\\\",\\\"strAnimation\\\":\\\"\\\",\\\"strAttachedData\\\":\\\"strFromPage=messageList&strTradeNo=20180827FDtx062lf800000000009738\\\",\\\"strTempURI\\\":\\\"\\\",\\\"strTitle\\\":\\\"资金明细详情\\\",\\\"strURI\\\":\\\"myCenter\\/fundDetail\\/fundDetail.htm\\\"}";
            String strNotification = "{\"lUserId\":\"13663667\",\"nOpenMode\":\"0\",\"strAnimation\":\"\",\"strAttachedData\":\"strFromPage=messageList&strTradeNo=20180827FDtx062lf800000000009738\"," +
                    "\"strTempURI\":\"\",\"strTitle\":\"资金明细详情\",\"strURI\":\"myCenter/fundDetail/fundDetail.htm\"}";
            String notification = strNotification.replace("\\", "");
            JSONObject mJsonObject = new JSONObject(notification);
            String strlUserId = mJsonObject.getString("lUserId");
            String strURI = mJsonObject.getString("strURI");
            String strTitle = mJsonObject.getString("strTitle");
            Log.e(TAG, "parseJson: strlUserId" + strlUserId + " strURI " + strURI + " strTitle " + strTitle);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class Notification {
        String lUserId;
        String nOpenMode;
        String strAnimation;
        String strAttachedData;
        String strTempURI;
        String strTitle;
        String strURI;

        public Notification(String lUserId, String nOpenMode, String strAnimation, String strAttachedData, String strTempURI, String strTitle, String strURI) {
            this.lUserId = lUserId;
            this.nOpenMode = nOpenMode;
            this.strAnimation = strAnimation;
            this.strAttachedData = strAttachedData;
            this.strTempURI = strTempURI;
            this.strTitle = strTitle;
            this.strURI = strURI;
        }

        public String getlUserId() {
            return lUserId;
        }

        public void setlUserId(String lUserId) {
            this.lUserId = lUserId;
        }

        public String getnOpenMode() {
            return nOpenMode;
        }

        public void setnOpenMode(String nOpenMode) {
            this.nOpenMode = nOpenMode;
        }

        public String getStrAnimation() {
            return strAnimation;
        }

        public void setStrAnimation(String strAnimation) {
            this.strAnimation = strAnimation;
        }

        public String getStrAttachedData() {
            return strAttachedData;
        }

        public void setStrAttachedData(String strAttachedData) {
            this.strAttachedData = strAttachedData;
        }

        public String getStrTempURI() {
            return strTempURI;
        }

        public void setStrTempURI(String strTempURI) {
            this.strTempURI = strTempURI;
        }

        public String getStrTitle() {
            return strTitle;
        }

        public void setStrTitle(String strTitle) {
            this.strTitle = strTitle;
        }

        public String getStrURI() {
            return strURI;
        }

        public void setStrURI(String strURI) {
            this.strURI = strURI;
        }
    }
}
