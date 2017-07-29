package com.nanomt88.demo.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 *  java序列化测试
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-27 7:42
 **/
public class JavaSerializableTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long start =  System.currentTimeMillis();
        setSerializableObject();
        System.out.println("java Serializable writeObject time:" + (System.currentTimeMillis() - start) + " ms" );
        start =  System.currentTimeMillis();
        getSerializableObject();
        System.out.println("java Serializable readObject time:" + (System.currentTimeMillis() - start) + " ms");
    }

    public static void setSerializableObject() throws IOException {

        FileOutputStream fo = new FileOutputStream("D:/file2.bin");

        ObjectOutputStream so = new ObjectOutputStream(fo);

        for (int i = 0; i < 100000; i++) {
            HashMap<String, Integer> map = new HashMap<String, Integer>(2);
            map.put("zhang0", i);
            map.put("zhang1", i);
            so.writeObject(new Simple("zhang"+i, (i+1), map));
        }
        so.flush();
        so.close();
    }

    public static void getSerializableObject(){
        FileInputStream fi;
        try {
            fi = new FileInputStream("D:/file2.bin");
            ObjectInputStream si = new ObjectInputStream(fi);

            Simple simple =null;
            while((simple=(Simple)si.readObject()) != null){
                //System.out.println(simple.getAge() + "  " + simple.getName());
            }
            fi.close();
            si.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
