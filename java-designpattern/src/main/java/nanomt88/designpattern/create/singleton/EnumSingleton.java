package nanomt88.designpattern.create.singleton;

import java.io.Serializable;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 上午10:23
 * @Description:
 *          枚举类型的单例类，最优方式
 *          优点： 轻松实现序列化和反序列化
 *
 */

public enum  EnumSingleton{

    INSTANCE ;

    public void somethingToDo(){
        //to do logic
        this.getDeclaringClass();
    }
}
