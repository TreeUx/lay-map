package com.bx.gis.common;

import java.util.Properties;
import java.util.Random;


import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
  * @Author Breach
  * @Description 发送图片及附件到email
  * @Date 2019/2/22
  */
public class ImgMailUtil {
    private static final String BX_CODE = "您好，感谢您使用伴行计调管理系统！本次验证码为：";
    // JavaMail需要Properties来创建一个session对象。它将寻找字符串"mail.smtp.host"，属性值就是发送邮件的主机.
    public static boolean send_mail(String ecode, String acceptMail) throws MessagingException {
        Properties properties = new Properties();
        // properties.put("mail.smtp.host", "mailcas.chinapnr.com", "smtp.163.com");// 设置smtp主机
        properties.put("mail.smtp.host", "smtp.163.com");// 设置smtp主机
        properties.put("mail.smtp.auth", "true");// 使用smtp身份验证
        /*
         * 在 JavaMail 中，可以通过 extends Authenticator 抽象类，在子类中覆盖父类中的
         * getPasswordAuthentication() 方法，就可以实现以不同的方式来进行登录邮箱时的用户身份认证。JavaMail
         * 中的这种设计是使用了策略模式（Strategy
         */
        MimeMessage message = new MimeMessage(Session.getInstance(properties,
                new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("pzd21966@163.com","DBnj21966");//发送邮件的账号及授权码
                    }
                }));
        // 设置邮件的属性
        // 设置邮件的发件人
        message.setFrom(new InternetAddress("pzd21966@163.com"));
        // 设置邮件的收件人 cc表示抄送 bcc 表示暗送
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(acceptMail)); //acceptMail接收方邮箱地址
        // 设置邮件的主题
        message.setSubject("伴行平台");
        // 创建邮件的正文
        MimeBodyPart text = new MimeBodyPart();
        // setContent(“邮件的正文内容”,”设置邮件内容的编码方式”)
        text.setContent(BX_CODE + ecode + Common.CHICKEN_SOUP[0]  + Common.CHICKEN_SOUP[(int) (Math.random()*50)] + "<img src='cid:a'>",
                "text/html;charset=gb2312");

        // 点到点的发送
        // 一对多发送只要改一个地方如下：
        // // 构建一个群发地址数组
        // InternetAddress[] adr=new InternetAddress[toMore.length];
        // for(int i=0;i<toMore.length;i++){ adr[i]=new
        // InternetAddress(toMore[i]); }
        // // Message的setRecipients方法支持群发。。注意:setRecipients方法是复数和点 到点不一样
        // message.setRecipients(Message.RecipientType.TO,adr);

        // 创建图片
        MimeBodyPart img = new MimeBodyPart();
        /*
         * JavaMail API不限制信息只为文本,任何形式的信息都可能作茧自缚MimeMessage的一部分.
         * 除了文本信息,作为文件附件包含在电子邮件信息的一部分是很普遍的. JavaMail
         * API通过使用DataHandler对象,提供一个允许我们包含非文本BodyPart对象的简便方法.
         */
        DataHandler dh = new DataHandler(new FileDataSource("src//main//resources//static//emimg//shouhui_siji_jingse-0" + (int) (Math.random()*30+1) + ".jpg"));//图片路径
        img.setDataHandler(dh);
        // 创建图片的一个表示用于显示在邮件中显示
        img.setContentID("a");

        MimeBodyPart img2 = new MimeBodyPart();
        DataHandler dh2 = new DataHandler(new FileDataSource("src//main//resources//static//emimg//shouhui_siji_jingse-0" + (int) (Math.random()*30+1) + ".jpg"));//第二张图片路径
        img2.setDataHandler(dh2);
        img2.setContentID("b");
        // 创建附件
        // MimeBodyPart attch = new MimeBodyPart();
        // DataHandler dh1 = new DataHandler(new FileDataSource("src//b.jpg"));
        // attch.setDataHandler(dh1);
        // String filename1 = dh1.getName();
        // MimeUtility 是一个工具类，encodeText（）用于处理附件字，防止中文乱码问题
        // attch.setFileName(MimeUtility.encodeText(filename1));
        // 关系 正文和图片的
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        mm.addBodyPart(img);
        mm.setSubType("related");// 设置正文与图片之间的关系
        // 图班与正文的 body
        MimeBodyPart all = new MimeBodyPart();
        all.setContent(mm);
        // 附件与正文（text 和 img）的关系
        MimeMultipart mm2 = new MimeMultipart();
        mm2.addBodyPart(all);
        mm2.addBodyPart(img2);
        mm2.setSubType("mixed");// 设置正文与附件之间的关系


        message.setContent(mm2);
        message.saveChanges(); // 保存修改


        Transport.send(message);// 发送邮件
        System.out.println("邮件发送成功");
        return true;
    }
    //测试
    public static void main(String[] args) throws Exception {
        String mailCode = String.valueOf(new Random().nextInt(899999) + 100000); //随机6位验证码
        boolean bl = false;
        String acceptMail = "1067747218@qq.com";
        bl = ImgMailUtil.send_mail(mailCode, acceptMail);
        System.out.println(bl);
    }
}
