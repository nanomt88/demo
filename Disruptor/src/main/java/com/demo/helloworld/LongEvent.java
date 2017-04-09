package com.demo.helloworld;

import java.io.Serializable;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/8 下午11:34
 * @Description: //TODO
 */

public class LongEvent implements Serializable{

    private static final long serialVersionUID = 2622972498658172218L;

    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
