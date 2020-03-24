package com.lucky.androidlearn.webservice.demo;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.webservice.utils.ProgressDialogUtils;
import com.lucky.androidlearn.webservice.utils.WebServiceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

public class WeatherActivity extends AppCompatActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);
		init();
	}

	private void init() {
		final TextView mTextWeather = (TextView) findViewById(R.id.weather);
		ProgressDialogUtils.showProgressDialog(this, "正在加载数据...");
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("theCityName", getIntent().getStringExtra("city"));
		
		WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "getWeatherbyCityName", properties, new WebServiceUtils.WebServiceCallBack() {
			
			@Override
			public void callBack(SoapObject result) {
				ProgressDialogUtils.dismissProgressDialog();
				if(result != null){
					SoapObject detail = (SoapObject) result.getProperty("getWeatherbyCityNameResult");
					StringBuilder sb = new StringBuilder();
					for(int i=0; i<detail.getPropertyCount(); i++){
						sb.append(detail.getProperty(i)).append("\r\n");
					}
					mTextWeather.setText(sb.toString());
				}else{
					Toast.makeText(WeatherActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
