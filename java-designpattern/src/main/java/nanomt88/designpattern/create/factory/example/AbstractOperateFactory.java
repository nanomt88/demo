package nanomt88.designpattern.create.factory.example;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 上午8:27
 * @Description:   运算符工厂类
 */

public interface AbstractOperateFactory {
    /**
     * 获取运算操作类实例
     * @param operation
     * @return
     */
    Operate getOperateInstance(String operation);
}
