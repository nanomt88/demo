package com.nanomt88.demo;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/5/6 上午9:34
 * @Description: //TODO
 *
 *  -XX:PermSize=10M -XX:MaxPermSize=10M
 */

public class MethodAreaOOM {

    static class OOMObject{}

    public static void main(String[] args) {

        while (true){
            Enhancer eh = new Enhancer();
            eh.setSuperclass(OOMObject.class);
            eh.setUseCache(false);
            eh.setCallback(new MethodInterceptor(){

                @Override
                public Object intercept(Object o, Method method, Object[] objects,
                                        MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invokeSuper(o, objects);
                }
            });

            eh.create();
        }
    }
}
