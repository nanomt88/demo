package nanomt88.designpattern.structure.adaptor;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/7 上午10:22
 * @Description:
 *
 *      适配器可以使用继承或者委让模式
 */

public class PowerAdaptor {

    private V220Power power;

    public PowerAdaptor(V220Power power){
        this.power = power;
    }

    public void v200Power(){
        power.power();
    }

    public void v5Power(){
        System.out.println("5V 电压");
    }
}
