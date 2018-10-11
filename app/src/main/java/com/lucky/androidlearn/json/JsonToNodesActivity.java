package com.lucky.androidlearn.json;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.jingewenku.abrahamcaijin.commonutil.AppResourceMgr;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.json.node.NodeData;
import com.lucky.androidlearn.json.node.ZiRuNode;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonToNodesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "JsonToNodesActivity";
    private Button mBtnJson2Nodes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json2nodes);
        mBtnJson2Nodes = findViewById(R.id.btn_json2nodes);
        mBtnJson2Nodes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_json2nodes:
                String json = AppResourceMgr.getStringByAssets(this, "elements.json");
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject elementData = jsonObject.getJSONObject("element");
                    ZiRuNode rootZiRuNode = ZiRuNode.createZiRuNode(elementData, null);
                    Log.e(TAG, "display:  "+rootZiRuNode.getNodeData().getViewId()+" viewName "+rootZiRuNode.getNodeData().getViewName());
                    display(rootZiRuNode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void display(ZiRuNode ziRuNode){
        NodeData nodeData = ziRuNode.getNodeData();
        if (nodeData.getChildren().size()>0) {
            for (ZiRuNode node:nodeData.getChildren()){
                Log.e(TAG, "display:  "+node.getNodeData().getViewId()+" viewName "+node.getNodeData().getViewName());
                display(node);
            }
        }
    }



}
