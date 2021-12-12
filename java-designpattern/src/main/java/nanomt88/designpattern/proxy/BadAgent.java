package nanomt88.designpattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *  黑中介
 *
 * @author nanomt88@gmail.com
 * @create 2018-03-10 20:42
 **/
public class BadAgent implements InvocationHandler{

    private Renter target;

    public BadAgent(){}

    public Object getInstance(Renter target){
        this.target = target;
        Class clazz = target.getClass();
        System.out.println("黑中介代理的对象为： " + target.getName() + " " + target.getClass());
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("------ 我是黑中介 ------");
        System.out.println("proxy : "+proxy.getClass());
        System.out.println("推荐备选房子 ： " + args[0].toString());
        boolean result = (Boolean) method.invoke(target, args);
        if(result){
            System.out.println("推荐房子成功……");
            System.out.println("------ 我是黑中介 ------");
            return result;
        }
        System.out.println("------ 我是黑中介 ------");
        return false;
    }

}
