package com.demo;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/4/12 下午2:42
 * @Description:
 *
 *
 */

public class Test {


    public static void main(String[] args) {

        test1();
        test2();

    }
    
    public static void test1(){
        long time = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            Orders s = new Orders();
            String id = "11111";
            s.setMerchantOrderNo(id);
            s.setOrderId(id);
            s.setReturnDate(new Date());
            s.setAmount(1000D);
            s.setFee(1000D);
            s.setExtra1("extra1");
            s.setExtra2("extra1");
            s.setExtra3("extra1");
        }
        long end = System.currentTimeMillis();
        System.out.println("test1耗时：" + (end - time));
    }

    public static void test2(){
        long time = System.currentTimeMillis();
        Orders[] orders = new Orders[100];
        for (int i = 0; i < orders.length; i++) {
            orders[i] = new Orders();
        }

        for (int i = 0; i < 100000000; i++) {
            Orders s = orders[i%100];
            //s.clear(); 调用clear()方法会大大的减慢速度。。。
            String id = "11111";
            s.setMerchantOrderNo(id);
            s.setOrderId(id);
            s.setReturnDate(new Date());
            s.setAmount(1000D);
            s.setFee(1000D);
            s.setExtra1("extra1");
            s.setExtra2("extra1");
            s.setExtra3("extra1");
        }
        long end = System.currentTimeMillis();
        System.out.println("test2耗时：" + (end - time));
    }
}

class Orders {
    private String merchantOrderNo;
    private String orderId;
    private Date returnDate;
    private Double amount;
    private Double fee;
    private String extra1;
    private String extra2;
    private String extra3;

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
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

//    public void clear(){
//        merchantOrderNo = null;
//        orderId = null;
//        returnDate = null;
//        amount = null;
//        fee = null;
//        extra1 = null;
//        extra2 = null;
//        extra3 = null;
//    }
}
