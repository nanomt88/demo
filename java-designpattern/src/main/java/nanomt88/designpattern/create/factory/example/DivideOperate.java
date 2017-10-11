package nanomt88.designpattern.create.factory.example;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 上午8:22
 * @Description: //TODO
 */

public class DivideOperate extends Operate {
    public double getResult() {
        if(this.getNumber2() == 0.0D){
            throw new RuntimeException("除数不能为0");
        }
        return this.getNumber1() / this.getNumber2();
    }
}
