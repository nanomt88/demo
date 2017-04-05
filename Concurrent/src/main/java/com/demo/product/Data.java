package com.demo.product;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 下午3:08
 * @Description: //TODO
 */

public class Data {

    private int id;
    private String name ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
