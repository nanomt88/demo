package com.nanomt88.risk;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单
 *
 * @author nanomt88@gmail.com
 * @create 2017-09-12 21:07
 **/
public class Order implements Serializable {
    private static final long serialVersionUID = 6588764118347599792L;
    private Long id;
    private String orderId;
    private String merchantId;
    private String merchantName;
    private String merchantOrderNo;
    private String bankCode;
    private String bankName;
    private String bankAccountName;
    private String bankAccountNo;
    private BigDecimal amount;
    private String accountType;
    private String idCardType;
    private String idCardNo;
    private String ip;
    private String mobile;
    private String comment;
    private int date;
    private Date createTime;
    private Date lastUpdateTime;

    private RiskStatus riskStatus;
    private String riskCheckMessage;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public RiskStatus getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(RiskStatus riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getRiskCheckMessage() {
        return riskCheckMessage;
    }

    public void setRiskCheckMessage(String riskCheckMessage) {
        this.riskCheckMessage = riskCheckMessage;
    }

    public void setRiskCheckResult(RiskStatus riskStatus, String riskCheckMessage) {
        this.riskStatus = riskStatus;
        this.riskCheckMessage = riskCheckMessage;
    }
}
