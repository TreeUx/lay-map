package com.bx.gis.common;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

/**
  * @Author Breach
  * @Description 邮件工具类
  * @Date 2019/2/22
  */
public class MailUtil {
    private static final String BX_CODE = "您好，感谢您使用伴行计调管理系统！此次验证码为：";
    /**
     * 发送邮件
     * @param to 给谁发
     * @param text 发送内容
     */
    public static void send_mail(String to,String text) throws MessagingException {
        //创建连接对象 连接到邮件服务器
        Properties properties = new Properties();
        //设置发送邮件的基本参数
        //发送邮件服务器(注意，此处根据你的服务器来决定，如果使用的是QQ服务器，请填写smtp.qq.com)
        properties.put("mail.smtp.host", "smtp.163.com");
        //发送端口（根据实际情况填写，一般均为25）
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");
        //设置发送邮件的账号和授权码（授权码在邮箱设置里获取）
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //两个参数分别是发送邮件的账户和授权码(注意，如果配置后不生效，请检测是否开启了 POP3/SMTP 服务，QQ邮箱对应设置位置在: [设置-账户-POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务])
//                return new PasswordAuthentication("1067747218@qq.com","qhveyzyrprpibbae"); //发送邮件的账号及授权码
                return new PasswordAuthentication("pzd21966@163.com","DBnj21966"); //发送邮件的账号及授权码
            }
        });

        //创建邮件对象
        Message message = new MimeMessage(session);
        //设置发件人
        message.setFrom(new InternetAddress("pzd21966@163.com"));
        //设置收件人
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
        //设置主题
        message.setSubject("伴行平台");
        //设置邮件正文  第二个参数是邮件发送的类型
        message.setContent(text,"text/html;charset=UTF-8");
        //发送一封邮件
        Transport.send(message);
    }

    //测试
    public static void main(String[] args) {
        String mailCode = String.valueOf(new Random().nextInt(899999) + 100000); //随机6位验证码
        try {
            // 请将此处的 1067747218@qq.com 替换为您的收件邮箱号码
            MailUtil.send_mail("1067747218@qq.com", BX_CODE + mailCode); //mailCode 获取随机6位验证码
            System.out.println("邮件发送成功!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
