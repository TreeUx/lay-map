package com.bx.gis.service;

/**
 * @Description 发送邮件
 * @Author Barton
 * @Date 2019/3/27
 * @Version V1.0
 **/
public interface MailService {

    /**
     * 发送普通文本
     * @param to
     * @param subject
     * @param content
     */
    public boolean sendSimpleMail(String to, String subject, String content);

    /**
     * 发送 html 代码的邮件
     * @param to
     * @param subject
     * @param content
     */
    public void sendHtmlMail(String to, String subject, String content);

    /**
     * 发送附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param filePath
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath);

    /**
     * 发送内嵌的文件
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     * @param rscId
     */
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId);

}