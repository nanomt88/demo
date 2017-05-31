package com.nanomt88.demo.dto;

/**
 * Created by ZBOOK-17 on 2017/5/30.
 */
public class MailMessage {

    /** 发件人 **/
    private String from;
    /** 收件人 **/
    private String to;
    /** 主题 **/
    private String subject;
    /** 邮件内容 **/
    private String content;

    public MailMessage(){}

    public MailMessage(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
