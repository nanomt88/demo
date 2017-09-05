package com.nanomt88.risk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

/**
 *  注意，不要重写loadClass方法。  因为在使用自定义的MyClassLoader加载Person类的时候 。  Person类中
 *  需要依赖的其他对象， 都会默认使用MyClassLoader的loadClass方法进行加载。  如果重写了loadClass方法
 *  （像下面代码注释那样），就会导致jvm使用MyClassLoader来加载Object、String等等一些类。  当然，这些
 *  类在classpath是找不到的。 所以就会抛出ClassNotFoundException 。
 *   （<font color="red">如果重写loadClass方法，一定要判断未加载该类的时候，调用getSystemClassLoader 让父类加载</font>）
 *
 *
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
        /**
         * defineClass：
         *      该方法是ClassLoader中非常重要的一个方法，它接收以字节数组表示的类字节码，并把它转换成Class实例，
         *      该方法转换一个类的同时，会先要求装载该类的父类以及实现的接口类。
         */
        return defineClass(name, raw, 0,  length);
    }

    /**
     * loadClass：加载类的入口方法，调用该方法完成类的显式加载。通过对该方法的重新实现，我们可以完全控制和管理类的加载过程。
     * @param name
     * @param resolve
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class cls = null ;
        /**
         * findLoadedClass：
         *      每个类加载器都维护有自己的一份已加载类名字空间，其中不能出现两个同名的类。
         *      凡是通过该类加载器加载的类，无论是直接的还是间接的，都保存在自己的名字空间中，该方法就是在该名字
         *      空间中寻找指定的类是否已存在，如果存在就返回给类的引用，否则就返回null。这里的直接是指，存在于
         *      该类加载器的加载路径上并由该加载器完成加载，间接是指，由该类加载器把类的加载工作委托给其他类
         *      加载器完成类的实际加载
         */
        cls = findLoadedClass(name);
        if(!this.dynaClass.contains(name) && cls == null){
            /**
             * getSystemClassLoader ：
             *      Java2中新增的方法。该方法返回系统使用的ClassLoader。
             *      可以在自己定制的类加载器中通过该方法把一部分工作转交给系统类加载器去处理。
             */
            cls = getSystemClassLoader().loadClass(name);
        }
        if(cls == null){
            throw new ClassNotFoundException(name);
        }
        //用于连接class
        if(resolve){
            /**
             *  resolveClass：
             *      链接一个指定的类。这是一个在某些情况下确保类可用的必要方法，
             *      详见Java语言规范中“执行”一章对该方法的描述。
             */
            resolveClass(cls);
        }
        return cls;
    }
}
