
- [OkHttpClient](#okhttpclient)
  - [OKHttpClient常见API的使用](#okhttpclient常见api的使用)
    - [1. OkHttpClient的构建过程](#1-okhttpclient的构建过程)
    - [2. OkHttpClient的GET请求](#2-okhttpclient的get请求)
    - [3. OkHttpClient的POST请求](#3-okhttpclient的post请求)
    - [4. OkHttpClient的文件上传](#4-okhttpclient的文件上传)
    - [5. OkHttpClient的文件下载](#5-okhttpclient的文件下载)
    - [6. OkHttpClient添加Header](#6-okhttpclient添加header)
    - [7. OkHttpClient添加网络日志拦截器](#7-okhttpclient添加网络日志拦截器)
    - [8. 添加Cookie](#8-添加cookie)
    - [9. OkHttp请求缓存](#9-okhttp请求缓存)
    - [10. OkHttp请求取消](#10-okhttp请求取消)
  - [OKHttpClient的框架结构](#okhttpclient的框架结构)
  - [OkHttpClient的拦截器](#okhttpclient的拦截器)
## OkHttpClient
### OKHttpClient常见API的使用
#### 1. OkHttpClient的构建过程
```java
//访问超时
private static final int TIME_OUT = 60;
private static OkHttpClient httpClient = new OkHttpClient.Builder()
        .addInterceptor(new HttpInterceptor()) // 自定义的拦截器
        .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() { // 添加日志拦截器
            @Override
            public void log(String message) {
                KLog.e(TAG, message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BASIC)) // 设置日志拦截器的级别
        .sslSocketFactory(SSLSocketFactoryUtil.createSSLSocketFactory())// 设置Https连接 
        .readTimeout(TIME_OUT, TimeUnit.SECONDS) // 设置读取超时时间
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS) // 设置连接超时时间
        .build();
```
#### 2. OkHttpClient的GET请求
```java
// 1. OkhttpClient的异步调用
OkHttpClient client = new OkHttpClient();
Request request = new Request.Builder().url("http://ip-api.com/json/?lang=zh-CN").build();
Call call = client.newCall(request);
call.enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG, "onFailure: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        ResponseBody responseBody = response.body();
        try {
            if (responseBody != null) {
                String body = responseBody.string();
                Log.e(TAG, "onResponse: " + body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
});
```
输出结果：
```java
{"status":"success","country":"中国","countryCode":"CN","region":"BJ","regionName":"北京市","city":"Jinrongjie","zip":"","lat":39.9174,"lon":116.361,"timezone":"Asia/Shanghai","isp":"China Unicom Beijing Province Network","org":"","as":"AS4808 China Unicom Beijing Province Network","query":"61.135.125.14"}
```
```java
//2. OkHttpClient的同步调用 需要在工作线程中调用
OkHttpClient client = new OkHttpClient();
Request request = new Request.Builder().get().url("http://ip-api.com/json/?lang=zh-CN").build();
Call call = client.newCall(request);
Response response = call.execute(); // 同步调用 
String responseResult = response.body().string();
Log.e(TAG, "onHttpGetExecuteClick: "+responseResult);
```
输出结果：
```java
{"status":"success","country":"中国","countryCode":"CN","region":"BJ","regionName":"北京市","city":"Jinrongjie","zip":"","lat":39.9174,"lon":116.361,"timezone":"Asia/Shanghai","isp":"China Unicom Beijing Province Network","org":"","as":"AS4808 China Unicom Beijing Province Network","query":"61.135.125.14"}
```
#### 3. OkHttpClient的POST请求
```java
/**
* Post请求
* RequestBody的数据格式都要指定Content-Type, 常见的有三种
* 1. application/x-www-form-urlencoded 数据是个普通表单
* FormBody是继承自RequestBody, 用于发普通的键值对，FormBody
* 已经指定了数据类型为 application/x-www-form-urlencoded
* <p>
* 2. application/json 数据格式是json格式的
* <p>
* 创建RequestBody时就不能用FormBody,要用RequestBody.Create()
* <p>
* MediaType mediaType = MediaType.parse("application/json");
* RequestBody body = RequestBody.Create(mediaType, "要发送的json字符串");
* <p>
* 3. multipart/form-data 数据里有文件
* <p>
* 这个时候还有新的东西 MultipartBody
* 如下面的例子中的 image/png 还可以换成其他的如
* 一种是 multipart/form-data, 一种是text/plain;charset=utf-8等
* RequetBody requestBody = new MultipartBody.Builder()
* .setType(MultipartBody.FORM)
* .addFormDataPart("file", file.getName(),
* RequestBody.create(MediaType.parse("image/png"), file))
* .build();
* <p>
* 如果一次上传多个文件，可重复利用addFormDataPart方法
* <p>
* RequetBody requestBody = new MultipartBody.Builder()
* .setType(MultipartBody.FORM)
* .addFormDataPart("file", file.getName(),
* RequestBody.create(MediaType.parse("image/png"), file))
* .addFormDataPart("file", file.getName(),
* RequestBody.create(MediaType.parse("image/png"), file))
* .build();
*/
```
```java
// 1. 表单提交
String url = "http://ip.taobao.com/service/getIpInfo.php";
String ip = "123.139.19.151";
OkHttpClient client = new OkHttpClient();
// 请求参数 参数
RequestBody requestBody = new FormBody.Builder()
        .add("ip", ip)
        .build();
Request request = new Request.Builder()
        .url(url)
        .post(requestBody)
        .build();
Call call = client.newCall(request);
call.enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG, "onFailure: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String result = response.body().string();
        Log.e(TAG, "onResponse: " + result);
    }
});

// 2. 上传JSON格式数据
OkHttpClient client = new OkHttpClient();
// 请求参数 参数
MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
RequestBody requestBody = RequestBody.create(mediaType, "json字符串");
Request request = new Request.Builder()
        .url("")
        .post(requestBody)
        .addHeader("", "")
        .addHeader("", "")
        .build();
Call call = client.newCall(request);
call.enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG, "onFailure: POST-JSON " + e.getMessage());
  
    @Override
    public void onResponse(Call call, Response response) throws IOExceptio
    }
});
```
#### 4. OkHttpClient的文件上传
```java
File file = new File("", "");
OkHttpClient client = new OkHttpClient();
// 请求参数 参数
RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("phone", "128983923")
        .addFormDataPart("headImage", "imageName", RequestBody.create(MediaType.parse("image/png"), file))
        .build();
Request request = new Request.Builder()
        .url("")
        .post(requestBody)
        .addHeader("", "")
        .addHeader("", "")
        .build();
Call call = client.newCall(request);
call.enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG, "onFailure: POST-JSON " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }
});
```
#### 5. OkHttpClient的文件下载
```java
String imageUrl = "http://dimg04.c-ctrip.com/images/vacations/images2/146/10272/10272_1_s43365_C_500_280.jpg";
OkHttpClient client = new OkHttpClient();
Request request = new Request.Builder().url(imageUrl).build();
Call call = client.newCall(request);
call.enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
    
    }
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        OkHttpLearnActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageViewShow.setImageBitmap(bitmap);
            }
        });
    }
});
```
#### 6. OkHttpClient添加Header
如上面的例子所示，可以在Request中添加Header, 还可以在拦截器中添加Header信息
```java
public class HttpInterceptor implements Interceptor {
    private static final String TAG = "HttpInterceptor";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = RepaymentSharePreferManager.getInstance().getToken();
        String guid = RepaymentSharePreferManager.getInstance().getGuid();
        String netWorkIP = RepaymentCache.getInstance().getNetWorkIP();
        KLog.e(TAG, "intercept: token " + token + "  guid  " + guid + " ip " + netWorkIP + " isConnected ");
        Request.Builder builder = chain.request().newBuilder();
        Request request = builder
                .addHeader("Content-type", "application/json")
                .addHeader("token", token)
                .addHeader("guid", guid)
                .addHeader("ip", netWorkIP)
                .build();
        Response response = chain.proceed(request);
        if (BuildConfig.DEBUG) {
            printResponse(response);
        }
        return response;
    }

    // {"code":"402","msg":"您的账号已在其他地方登录，如果不是本人操作，请及时修改密码"}
    private void printResponse(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        KLog.e("<-- "
                + response.code()
                + ' '
                + response.message()
                + ' '
                + response.request().url()
        );
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        if (contentLength != 0) {
            String responseBodyContent = buffer.clone().readString(charset);
            KLog.e("返回结果 " + responseBodyContent);
        }
        KLog.e("<-- END HTTP (" + buffer.size() + "-byte body)");
    }
}
// 最后在添加创建OkHttpClient对象的时候添加Inteceptor
OkHttpClient httpClient = new OkHttpClient.Builder()
    .addInterceptor(new HttpInterceptor()) // 自定义的拦截器
    .build();
```
#### 7. OkHttpClient添加网络日志拦截器
```java
// 1. 添加依赖
implementation 'com.squareup.okhttp3:logging-interceptor:3.8.1'//拦截器

// 2. 添加拦截器
OkHttpClient httpClient = new OkHttpClient.Builder()
    .addInterceptor(new HttpInterceptor()) // 自定义的拦截器
    .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() { // 添加日志拦截器
            @Override
            public void log(String message) {
                KLog.e(TAG, message);
            }
    }).setLevel(HttpLoggingInterceptor.Level.BASIC)) // 设置日志拦截器的级别
    .build();

```
#### 8. 添加Cookie
```java
final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
String imageUrl = "http://dimg04.c-ctrip.com/images/vacations/images2/146/10272/10272_1_s43365_C_500_280.jpg";
OkHttpClient client = new OkHttpClient.Builder().cookieJar(new CookieJar() {
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        Log.e(TAG, "saveFromResponse: cookieManage  " + url.host());
        for (Cookie cookie : cookies) {
            Log.e(TAG, "saveFromResponse: CookieManage " + cookie.domain() + " name " + cookie.name());
        }
        cookieStore.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        return cookies == null ? new ArrayList<>() : cookies;
    }
}).build();
Request request = new Request.Builder().url(imageUrl).build();
Call call = client.newCall(request);
call.enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) {
        Log.e(TAG, "onResponse: cookie xxxx ");
    }
});
```
#### 9. OkHttp请求缓存
```java
// OkHttp缓存请求
public class OkHttpCacheActivity extends AppCompatActivity {
    private static final String TAG = "OkHttpCacheActivity";
    private OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp_cache);
        ButterKnife.bind(this);
        initOkHttp();
    }

    // 如果不想要缓存，需要强制请求网络，在可以在Request请求创建的时候添加header 然后在interceptor里获取header, 根据对应的字段对是否进行请求进行强制网络请求进行处理
    // 有网的时候缓存
    final Interceptor NetCacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            //在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0 30秒内 不会重复请求同一个接口 不会请求网络 出了30秒，则会请求网络
            int onlineCacheTime = 30;
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + onlineCacheTime)
                    .removeHeader("Pragma")
                    .build();
        }
    };

    // 无网的时候缓存
    final Interceptor OfflineCacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!isNetworkConnected(OkHttpCacheActivity.this)) {
                //离线的时候的缓存的过期时间  没有网络的情况下，600秒内缓存是有效的，出了600秒，就会出现504错误 
                int offlineCacheTime = 600;
                request = request.newBuilder()
//                        .cacheControl(new CacheControl
//                                .Builder()
//                                .maxStale(60,TimeUnit.SECONDS)
//                                .onlyIfCached()
//                                .build()
//                        ) 两种方式结果是一样的，写法不同
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)
                        .build();
            }
            return chain.proceed(request);
        }
    };

    // 请求的过程中先是经历AppInterceptor, 然后是NetWorkInterceptor
    private void initOkHttp() {
        File httpCacheDirectory = new File(getCacheDir(), "okhttpCache");
        int cacheSize = 50 * 1024 * 1024; // 50M
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                // 添加缓存文件 文件地址 /data/data/packagename/cache/okhttpcache
                .cache(new Cache(httpCacheDirectory, cacheSize)) 
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(OfflineCacheInterceptor) // 添加App缓存拦截器
                .addNetworkInterceptor(NetCacheInterceptor) // 添加网络缓存拦截器
                .build();
    }

    @OnClick(R.id.btn_fetch_cache)
    public void onFetchNetDataClick() {
        Request request = new Request.Builder().url("http://ip-api.com/json/?lang=zh-CN").build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body != null) {
                    String result = body.string();
                    Log.e(TAG, "onResponse: result " + result);
                }
            }
        });
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            //获取连接对象
            if (mNetworkInfo != null) {
                //判断是TYPE_MOBILE网络
                if (ConnectivityManager.TYPE_MOBILE == mNetworkInfo.getType()) {
                    AppLogMessageMgr.i("AppNetworkMgr", "网络连接类型为：TYPE_MOBILE");
                    //判断移动网络连接状态
                    NetworkInfo.State STATE_MOBILE = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                    if (STATE_MOBILE == NetworkInfo.State.CONNECTED) {
                        AppLogMessageMgr.i("AppNetworkMgrd", "网络连接类型为：TYPE_MOBILE, 网络连接状态CONNECTED成功！");
                        return mNetworkInfo.isAvailable();
                    }
                }
                //判断是TYPE_WIFI网络
                if (ConnectivityManager.TYPE_WIFI == mNetworkInfo.getType()) {
                    AppLogMessageMgr.i("AppNetworkMgr", "网络连接类型为：TYPE_WIFI");
                    //判断WIFI网络状态
                    NetworkInfo.State STATE_WIFI = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                    if (STATE_WIFI == NetworkInfo.State.CONNECTED) {
                        AppLogMessageMgr.i("AppNetworkMgr", "网络连接类型为：TYPE_WIFI, 网络连接状态CONNECTED成功！");
                        return mNetworkInfo.isAvailable();
                    }
                }
            }
        }
        return false;
    }
}
```
总结：
我们在平时的开发过程中，总会遇到下面的需求：

1、有网的时候也可以读取缓存，并且可以控制缓存的过期时间，这样可以减轻服务器压力
2、有网的时候不读取缓存，比如一些及时性较高的接口请求 
3、无网的时候读取缓存，并且可以控制缓存过期的时间

一般缓存数据是缓存首页的第一页数据，下拉加载更多就不需要使用缓存了，直接访问网络，可以根据情况在拦截器里进行判断处理，是否直接继续网络请求，直接调用并返回chain.process(request)。

在有网的情况下，在response中添加header, Cache-Control", "public, max-age=200", 在没有网络的情况下，在request中添加header,
Cache-Control", "public, only-if-cached, max-stale=4000"

```java
// OkHttpClient的缓存策略配置 CacheControl
CacheControl cc = new CacheControl.Builder()
    //不使用缓存，但是会保存缓存数据
    //.noCache()
    //不使用缓存，同时也不保存缓存数据
    // .noStore()
    //只使用缓存，（如果我们要加载的数据本身就是本地数据时，可以使用这个，不过目前尚未发现使用场景）
    //.onlyIfCached()
    //手机可以接收响应时间小于当前时间加上10s的响应
    //.minFresh(10,TimeUnit.SECONDS)
    //手机可以接收有效期不大于10s的响应
    //.maxAge(10,TimeUnit.SECONDS)
    //手机可以接收超出5s的响应
    .maxStale(5,TimeUnit.SECONDS)
    .build();
Request request = new Request.Builder()
     .cacheControl(cc).
     url("http://192.168.152.2:8080/cache")
     .build();

// 如果直接使用CacheControl中的常量，则不用调用上面那么多的方法，使用方式如下：
Request request = new Request.Builder()
    //强制使用网络
    // .cacheControl(CacheControl.FORCE_NETWORK)
    //强制使用缓存
    .cacheControl(CacheControl.FORCE_CACHE)
    .url("http://192.168.152.2:8080/cache")
    .build();

```

#### 10. OkHttp请求取消
```java
Call call = okhttpClient.newCall(request);
call.cancel() //可以用于取消请求
```
### OKHttpClient的框架结构
OkHttpClient的整体架构比较大，后面找时间在详细整理吧

### OkHttpClient的拦截器
OkHttpClient中的拦截器算是比较有意思的部分，通过拦截器，我们可以做网络请求的日志拦截，还可以进行网络数据的缓存，动态添加Header等等。


