package com.lucky.androidlearn.webservice.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.webservice.utils.ProgressDialogUtils;
import com.lucky.androidlearn.webservice.utils.WebServiceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class WebServiceActivity extends AppCompatActivity {
    private List<String> provinceList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webservice);
        init();
    }

    private void init() {
        final ListView mProvinceList = (ListView) findViewById(R.id.province_list);
        ProgressDialogUtils.showProgressDialog(this, "正在加载数据...");

        WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "getSupportProvince", null, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(SoapObject result) {
                ProgressDialogUtils.dismissProgressDialog();
                if (result != null) {
                    provinceList = parseSoapObject(result);
                    mProvinceList.setAdapter(new ArrayAdapter<String>(WebServiceActivity.this, android.R.layout.simple_list_item_1, provinceList));
                } else {
                    Toast.makeText(WebServiceActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mProvinceList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(WebServiceActivity.this, CityActivity.class);
                intent.putExtra("province", provinceList.get(position));
                startActivity(intent);
            }
        });
    }

    private List<String> parseSoapObject(SoapObject result) {
        List<String> list = new ArrayList<String>();
        SoapObject provinceSoapObject = (SoapObject) result.getProperty("getSupportProvinceResult");
        if (provinceSoapObject == null) {
            return null;
        }
        for (int i = 0; i < provinceSoapObject.getPropertyCount(); i++) {
            list.add(provinceSoapObject.getProperty(i).toString());
        }
        return list;
    }

}
