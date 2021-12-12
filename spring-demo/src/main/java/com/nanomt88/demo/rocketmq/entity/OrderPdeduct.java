package com.nanomt88.demo.rocketmq.entity;

import com.lxft.nova.commons.type.pdeduct.OrderPdeductFeeType;
import com.lxft.nova.commons.type.pdeduct.OrderPdeductStatus;
import org.springframework.context.annotation.Lazy;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author hxd
 * @Time 2017/2/28 19:25
 * @Email nanomt88@gmail.com
 * @Desc 实时代扣订单表
 */
@Entity
@Lazy(value=false)
public class OrderPdeduct extends BaseEntity {

    private static final long serialVersionUID = 6502589481998718153L;

    /**
     * 内部订单号
     */
    private String orderId;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 商户订单号
     */
    private String merchantOrderNo;
    /**
     * 代扣人银行代码
     */
    private String bankCode;
    /**
     * 代扣人开户行所在省
     */
    private String bankAccountProvince;
    /**
     * 代扣人开户行所在市
     */
    private String bankAccountCity;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 代扣人开户支行名称
     */
    private String bankBranchName;
    /**
     * 代扣人开户行联行行号
     */
    private String bankBranchNo;
    /**
     * 代扣人开户户名
     */
    private String bankAccountName;
    /**
     * 代扣人银行账号
     */
    private String bankAccountNo;
    /**
     * 代扣人扣款卡有效期
     */
    private String bankAccountExpire;
    /**
     * 代扣人扣款卡CVV2
     */
    private String bankAccountCvv2;
    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 账户类型:0-对公；1-对私借记卡；2-对私贷记卡；3-对私存折
     */
    private String accountType;
    /**
     * 代扣人证件类型 : 01-身份证
     */
    private String idCardType;
    /**
     * 代扣人证件号
     */
    private String idCardNo;

    private String ip;
    /**
     * 手续费
     */
    private BigDecimal fee;
    /**
     * 手续费类型: 坐扣, 收支两条线
     */
    @Enumerated(EnumType.ORDINAL)
    private OrderPdeductFeeType feeType;
    /**
     * 实际结算金额
     */
    private BigDecimal amountSettle = BigDecimal.ZERO;
    /**
     * 通知手机号
     */
    private String mobileNo;
    /**
     * 商户保留域1
     */
    private String extra1;
    /**
     * 商户保留域2
     */
    private String extra2;
    /**
     * 商户保留域3
     */
    private String extra3;
    /**
     * 商户保留域4
     */
    private String extra4;
    /**
     * 商户保留域5
     */
    private String extra5;
    /**
     * 备注信息
     */
    private String comment;
    /**
     * 订单状态 0: FAILED 失败, 1: INIT 订单初始化入库, 2:PROCESSING 处理中 , 3: SUCCEED 渠道返回成功,
     * 4:PROCESSING_SETTLEMENT 处理中转结算，5：WAIT_REFUND 成功待退款，6：SUCCESS_REFUND 成功转退款
     */
    @Enumerated(EnumType.ORDINAL)
    private OrderPdeductStatus status;
    /**
     * 交易完成时间:渠道返回
     */
    private Date tradeTime;
    /**
     * 订单状态发生变化的时间
     */
    private Date lastStatusChangeTime;
    /**
     * 创建时间索引字段
     */
    private Long createTimeIndex;
    /**
     * 支付会计日
     */
    private int date;
    /**
     * 结算会计日
     */
    private int settleDate;

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

    public String getBankAccountProvince() {
        return bankAccountProvince;
    }

    public void setBankAccountProvince(String bankAccountProvince) {
        this.bankAccountProvince = bankAccountProvince;
    }

    public String getBankAccountCity() {
        return bankAccountCity;
    }

    public void setBankAccountCity(String bankAccountCity) {
        this.bankAccountCity = bankAccountCity;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchNo() {
        return bankBranchNo;
    }

    public void setBankBranchNo(String bankBranchNo) {
        this.bankBranchNo = bankBranchNo;
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

    public String getBankAccountExpire() {
        return bankAccountExpire;
    }

    public void setBankAccountExpire(String bankAccountExpire) {
        this.bankAccountExpire = bankAccountExpire;
    }

    public String getBankAccountCvv2() {
        return bankAccountCvv2;
    }

    public void setBankAccountCvv2(String bankAccountCvv2) {
        this.bankAccountCvv2 = bankAccountCvv2;
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

    public BigDecimal getFee() {
        return fee == null ? BigDecimal.ZERO : fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getAmountSettle() {
        return amountSettle;
    }

    public void setAmountSettle(BigDecimal amountSettle) {
        this.amountSettle = amountSettle;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public String getExtra4() {
        return extra4;
    }

    public void setExtra4(String extra4) {
        this.extra4 = extra4;
    }

    public String getExtra5() {
        return extra5;
    }

    public void setExtra5(String extra5) {
        this.extra5 = extra5;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OrderPdeductStatus getStatus() {
        return status;
    }

    public void setStatus(OrderPdeductStatus status) {
        this.status = status;
    }

    public Date getLastStatusChangeTime() {
        return lastStatusChangeTime;
    }

    public void setLastStatusChangeTime(Date lastStatusChangeTime) {
        this.lastStatusChangeTime = lastStatusChangeTime;
    }

    public Long getCreateTimeIndex() {
        return createTimeIndex;
    }

    public void setCreateTimeIndex(Long createTimeIndex) {
        this.createTimeIndex = createTimeIndex;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(int settleDate) {
        this.settleDate = settleDate;
    }

    public OrderPdeductFeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(OrderPdeductFeeType feeType) {
        this.feeType = feeType;
    }
}