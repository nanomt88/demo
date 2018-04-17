package com.nanomt88.common.util.security;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Iterator;

/**
 * ${DESCRIPTION}
 *
 * @author hongxudong
 * @create 2018-04-16 7:41
 **/
public class CertificateUtil {

    @Test
    public void test()throws Exception{
        String filename = CertificateUtil.class.getResource("/").getPath() + "trustKeyStore.jks";
        System.out.println(filename);
        FileInputStream fis = new FileInputStream(filename);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate c = cf.generateCertificate(fis);
        System.out.println(c);
//        Iterator i = c.iterator();
//        while (i.hasNext()) {
//            Certificate cert = (Certificate)i.next();
//            System.out.println(cert);
//        }
    }
}
