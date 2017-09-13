package com.nanomt88.risk;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.storm.shade.org.apache.commons.collections.map.HashedMap;
import org.apache.storm.shade.org.eclipse.jetty.util.ajax.JSON;
import org.apache.storm.thrift.TException;
import org.apache.storm.utils.DRPCClient;

import java.math.BigDecimal;
import java.util.*;

/**
 * 本地调用远程 DRPC 服务
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-03 8:10
 **/
public class RiskCheckClient {

    private static String MERCHANT_ID = "8880000120230001";

    private static String[] IP_ADDR = new String[]{"192.168.1.100","123.130.51.220", "113.128.90.10", "1.85.221.199", "113.128.90.165", "222.141.13.184", "182.43.214.147", "175.8.110.203", "111.155.116.23", "60.23.36.164"};


    private static Random RANDOM = new Random();

    public static void main(String[] args) throws TException {
        //远程 DRPC 端口默认 3772
        Map conf = null;

        DRPCClient client = new DRPCClient(conf,"192.168.1.140", 3772);

        Order order = packageOrder();
        String json = JSON.toString(order);
        String exclamation = client.execute("risk-check", json);

    }

    private static Order packageOrder(){
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setMerchantId(MERCHANT_ID);
        order.setAmount(new BigDecimal(RANDOM.nextInt(1000000)));
        order.setBankAccountNo(randomCard());
        order.setIdCardNo(randomIdCard());
        order.setMobile("15011223344");
        order.setAccountType("0"); //对私
        order.setBankName("北京银行");
        order.setCreateTime(new Date());
        order.setLastUpdateTime(new Date());
        order.setIp(randomIp());
        return order;
    }

    private static String randomCard(){


        char[] strs = new char[10];
        for (int i = 0; i < 10; i++) {
            strs[i] = (char) RANDOM.nextInt(10);
        }
        String s = new String(strs);
        return "622000"+s;
    }

    private static String randomIdCard(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, RANDOM.nextInt( 365 * 30) * -1);
        String date = DateFormatUtils.format(calendar.getTime(), "yyyyMMdd");
        return "140101"+date+"1201";
    }

    public static String randomIp(){
        //ip范围
        return IP_ADDR[RANDOM.nextInt(IP_ADDR.length)];
    }

}
