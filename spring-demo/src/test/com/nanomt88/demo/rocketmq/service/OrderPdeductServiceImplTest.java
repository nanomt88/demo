package com.nanomt88.demo.rocketmq.service;

import com.nanomt88.demo.boot.Main;
import com.nanomt88.demo.rocketmq.entity.OrderPdeduct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(classes = Main.class)  // 指定spring-boot的启动类
public class OrderPdeductServiceImplTest {

    @Autowired
    OrderPdeductService orderPdeductService;

    @Test
    public void getOne() throws Exception {
        OrderPdeduct o = orderPdeductService.getOne(193023L);
        System.out.println("===================>"+o.getId() +" : "+o.getMerchantId());
    }

}