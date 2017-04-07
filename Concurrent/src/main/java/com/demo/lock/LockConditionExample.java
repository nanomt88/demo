package com.demo.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/7 上午1:06
 * @Description: //TODO
 */

public class LockConditionExample {

    private Lock locks = new ReentrantLock();
    private Condition empty = locks.newCondition();
    private Condition notEmpty = locks.newCondition();

    private volatile List<String>  list = new ArrayList<>();

    public void put(String str){
        //先判断大小
        list.size();
        //插入
    }

    public String take(){

        return null;
    }


    public static void main(String[] args) {

    }


}
