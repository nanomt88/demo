package com.demo.multi;

import java.io.Serializable;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/4/10 上午7:36
 * @Description: //TODO
 */

public class Order implements Serializable{

    private static final long serialVersionUID = 190477375600102293L;

    private String id;
    private String name;
    private double price;

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

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
