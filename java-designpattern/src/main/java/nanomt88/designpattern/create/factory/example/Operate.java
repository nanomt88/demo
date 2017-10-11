package nanomt88.designpattern.create.factory.example;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 上午8:14
 * @Description: //TODO
 */

public abstract class Operate {

    private double number1 ;

    private double number2 ;

    public Operate(){}

    public Operate(double number1, double number2) {
        this.number1 = number1;
        this.number2 = number2;
    }

    public double getNumber1() {
        return number1;
    }

    public Operate setNumber1(double number1) {
        this.number1 = number1;
        return this;
    }

    public double getNumber2() {
        return number2;
    }

    public Operate setNumber2(double number2) {
        this.number2 = number2;
        return this;
    }

    public abstract double getResult();
}
