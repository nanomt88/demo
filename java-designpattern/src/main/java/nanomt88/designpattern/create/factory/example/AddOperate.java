package nanomt88.designpattern.create.factory.example;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 上午8:19
 * @Description: //TODO
 */

public class AddOperate extends Operate {
    public double getResult() {
        return this.getNumber1() + this.getNumber2();
    }
}
