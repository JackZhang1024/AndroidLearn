package com.lucky.androidlearn.hybrid;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻详情图文混排
 */
public class WebViewNewsDetailActivity extends AppCompatActivity {
    private static final String TAG = "WebViewNewsDetailActivi";

    @BindView(R.id.webview)
    InnerWebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        FavorModel model = new FavorModel("123dcde2323", "1");
        mWebView.setFavorJavaScript("FavorModel", model);
        loadLocalNewsDetail();
    }

    private void loadLocalNewsDetail() {
        try {
            // 读取来自assets的信息 实际操作 _newsContent 来自网络
            //String _newsContent = getStringFromAssets("test.html");
            String newsContent = newsDetail;
            // 这是本地的html模板
            String htmlContent = getStringFromAssets("newsdetail.html");
            // 替换文本
            String newsPublishTime = "2018-03-23 08:00";
            String newsTitle = "测试Html图文混排哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈或或或或或或或或或或或或或或或或或或或或或或";
            // 本地html
            String localPath = "file:///android_asset/";
            String data = htmlContent.replace("#title#", newsTitle)
                    .replace("#author#", "百度")
                    .replace("#time#", newsPublishTime)
                    .replace("#content#", newsContent);
            Log.e(TAG, "loadLocalNewsDetail: "+data);
            // 替换信息加载到html模板中
            mWebView.loadDataWithBaseURL(
                    localPath, data
                    , "text/html",
                    "utf-8", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void executeJavaScript() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String jsFunction = "function changeName(name){model.name=name; alert(model.name)}; changeName('Han')";
                mWebView.loadUrl("javascript:" + jsFunction);
            }
        }, 1000);
    }

    String getStringFromAssets(String path) throws IOException {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open(path);
        return inputStream2String(inputStream);

    }

    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }


    String newsDetail = "<div class=\"detail-body photos\" style=\"font-family: 微软雅黑; font-size: 13pt; color:#000;background-color: #FFFFFF;line-height: 1.8\">\n" +
            "    <p>公司名称：中付</a>支付</p><p>公司全称：中付支付</a>科技有限公司</p><p>有无支付牌照：有</p><p>现行费率：0.68%+3</p><p>成立时间：2007年</p><p>公司地址：深圳市福田中心区卓越时代广场第48层04-08单元</p><p>业务类型：银行卡收单，互联网支付</p><p>业务范围：全国</p><p>备付金：未知</p><p>主要品牌：新中付</a>，中付</p><p><img src=\"http://res.xuexiba.net/uploads/20190223/5c715aece184ewezojlthho.png\" title=\"中付支付.png\" alt=\"中付支付.png\"/></p><p>点评：中付支付是深圳一家支付公司，之前是深圳的支付牌照，也是后面拓展到全国的，自选商户也是产品的一大亮点，大额交易经常会延迟到账，或者需要提交各种认证资料，比较繁琐，新中付MPOS</a>市场热度不错。</p><p style=\"white-space: normal;\">公司简介：中付支付科技有限公司成立于2007年，注册资本人民币16,100万元，是国内领先的独立第三方支付</a>企业，根据《中国人民银行信息安全管理规定》，中付支付系统符合《支付卡行业数据安全标准》V1.2及V2.0的相关要求，获得中国信息安全认证中心颁发的《支付系统安全认证证书》。公司的宗旨是为各类企业及个人提供安全、便捷和保密的综合电子支付服务。</p><p style=\"white-space: normal;\">公司拥有由互联网行业资深创业者、优秀金融界人士和顶尖技术人员所组成的国际化管理团队，在产品开发、技术创新、市场开拓、企业管理和资本运作等方面都具有丰富的经验。</p><p style=\"white-space: normal;\">公司的业务范围涵盖网上支付、预付卡积分支付、银行卡X收单、支付清算、小额代发工资业务以及商户推广等，为客户提供各种专业的支付结算解决方案。</p><p style=\"white-space: normal;\">凭借稳健的作风、先进的技术、敏锐的市场洞察力以及极大的社会责任感，我们赢得了银联以及银行等金融机构认同与支持。成为众多金融机构在电子支付领域信任可靠的合作伙伴。</p><p style=\"white-space: normal;\">公司名称：中付支付科技有限公司</p><p style=\"white-space: normal;\">通讯地址：深圳市福田中心区卓越时代广场第48层04-08单元</p><p style=\"white-space: normal;\">邮政编码：518000联系电话：4007006889传真号码：755-82235346</p><p>中付支付牌照信息：</p><p><img src=\"http://res.xuexiba.net/uploads/20190121/5c45daa23e51chifcwcwraj.png\" style=\"\" title=\"中天中付2.png\"/></p><p><img src=\"http://res.xuexiba.net/uploads/20190121/5c45daa2501b4ouyottedth.png\" style=\"\" title=\"中付支付1.png\"/></p><p><br/></p>                </div>\n" +
            "</div>";
}
