package com.nanomt88.demo.rocketmq;

import com.lxft.nova.boss.lib.BossUtil;
import com.lxft.nova.boss.lib.bean.BillRequest;
import com.lxft.nova.boss.lib.bean.RealtimeChargeClearing;
import com.lxft.nova.boss.lib.bean.RealtimeChargeClearingResponse;
import com.lxft.nova.boss.lib.bean.base.BaseResponse;
import com.lxft.nova.commons.type.AccountType;
import com.lxft.nova.commons.type.ClearingType;
import com.lxft.nova.commons.type.pdeduct.OrderPdeductFeeType;
import com.lxft.nova.commons.type.pdeduct.TallyType;
import com.nanomt88.demo.rocketmq.dao.OrderPdeductDao;
import com.nanomt88.demo.rocketmq.entity.OrderPdeduct;
import com.nanomt88.demo.rocketmq.service.OrderPdeductService;
import com.swwx.charm.commons.lang.utils.LogPortal;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/27 下午6:39
 * @Description:    启动后加载数据到 lucene中
 */
@Component
@Order(value=10)     //启动顺序
public class BootListener implements CommandLineRunner {

    @Autowired
    private OrderPdeductService orderPdeductService;

    @Autowired
    private Environment env;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        LogPortal.info(">>>>>>>>>>>>>>>服务启动执行，读取数据开始<<<<<<<<<<<<<");
        Thread.currentThread().sleep(3000*1 );
        //加载数据
        loading();
        LogPortal.info(">>>>>>>>>>>>>>>服务启动执行，读取数据开始<<<<<<<<<<<<<");
    }

    private void loading() throws Exception {

//        LogPortal.info("请输入需要清分记账的orderId，以逗号分隔");
//        Scanner scanner = new Scanner(System.in);
//        String orderId = scanner.nextLine();


        //读取配置
        String orderId = env.getProperty("order.id");

        if(StringUtils.isBlank(orderId)){
            LogPortal.error(">>>>>>>>>>>>>>>>>>>>未读取到配置[order.id]");
            return;
        }
        LogPortal.info(">>>>>>>>>>>>>>>>>>>>读取到订单 orderId :{}",orderId);


        String[] orderIds = orderId.split(",");

        for(String id : orderIds){
            if(StringUtils.isNotBlank(id)){
                final OrderPdeduct orders = orderPdeductService.getOne(Long.parseLong(id));

                if(orders != null){

                    LogPortal.info("查询到订单：{}, 订单详情为：{}", id, orders);

//                    LogPortal.info("请输入操作，0 - 清分和记账； 1 - 清分； 2 - 手动记账； 3 - 自动记账； 4 - 跳过输入； 其他选择退出");
//                    Scanner sc1 = new Scanner(System.in);
//                    String type = sc1.nextLine();


                    String type = env.getProperty("order_" + id);

                    if(StringUtils.isBlank(type)){
                        LogPortal.info("未读取到订单：[order_{}] 的配置，跳过该订单...", id);
                        continue;
                    }

                    type = type.trim();

                    if("0".equals(type)){
                        LogPortal.info("开始清分和记账...");
                        //清分
                        all(orders);
                    }else if("1".equals(type)){
                        LogPortal.info("开始清分...");
                        chargeClearing(orders);
                    }else if("2".equals(type)){
                        LogPortal.info("开始手动记账...");
                        chargeClearingAndBilling(orders);
                    }else if("3".equals(type)){
                        LogPortal.info("开始自动记账...");
                        billing(orders);
                    }else{
                        LogPortal.info("退出。。。");
                        System.exit(0);
                        return;
                    }

                }

            }
        }

    }

    private void  all(final OrderPdeduct orders) throws Exception {
        chargeClearing(orders);
        billing(orders);
    }

    private void chargeClearing(final OrderPdeduct finalOrder) throws Exception {
        //调用清分接口
        RealtimeChargeClearing data = new RealtimeChargeClearing();
        data.setMerchantId(finalOrder.getMerchantId());
        data.setOrderNo(finalOrder.getOrderId());
        data.setMerchantOrderNo(finalOrder.getMerchantOrderNo());
        data.setAmount(finalOrder.getAmount());
        data.setClearingType(ClearingType.REALTIME_TRADE);
        data.setAccountType(AccountType.getAccountType(finalOrder.getAccountType()));
        data.setMobile(finalOrder.getMobileNo());
        data.setCreateTime(DateFormatUtils.format(finalOrder.getCreateTime(), "yyyyMMdd"));

        RealtimeChargeClearingResponse response = BossUtil.realtimeChargeClearing(data);
        if ("CAP00000".equals(response.getReturnCode())) {

            //收到清分记账的接口返回的手续费,入库
            finalOrder.setFee(response.getFeeAmount());

            //坐扣 结算金额减去手续费
            if (OrderPdeductFeeType.ZUOKOU.getValue().toString().equals(response.getFeeType())) {
                finalOrder.setFeeType(OrderPdeductFeeType.ZUOKOU);
                finalOrder.setAmountSettle(finalOrder.getAmount().subtract(response.getFeeAmount()));
            } else if (OrderPdeductFeeType.SHOUZHI.getValue().toString().equals(response.getFeeType())) { //收支两条线:结算金额不变
                finalOrder.setAmountSettle(finalOrder.getAmount());
                finalOrder.setFeeType(OrderPdeductFeeType.SHOUZHI);
            } else {
                LogPortal.error("[ERROR_PDEDUCT_SETTLEMENT] 调用boss清分接口失败,返回手续费类型错误,订单：[{}], request:[{}], response:[{}]",
                        finalOrder, data, response);
            }
            orderPdeductService.save(finalOrder);
        }
    }

    private void chargeClearingAndBilling(final OrderPdeduct finalOrder) throws Exception {
        // 读取输入数据
//        LogPortal.info("请输入需要记账的手续费和金额，中间以逗号分隔");
//        Scanner sc1 = new Scanner(System.in);
//        String data = sc1.nextLine();


        String data = env.getProperty("fee_"+finalOrder.getId());
        if(StringUtils.isBlank(data)){
            LogPortal.info("未读取到订单[{}] 的记账配置...", finalOrder.getId());
            return;
        }

        String[] args = data.split(",");
        if(args==null || args.length != 2){
            LogPortal.error("输入的数据有误：{}", args);
            return;
        }

        LogPortal.info("读取订单[{}]记账配置，手续费：{}， 类型：{}", finalOrder.getId(), args[0] ,args[1]);

//        LogPortal.info("确认该操作请输入：Y");
//
//        Scanner sc2 = new Scanner(System.in);
//        String yes = sc1.nextLine();
//        if(!"Y".equalsIgnoreCase(yes.trim())){
//            return;
//        }

        BigDecimal feeAmount = new BigDecimal(args[0]);
        String feeType = args[1];


        //收到清分记账的接口返回的手续费,入库
        finalOrder.setFee(feeAmount);

        //坐扣 结算金额减去手续费
        if (OrderPdeductFeeType.ZUOKOU.getValue().toString().equals(feeType)) {
            finalOrder.setFeeType(OrderPdeductFeeType.ZUOKOU);
            finalOrder.setAmountSettle(finalOrder.getAmount().subtract(feeAmount));
        } else if (OrderPdeductFeeType.SHOUZHI.getValue().toString().equals(feeType)) { //收支两条线:结算金额不变
            finalOrder.setAmountSettle(finalOrder.getAmount());
            finalOrder.setFeeType(OrderPdeductFeeType.SHOUZHI);
        } else {
            LogPortal.error("读取订单记账类型：[{}], request:[{}], response:[{}]",
                    finalOrder.getId(), feeType );
            return;
        }

        orderPdeductService.save(finalOrder);

        //清分
        billing(finalOrder);

    }

    private void billing(final OrderPdeduct finalOrder) throws Exception {
        //调用记账接口
        BillRequest req = new BillRequest();
        req.setMerchantId(finalOrder.getMerchantId());
        req.setOrderId(finalOrder.getOrderId());
        req.setMerchantOrderNo(finalOrder.getMerchantOrderNo());
        req.setStatus(finalOrder.getStatus().name());
        req.setAmount(finalOrder.getAmount());
        req.setFee(finalOrder.getFee());
        req.setDate(finalOrder.getDate());
        req.setTallyType(TallyType.TRADE);
        req.setAmountSettle(finalOrder.getAmountSettle());
        BaseResponse billing = BossUtil.billing(req);
        if(billing==null || !"CAP00000".equals(billing.getReturnCode())){
            LogPortal.error("[ERROR_PDEDUCT_BILLING] 调用boss记账接口失败,订单信息[{}], request:[{}], response:[{}]",
                    finalOrder, req, billing);
        }
    }
}