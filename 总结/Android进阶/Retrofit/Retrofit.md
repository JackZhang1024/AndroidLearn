- [Retrofit](#retrofit)
  - [Retrofit的常用使用方法](#retrofit的常用使用方法)
    - [1. Retrofit的创建](#1-retrofit的创建)
    - [2. Retrofit的GET请求](#2-retrofit的get请求)
    - [3. Retrofit的POST请求](#3-retrofit的post请求)
    - [4. Retrofit的文件上传](#4-retrofit的文件上传)
    - [5. Retrofit的文件下载](#5-retrofit的文件下载)
    - [6. Retrofit的断点下载](#6-retrofit的断点下载)
    - [7. Retrofit文件带进度上传](#7-retrofit文件带进度上传)
  - [Retrofit的原理分析](#retrofit的原理分析)
    - [1. 动态代理](#1-动态代理)
    - [2. Retroif的create方法](#2-retroif的create方法)
## Retrofit
### Retrofit的常用使用方法
#### 1. Retrofit的创建
```java
// 添加Retrofit依赖
// Retrofit
compile 'com.squareup.retrofit2:retrofit:2.0.0'
// Gson转换器
compile 'com.squareup.retrofit2:converter-gson:2.3.0'
// 字符串转换器
compile 'com.squareup.retrofit2:converter-scalars:2.3.0'
// RxJava
implementation 'io.reactivex.rxjava2:rxjava:2.1.1'
implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
// retrofit  返回值的adapter的转化器，为了于RxJava配合使用
implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
// 日志拦截
implementation 'com.squareup.okhttp3:logging-interceptor:3.8.1'
```

```java
// SSLSocketFactoryUtil.java
public class SSLSocketFactoryUtil {

    // 默认信任所有的证书 通过这个配置
    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{createTrustAllManager()}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {

        }
        return sslSocketFactory;
    }

    public static X509TrustManager createTrustAllManager() {
        X509TrustManager tm = null;
        try {
            tm =  new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                     throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                     throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
        } catch (Exception e) {

        }
        return tm;
    }
}

//1. 通过在res/xml目录下创建network_security_config.xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true"/>
</network-security-config>
// 2. 在清单文件中进行配置
<application 
      ...
      android:networkSecurityConfig="@xml/network_security_config">
      ...
</application>
然后就可以在Android项目中使用Https进行访问了，但是还是有风险，还是明文发送请求

// 创建和初始化Retrofit
public class RetrofitFactory {
    private static final String TAG = "RetrofitFactory";

    //访问超时
    private static final int TIME_OUT = 60;

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpInterceptor())
            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    KLog.e(TAG, message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BASIC))
            .sslSocketFactory(SSLSocketFactoryUtil.createSSLSocketFactory())
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build();

    private static RetrofitService retrofitService = new Retrofit.Builder()
            .baseUrl(RetrofitService.REPAYMENT_BASE_URI)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 将返回的Call对象转化成Observable对象
            .client(httpClient)
            .build()
            .create(RetrofitService.class);

    public static RetrofitService getInstance() {
        return retrofitService;
    }
}

// Retrofit的使用换需要一个接口文件才能使用
public interface RetrofitService {

    //Path的使用 替换使用
    @GET("/user/{name}")
    Call<ResponseBody> encoded(@Path("name") String name);
    
    /* 获取图形验证码 */
    @GET("repayment/v1.0.1/imgCode/getImgCode")
    Observable<ResponseBody> downloadVerificationCodeImg(@Query("imei") String imei);

    /* 淘宝外网地址  http://ip.taobao.com/service/getIpInfo2.php?ip=myip */
    @GET("http://ip.taobao.com/service/getIpInfo2.php")
    Observable<String> taoBaoIp(@Query("ip") String ip);

    //简单的表单提交
    /*支付通道获取*/
    @FormUrlEncoded
    @POST("aci/v1.0.1/handlingBusiness/getPayChannels")
    Observable<HttpResult<List<PayChannel>>> payChannels(@FieldMap Map<String, String> fieldMap);

    //文件上传
    /*身份证上传*/
    @POST("user/v1.0.1/userIdcard/idCardDistinguish")
    Observable<HttpResult<AuthenticateModel>> uploadIDCard(@Body RequestBody requestBody);

    //文件下载
    /* 获取邮箱登录图片验证码 */
    @GET
    Observable<ResponseBody> mailLoginVerifyCode(@Url String url);
    ...
}
```
#### 2. Retrofit的GET请求
```java
// 通过GET方法获取图片
public void downLoadVerificationImg(String imei) {
    RetrofitFactory.getInstance()
            .downloadVerificationCodeImg(imei)
            .subscribeOn(Schedulers.io())
            .map(new Function<ResponseBody, Bitmap>() {
                @Override
                public Bitmap apply(ResponseBody responseBody) {
                    long fileSize = responseBody.contentLength();
                    InputStream inputStream = responseBody.byteStream();
                    KLog.e(TAG, "fileSize " + fileSize);
                    return BitmapFactory.decodeStream(inputStream);
                }
            })
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(@NonNull Disposable disposable) throws Exception {
                    addDisposable(disposable);
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Bitmap>() {
                @Override
                public void accept(@NonNull Bitmap verificationCode) throws Exception {
                    mBaseView.displayVerificationCodeImg(verificationCode);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    mBaseView.showError("获取验证码失败");
                    RetrofitExceptionHandler.handleException(throwable);
                }
            });
}
```
#### 3. Retrofit的POST请求
```java
public void getPayChannels(Map<String, String> fieldMap) {
    RetrofitFactory.getInstance()
            .payChannels(fieldMap)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(@NonNull Disposable disposable) throws Exception {
                    addDisposable(disposable);
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<HttpResult<List<PayChannel>>>() {
                @Override
                public void accept(@NonNull HttpResult<List<PayChannel>> httpResult) throws Exception {
                    if ("200".equals(httpResult.getCode())) {
                        mBaseView.onPayChannelSuccess(httpResult.getData());
                    } else {
                        mBaseView.handleServerException(httpResult.getCode(), httpResult.getMsg());
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    mBaseView.showError("发生错误啦!");
                    RetrofitExceptionHandler.handleException(throwable);
                }
            });
}
```
#### 4. Retrofit的文件上传
```java
// 上传文件对应的接口
@POST("user/v1.0.1/userIdcard/idCardDistinguish")
Observable<HttpResult<AuthenticateModel>> uploadIDCard(@Body RequestBody requestBody);

public void uploadIDCard(String name, String path, String side) {
    HashMap<String, String> map = new HashMap<>();
    map.put("side", side);
    String signValue = RequestParamUtil.encodedParams(map);
    File file = new File(path);
    
    MultipartBody uploadBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
            .addFormDataPart("side", side)
            .addFormDataPart("sign", signValue)
            .build();

    RetrofitFactory.getInstance()
            .uploadIDCard(uploadBody)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(@NonNull Disposable disposable) throws Exception {
                    addDisposable(disposable);
                    mBaseView.showLoadingDialog("");
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<HttpResult<AuthenticateModel>>() {
                @Override
                public void accept(@NonNull HttpResult<AuthenticateModel> httpResult) throws Exception {
                    Log.e(TAG, "accept: 返回数据 " + httpResult.getMsg() + "  code " + httpResult.getCode() +"\n" + httpResult.getData());
                    mBaseView.dismissDialog();
                    if ("200".equals(httpResult.getCode())) {
                        mBaseView.uploadIDCardSuccess(side, httpResult.getMsg(), httpResult.getData());
                    } else {
                        mBaseView.handleServerException(httpResult.getCode(), httpResult.getMsg());
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    Log.e(TAG, "accept: " + throwable.getMessage());
                    mBaseView.dismissDialog();
                    mBaseView.showError("发生错误啦!");
                    RetrofitExceptionHandler.handleException(throwable);
                }
            });
}
```
#### 5. Retrofit的文件下载
如果是小文件，则可以通过前面的@GET请求将ResponseBody转换成流写入文件就可以了，但是如果是大文件，这么写可能会发生OOM，所以需要特殊处理
常见的方法有两种，一种是通过拦截器将ResponseBody转换成我们自己定义的ResponseBody，第二种就是通过@Streaming标签转化成普通的ResponseBody，然后直接将内容写到磁盘上。

```java
// 第一种将流直接写进磁盘 不用先存在内存中然后写入磁盘 可以防止OOM 
// RetrofitFactory.java
public class RetrofitFactory {
    private static final String TAG = "RetrofitFactory";
    // 访问超时
    private static final int TIME_OUT = 10*1000;

    // Retrofit是基于OkHttpClient的, 可以创建一个OkHttpClient进行一些配置
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            // 打印接口信息 方便调试接口
            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e(TAG, "log: "+message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BASIC))
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build();

    private static RetrofitService retrofitService = new Retrofit.Builder()
            .baseUrl(RetrofitService.BASE_URL)
            // 添加Gson转换器
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .create()))
            // 添加Retrofi到RxJava的转换器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .callbackExecutor(Executors.newSingleThreadExecutor()) // 在回调中切换到工作线程
            .client(httpClient)
            .build()
            .create(RetrofitService.class);

    public static RetrofitService getInstance(){
        return retrofitService;
    }
}

public class RetrofitActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_download);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_download_large_interceptor)
    public void onInterceptorDownloadClick() {
        Intent intent = new Intent(this, RetrofitSecondActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_upload_large_interceptor)
    public void onUploadFileClick(){
        Intent intent = new Intent(this, RetrofitUploadActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_download_large_file)
    public void onDownLoadLargeFileClick() {
        String url = "http://luckyboy.oss-cn-beijing.aliyuncs.com/oss_file/1594377746736.mp4";
        RetrofitFactory.getInstance().downloadFile(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: " + Thread.currentThread().getName());
                downloadFile(new File(getFilesDir(), "hello.mp4"), response, new DownloadListener() {
                    @Override
                    public void onDownloadFail() {

                    }

                    @Override
                    public void onDownloadSuccess() {

                    }

                    @Override
                    public void onProgressChanged(int progress) {
                        Log.e(TAG, "onProgressChanged: " + progress);
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });
    }

    private void downloadFile(File file, Response<ResponseBody> response, DownloadListener listener) {
        // 1. 创建文件
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onDownloadFail();
            }
        }
        InputStream inputStream = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            inputStream = response.body().byteStream();
            long contentLength = response.body().contentLength();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            long currentLength = 0;
            while ((len = inputStream.read(buffer, 0, bufferSize)) != -1) {
                bos.write(buffer, 0, len);
                currentLength += len;
                // 计算当下进度
                if (listener != null) {
                    listener.onProgressChanged((int) (100 * currentLength / contentLength));
                }
            }
            if (listener != null) {
                listener.onDownloadSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeQuietly(inputStream);
            CloseUtil.closeQuietly(fos);
            CloseUtil.closeQuietly(bos);
        }
    }

    interface DownloadListener {

        void onDownloadSuccess();

        void onProgressChanged(int progress);

        void onDownloadFail();

    }

    static class CloseUtil {
        public static void closeQuietly(Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

```
```java
// 第二种通过拦截器来获取读取的进度
public class RetrofitSecondActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitSecondActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_second);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_interceptor_download)
    public void onInterceptorDownloadClick() {
        RetrofitCallBack<ResponseBody> callBack = new RetrofitCallBack<ResponseBody>() {
            @Override
            public void onLoading(long totalLength, long currentLength) {
                super.onLoading(totalLength, currentLength);
                int progress = (int) (100 * currentLength / totalLength);
                Log.e(TAG, "onLoading: progress " + progress);
            }

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
            }

            @Override
            void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onSuccess: ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ");
            }
        };
        String url = "http://luckyboy.oss-cn-beijing.aliyuncs.com/oss_file/1594377746736.mp4";
        getRetrofitService(callBack).downloadFileInterceptor(url).enqueue(callBack);
    }

    abstract static class RetrofitCallBack<T> implements Callback<T> {

        @Override
        public void onResponse(@NotNull Call<T> call, Response<T> response) {
            if (response.isSuccessful()) {
                onSuccess(call, response);
            } else {
                onFailure(call, new Throwable(response.message()));
            }
        }

        abstract void onSuccess(Call<T> call, Response<T> response);

        public void onLoading(long totalLength, long currentLength) {

        }
    }

    // 扩展ResponseBody 用于处理下载显示进度
    static final class FileResponseBody<T> extends ResponseBody {

        private ResponseBody mResponseBody;
        private RetrofitCallBack<T> mCallBack;

        public FileResponseBody(ResponseBody responseBody, RetrofitCallBack<T> retrofitCallBack) {
            super();
            mResponseBody = responseBody;
            mCallBack = retrofitCallBack;
        }

        @Override
        public MediaType contentType() {
            return mResponseBody.contentType();
        }

        @Override
        public long contentLength() {
            return mResponseBody.contentLength();
        }

        private BufferedSource mBufferedSource;

        @Override
        public BufferedSource source() {
            if (mBufferedSource == null) {
                mBufferedSource = Okio.buffer(source(mResponseBody.source()));
            }
            return mBufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0;

                @Override
                public long read(@NotNull Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    mCallBack.onLoading(mResponseBody.contentLength(), totalBytesRead);
                    return bytesRead;
                }
            };
        }
    }

    private <T> RetrofitService getRetrofitService(RetrofitCallBack<T> callBack) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed(chain.request());
                // 将ResponseBody转化成我们需要的FileResponseBody
                return response.newBuilder().body(new FileResponseBody<T>(response.body(), callBack)).build();
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        return retrofit.create(RetrofitService.class);
    }
}
```

#### 6. Retrofit的断点下载
[后面再去分析，断点下载](https://www.jb51.net/article/163704.htm)

#### 7. Retrofit文件带进度上传
```java  
// 上传文件  通过继承RequestBody，然后在Write()方法中进行进行写的进度获取和回调
public class RetrofitUploadActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitUploadActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_upload);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_upload_interceptor)
    public void onUploadFileClick() {
        RetrofitCallBack<HttpResult<OSSFile>> callBack = new RetrofitCallBack<HttpResult<OSSFile>>() {
            @Override
            void onSuccess(Call<HttpResult<OSSFile>> call, Response<HttpResult<OSSFile>> response) {
                Log.e(TAG, "onSuccess: " + " url "+response.body().data.data.ossUrl);
            }

            @Override
            public void onFailure(Call<HttpResult<OSSFile>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + Thread.currentThread().getName() + " error " + t.getMessage());
            }

            @Override
            public void onUploading(long totalLength, long currentLength) {
                super.onUploading(totalLength, currentLength);
                Log.e(TAG, "onUploading: " + (100 * currentLength / totalLength));
            }
        };
        File file = new File(getFilesDir(), "hello.mp4");
        MultipartBody uploadBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        getRetrofitService().uploadFile(new FileRequestBody<>(uploadBody, callBack)).enqueue(callBack);

//        RequestBody body1 = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
//        //通过该行代码将RequestBody转换成特定的FileRequestBody
//        FileRequestBody body = new FileRequestBody(body1, callBack);
//        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);
//        getRetrofitService().uploadFileNew(part).enqueue(callBack);

    }

    public static class HttpResult<T> {
        public String status;
        private String message;
        private HttpResultInner<T> data;
    }

    public static class HttpResultInner<T> {
        public T data;
    }

    // 上传返回结果
    public static class OSSFile {
        public String ossUrl;

        public void setOssUrl(String ossUrl) {
            this.ossUrl = ossUrl;
        }

        public String getOssUrl() {
            return ossUrl;
        }
    }

    abstract static class RetrofitCallBack<T> implements Callback<T> {

        @Override
        public void onResponse(@NotNull Call<T> call, Response<T> response) {
            if (response.isSuccessful()) {
                onSuccess(call, response);
            } else {
                onFailure(call, new Throwable(response.message()));
            }
        }

        abstract void onSuccess(Call<T> call, Response<T> response);

        // 回调上传进度
        public void onUploading(long totalLength, long currentLength) {

        }
    }

    final static class FileRequestBody<T> extends RequestBody {

        private RequestBody mRequestBody;
        private RetrofitCallBack<T> mCallBack;
        private BufferedSink mBufferedSink;

        public FileRequestBody(RequestBody requestBody, RetrofitCallBack<T> callBack) {
            super();
            this.mRequestBody = requestBody;
            this.mCallBack = callBack;
        }

        @Override
        public MediaType contentType() {
            return mRequestBody.contentType();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (mBufferedSink == null) {
                mBufferedSink = Okio.buffer(sink(sink));
            }
            mRequestBody.writeTo(mBufferedSink);
            // 必须flush 否则最后一部分数据写不进去
            mBufferedSink.flush();
        }

        @Override
        public long contentLength() throws IOException {
            return mRequestBody.contentLength();
        }

        private Sink sink(Sink sink) {
            return new ForwardingSink(sink) {
                //当前写入字节数
                long bytesWritten = 0L;
                //总字节长度，避免多次调用contentLength()方法
                long contentLength = 0L;

                @Override
                public void write(@NotNull Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);
                    if (contentLength == 0) {
                        contentLength = contentLength();
                    }
                    bytesWritten += byteCount;
                    mCallBack.onUploading(contentLength, bytesWritten);
                }
            };
        }
    }

    private <T> RetrofitService getRetrofitService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://122.51.191.103:8080/serverdemo/")
                .baseUrl("http://192.168.104.9:8080/serverdemo/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .client(builder.build())
                .build();
        return retrofit.create(RetrofitService.class);
    }
}
```
### Retrofit的原理分析
#### 1. 动态代理
```java
public class DynamicProxyNewActivity extends AppCompatActivity {

    private static final String TAG = "DynamicProxyNewActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_new);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_dynamic)
    public void onDynamicProxyClick() {
        Student student = new Student();
        IStudy study = (IStudy) Proxy.newProxyInstance(student.getClass().getClassLoader(), new Class[]{IStudy.class}, new StudentHandler(student));
        // 用IStudy这个代理对象代理原来的student对象
        study.study();
    }

    interface IStudy {
        void study();
    }

    static class Student implements IStudy {

        @Override
        public void study() {
            Log.e(TAG, "study: 我要好好学习，天天向上");
        }
    }

    static class StudentHandler implements InvocationHandler {
        private Student student;

        public StudentHandler(Student student) {
            this.student = student;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            preStudy();
            Log.e(TAG, "invoke: methodName " + method.getName());
            Object result = method.invoke(student, args);
            postStudy();
            return result;
        }

        private void preStudy(){
            Log.e(TAG, "preStudy: 我要好好看书学习");
        }

        private void postStudy(){
            Log.e(TAG, "postStudy: 好好学习，迎娶白富美");
        }
    }
}
```
输出结果：
```java
2020-07-15 18:03:35.199 22282-22282/com.lucky.androidlearn E/DynamicProxyNewActivity: preStudy: 我要好好看书学习
2020-07-15 18:03:35.199 22282-22282/com.lucky.androidlearn E/DynamicProxyNewActivity: invoke: methodName study
2020-07-15 18:03:35.199 22282-22282/com.lucky.androidlearn E/DynamicProxyNewActivity: study: 我要好好学习，天天向上
2020-07-15 18:03:35.199 22282-22282/com.lucky.androidlearn E/DynamicProxyNewActivity: postStudy: 好好学习，迎娶白富美
```
#### 2. Retroif的create方法
