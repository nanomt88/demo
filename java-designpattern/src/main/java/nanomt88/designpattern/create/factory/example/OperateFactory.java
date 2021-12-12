package nanomt88.designpattern.create.factory.example;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/5 上午8:10
 * @Description:  简单工厂
 */

public class OperateFactory implements AbstractOperateFactory {
    /**
     * 获取运算操作类实例
     * @param operation
     * @return
     */
    public Operate getOperateInstance(String operation){
        if("+".equals(operation)){
            return new AddOperate();
        }else if("-".equals(operation)){
            return new SubtractOperate();
        }else if("*".equals(operation)){
            return new MultiplyOperate();
        }else if("/".equals(operation)){
            return new DivideOperate();
        }
        return null;
    }
}
