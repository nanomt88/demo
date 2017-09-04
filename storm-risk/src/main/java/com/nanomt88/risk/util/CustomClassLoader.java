package com.nanomt88.risk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

/**
 * @author nanomt88@gmail.com
 * @create 2017-09-04 7:25
 **/
public class CustomClassLoader extends ClassLoader{

    /**
     * 加载class的URL
     */
    private String url;
    /**
     * 需要加载的class 类名
     */
    private HashSet<String> dynaClass;

    public CustomClassLoader(String url, String[] classNames) throws Exception {
        //指定父类加载器为null
        super(null);
        this.url = url;
        dynaClass = new HashSet<>();
        loadClassByURL(classNames);
    }

    private void loadClassByURL(String[] classNames) throws Exception {
        for(String name : classNames){
            loadDirectly(name);
            dynaClass.add(name);
        }
    }

    private Class loadDirectly(String name) throws Exception {
        Class cls = null;
        StringBuffer sb = new StringBuffer(url);
        String classname = name.replace('.', File.separatorChar) + ".class";
        sb.append(File.separator + classname);
        File classF = new File(sb.toString());
        cls = instantiateClass(name,new FileInputStream(classF),
                (int) classF.length());
        return cls;
    }

    private Class instantiateClass(String name, FileInputStream fileInputStream, int length) throws IOException {
        byte[] raw = new byte[ length];
        fileInputStream.read(raw);
        fileInputStream.close();
        return defineClass(name, raw, 0,  length);
    }

    protected Class loadClass(String name, boolean resolve){

        return null;
    }
}
