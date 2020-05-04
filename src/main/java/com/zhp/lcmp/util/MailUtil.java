package com.zhp.lcmp.util;


import com.sun.mail.util.MailSSLSocketFactory;
import com.zhp.lcmp.entity.UserEntity;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * 邮件工具类
 *
 * @author ZhaoHP
 * @date 2020/5/3 22:27
 */
public class MailUtil {

    private static final String DEFAULT_CONTENT = "text/html;charset=UTF-8";
    private static final String DEFAULT_SEND_MAIL_ACCOUNT = "1939555065@qq.com";
    private static final String MAIL_AUTHORIZATION_CODE = "xvnhjencdfhwcghf";
    private static final String ACTIVATION_URL = "http://localhost:8089/activationAccount?userId=";
    private static final String IDENTIFYING_CODE = "&identifyingCode=";
    private static Properties properties = new Properties();

    static {
        properties.setProperty("mail.host", "smtq.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");

        try {
            MailSSLSocketFactory factory = new MailSSLSocketFactory();
            factory.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.socketFactory", factory);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

    }

    public static void sendMail(String mailAccount, UserEntity userEntity) {


        Transport transport = null;
        try {
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(DEFAULT_SEND_MAIL_ACCOUNT, MAIL_AUTHORIZATION_CODE);
                }
            });
            session.setDebug(true);
            transport = session.getTransport();
            transport.connect("smtp.qq.com", DEFAULT_SEND_MAIL_ACCOUNT, MAIL_AUTHORIZATION_CODE);
            MimeMessage mimeMessage = getMimemessage(mailAccount, session, userEntity);
            //发送邮件
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                transport.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }
    }

    private static MimeMessage getMimemessage(String mailAccount, Session session, UserEntity userEntity) {
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            //邮件发送人
            mimeMessage.setFrom(new InternetAddress(DEFAULT_SEND_MAIL_ACCOUNT));
            //邮件接收人
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailAccount));
            //邮件标题
            mimeMessage.setSubject("激活邮件");
            //拼接邮件内容
            String mailContent = getMailContent(userEntity);
            //邮件内容
            mimeMessage.setContent(mailContent, DEFAULT_CONTENT);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return mimeMessage;
    }

    private static String getMailContent(UserEntity userEntity) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("欢迎使用Linux配置文件管理系统，点击")
                .append("<b><a href=").append(ACTIVATION_URL).append(userEntity.getId())
                .append(IDENTIFYING_CODE).append(userEntity.getIdentifyingCode()).append(">链接</a></b>")
                .append("激活您的账号");
        return buffer.toString();
    }

    // public static void main(String[] args) {
    //     UserEntity userEntity = new UserEntity();
    //     userEntity.setId(1);
    //     userEntity.setIdentifyingCode("123456789789456123");
    //     sendMail("loner_0215@163.com", userEntity);
    // }
}
