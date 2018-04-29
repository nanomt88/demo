package com.nanomt88.common.util.security;

import org.junit.jupiter.api.Test;

/**
 * ${DESCRIPTION}
 *
 * @author hongxudong
 * @create 2018-04-25 14:59
 **/
public class MD5UtilsTest {
    @Test
    public void md5() {
        System.out.println(MD5Utils.md5("111"));
    }

    @Test
    public void sha256() {
        System.out.println(SHAUtils.sha256("111"));
    }

}