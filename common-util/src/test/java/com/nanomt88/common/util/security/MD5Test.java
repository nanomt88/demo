package com.nanomt88.common.util.security;


import org.junit.Test;

/**
 * ${DESCRIPTION}
 *
 * @author hongxudong
 * @create 2018-04-14 22:10
 **/
public class MD5Test {

    @Test
    public void sign() {
        System.out.println("------------");
    }

    @Test
    public void test2(){
        byte max = Byte.MAX_VALUE;
        byte min = Byte.MIN_VALUE;
        for(byte i=min; i<=max; i++) {
            System.out.println(i + " --> " + Integer.toHexString(0xFF & i));
            if(i == max){
                break;
            }
        }
    }
}