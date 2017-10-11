package nanomt88.designpattern.create.singleton;

import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 上午10:01
 * @Description:  静态内部类 ，
 *                  优点： 只加载一次，而且是懒加载
 *                  缺点： 不能进行序列化和反序列化。
 *                      如果实现Serializable接口，每次反序列化的时候都会新创建一个对象
 */

public class  StaticInnerClassSingleton implements Serializable{


    private StaticInnerClassSingleton(){}

    public static StaticInnerClassSingleton getInstance(){
        return InnerClass.INSTANCE;
    }

    private static class InnerClass{
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }
}
