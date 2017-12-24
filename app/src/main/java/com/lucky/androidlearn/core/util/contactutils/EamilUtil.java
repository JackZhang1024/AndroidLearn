package com.lucky.androidlearn.core.util.contactutils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 此类用来发送错误报告
 *
 * @author Minimax
 */
public class EamilUtil {

    /**
     * 通过Intent发送邮件到制定的邮箱 经过测试貌似不可以使用啊
     * @param context
     */
    public static void SendEmail(Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:1120335370@qq.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "邮件主题名字");
        intent.putExtra(Intent.EXTRA_TEXT, "邮件的内容");
        context.startActivity(intent);
    }

    /**
     * 这个类是网上传的东西，可以发送邮件到指定的邮箱地址
     *
     * @param context
     * @param mailcontext
     */
    public static void sendEmailToMe(Context context, String mailcontext) {
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.qq.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUserName("1120335370@qq.com"); // 你的邮箱地址
        mailInfo.setPassword("asdzxc123456");// 您的邮箱密码
        mailInfo.setFromAddress("");
        mailInfo.setToAddress("1120335370@qq.com");
        mailInfo.setSubject("发送的异常信息,哦哦哦");
        mailInfo.setContent(mailcontext);
        // 这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        sms.sendTextMail(mailInfo);// 发送文体格式
        // sms.sendHtmlMail(mailInfo);//发送html格式
    }

    /**
     * 这个方法是未封装的对象
     *
     * @throws AddressException
     * @throws MessagingException
     */
    public void sendEmail_Method_1() throws AddressException, MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");// 发送邮件协议
        properties.setProperty("mail.smtp.auth", "true");// 需要验证
        Session session = Session.getInstance(properties);
        session.setDebug(true);// debug模式
        // 邮件信息
        Message messgae = new MimeMessage(session);
        messgae.setFrom(new InternetAddress("zhangfengzhou@sina.cn"));
        messgae.setText("这是邮件内容，啊啊啊啊啊");// 设置邮件内容
        messgae.setSubject("这是邮件主题，哦哦哦哦哦哦");// 设置邮件主题
        // 发送邮件
        Transport tran = session.getTransport();
        tran.connect("smtp.sina.cn", 25, "zhangfengzhou@sina.cn", "asdzxc123456");//
        // 连接到新浪邮箱服务器
        tran.sendMessage(messgae, new Address[]{new InternetAddress("1120335370@qq.com")});// 设置邮件接收人
        tran.close();
    }

    /**
     * 这个是封装好的
     */
    public void sendEmail_Method_2() {

        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailprotocol("smtp");
        mailInfo.setMailServerHost("smtp.sina.cn");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUserName("zhangfengzhou@sina.cn");
        // 你的邮箱地址
        mailInfo.setPassword("asdzxc123456");// 您的邮箱密码
        mailInfo.setFromAddress("zhangfengzhou@sina.cn");
        mailInfo.setToAddress("1120335370@qq.com");
        mailInfo.setSubject("这是邮件的标题,哦哦哦哦哦");
        mailInfo.setContent("这是邮件的内容，啊啊啊啊"); // 这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        sms.sendTextMail(mailInfo);// 发送文体格式
        // sms.sendHtmlMail(mailInfo);//发送html格式
    }


    /**
     * 发送复杂的邮件(文本内容，附件，图片)
     */
    public void sendEmail_Mehtod_3() {
        try {
            // 发送邮件的协议
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.auth", "true");// 设置验证机制
            properties.setProperty("mail.transport.protocol", "smtp");// 发送邮件协议
            properties.setProperty("mail.smtp.host", "smtp.sina.com");// 设置邮箱服务器地址
            properties.setProperty("mail.smtp.port", "25");
            Session session = Session.getInstance(properties, new MyAuthenticator());
            session.setDebug(true);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("whyao@sina.cn"));
            message.setSubject("一封复杂的邮件");
            message.setRecipients(MimeMessage.RecipientType.TO,
                    InternetAddress.parse("michael8@vip.qq.com"));// 接收人
            message.setRecipients(MimeMessage.RecipientType.CC,
                    InternetAddress.parse("1348800595@qq.com"));// 抄送人
            message.setRecipients(MimeMessage.RecipientType.BCC,
                    InternetAddress.parse("1348800595@qq.com"));// 密送人
            MimeBodyPart bodyPartAttch = createAttachMent("C:\\Users\\Administrator\\Desktop\\mail.jar");// 附件
            MimeBodyPart bodyPartContentAndPic = createContentAndPic(
                    "I just want to Fuck",
                    "C:\\Users\\Administrator\\Desktop\\0.jpg");// 文本内容
            MimeMultipart mimeMuti = new MimeMultipart("mixed");
            mimeMuti.addBodyPart(bodyPartAttch);
            mimeMuti.addBodyPart(bodyPartContentAndPic);
            message.setContent(mimeMuti);
            message.saveChanges();
            // message.setContent("Michael", "text/html;charset=gbk");
            Transport.send(message);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    // 创建附件
    public static MimeBodyPart createAttachMent(String path)
            throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        FileDataSource dataSource = new FileDataSource(new File(path));
        mimeBodyPart.setDataHandler(new DataHandler(dataSource));
        mimeBodyPart.setFileName(dataSource.getName());
        return mimeBodyPart;
    }

    // 创建文本和图片
    public static MimeBodyPart createContentAndPic(String content, String path) throws MessagingException {
        MimeMultipart mimeMutiPart = new MimeMultipart("related");
        // 图片
        MimeBodyPart picBodyPart = new MimeBodyPart();
        FileDataSource fileDataSource = new FileDataSource(new File(path));
        picBodyPart.setDataHandler(new DataHandler(fileDataSource));
        picBodyPart.setFileName(fileDataSource.getName());
        mimeMutiPart.addBodyPart(picBodyPart);
        // 文本
        MimeBodyPart contentBodyPart = new MimeBodyPart();
        contentBodyPart.setContent(content, "text/html;charset=gbk");
        mimeMutiPart.addBodyPart(contentBodyPart);
        // 图片和文本结合
        MimeBodyPart allBodyPart = new MimeBodyPart();
        allBodyPart.setContent(mimeMutiPart);
        return allBodyPart;
    }
}
