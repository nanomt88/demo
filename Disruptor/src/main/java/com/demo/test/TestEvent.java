package com.demo.test;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/10 上午7:36
 * @Description: //TODO
 */

public class TestEvent implements Serializable{

    private static final long serialVersionUID = 190477375600102293L;

    private String id;
    private String name;
    private double price;
    private AtomicInteger count = new AtomicInteger(0);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TestEvent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}
