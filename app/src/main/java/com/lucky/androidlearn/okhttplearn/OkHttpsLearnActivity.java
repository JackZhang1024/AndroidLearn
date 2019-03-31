package com.lucky.androidlearn.okhttplearn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lucky.androidlearn.R;

import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

// 1. 客户端通过SSL 访问，我们使客户端通过我们设置的证书信任服务端的证书
// 2. 双向认证
// https://blog.csdn.net/yukaihuaboke/article/details/78926166
// okHttp http://blog.csdn.net/hello_1s/article/details/76641527
// https https://blog.csdn.net/lmj623565791/article/details/48129405
// 日志拦截器 https://blog.csdn.net/bunny1024/article/details/53504556
// okHttps忽略证书 https://blog.csdn.net/u014752325/article/details/73185351
// 信任所有证书和当前证书 https://blog.csdn.net/yukaihuaboke/article/details/78926166
// okhttp 缓存请求 https://www.cnblogs.com/lenve/p/6063851.html
// 缓存请求 https://www.jianshu.com/p/dbda0bb8d541
// https 单向认证和双向认证 https://blog.csdn.net/u011394071/article/details/52880062
// https://www.jianshu.com/p/64172ccfb73b
/**
 * 1.
 *
 * 2. 对于自签名网站的访问 可以选择忽略证书（忽略所有证书 包括自签名证书） 也可以选择信任证书（信任自签名证书）
 * 忽略证书就意味着信任所有证书 会有风险
 * 自签名由于不被信任，如果我们想用的话，意思就是我们自己信任此证书，则需要okHttpClient信任此证书，才能保证通信正常 （自己信任 出了问题自己处理 不能怪别人）
 *
 * 弘扬的Https双向认证 https https://blog.csdn.net/lmj623565791/article/details/48129405
 * Intellij IDEA 创建JavaWeb项目
 * https://blog.csdn.net/builder_taoge/article/details/79501694
 *
 *
 *
 */

// TODO: 2019/3/31 后面再完善双向认证 Tomcat配置和Android客户端配置
public class OkHttpsLearnActivity extends AppCompatActivity {

    private static final String TAG = "OkHttpsLearn";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttps);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.btn_trust_certificate)
    public void onHttpsClick() {
        String url = "https://www.zhaoapi.cn/user/login";
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        setCertificate(builder, "zhaoping.cer");
        OkHttpClient client = builder.build();
        FormBody formBody = new FormBody.Builder()
                .add("mobile", "jack")
                .add("password", "1821057554")
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().toString());
            }
        });
    }

    // 看证书的来源  服务器用的什么证书 CA证书  自签名证书
    // CA证书 不用管 Android系统会自己处理 客户端这边只需要公钥
    // 自签名证书 需要处理  可以选择忽略（有风险） 也可以选择信任证书
    @OnClick(R.id.btn_trust_all_certificates)
    public void trustAllCertificates(){
        // https://hao.360.cn/?src=bm
        // https://www.zhaoapi.cn/user/login
        String url = "https://hao.360.cn/?src=bm";
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        builder.addInterceptor(new LoggingInterceptor());
        //builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
        //builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        OkHttpClient client = builder.build();
        FormBody formBody = new FormBody.Builder()
                .add("mobile", "jack")
                .add("password", "1821057554")
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().toString());
            }
        });
    }


    /**
     * app 设置证书验证的方法
     */
    public void setCertificate(OkHttpClient.Builder builder, String certName) {
        try {
            // https固定模式 X.509 固定模式
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            // 关联证书对象
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            String certificateAlias = Integer.toString(0);
            // 核心逻辑，信任什么证书  从Asset读取拷贝的证书
            Certificate certificate = certificateFactory.generateCertificate(getAssets().open(certName));
            keyStore.setCertificateEntry(certificateAlias, certificate);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // 信任关联器
            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            // 初始化证书对象
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory());
            builder.addInterceptor(new HttpLoggingInterceptor());
//            builder.hostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    // 1. certificate
    // 2. keyStore
    // 3. factory
    // 4. sslContext
    // 5. setSSL
    public class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();

            long t1 = System.nanoTime();//请求发起的时间
            Log.e(TAG, String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();//收到响应的时间

            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            Log.e(TAG, String.format("接收响应: [%s] %n返回json:【%s】 %.1fms%n%s", response.request().url(), responseBody.string(), (t2 - t1) / 1e6d,
                    response.headers()));
            return response;
        }
    }
}
