package com.nanomt88.common.util.security;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
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

    @Test
    public void tet() throws Exception {
        CertificateFactory certificateFactory = CertificateFactory
                .getInstance("X.509");
        FileInputStream in = new FileInputStream("C:\\Users\\TCDD\\Desktop\\unionpay.cer");

        X509Certificate aCert = (X509Certificate) certificateFactory.generateCertificate(in);

        String tDN = aCert.getSubjectDN().toString();
        System.out.println(tDN);
//        String tDN = "CFCA@中国银联@N91310000736239890T@9";
        String tPart = "";
        if ((tDN != null)) {
            String tSplitStr[] = tDN.substring(tDN.indexOf("CN=")).split("@");
            if (tSplitStr != null && tSplitStr.length > 2
                    && tSplitStr[1] != null)
                tPart = tSplitStr[1];
        }
        System.out.println(tPart);
    }
}
