package nanomt88.designpattern.create.factory.example;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/5 上午8:23
 * @Description: //TODO
 */

public class MultiplyOperate extends Operate {
    public double getResult() {
        return this.getNumber1() * this.getNumber2();
    }
}
