package nanomt88.designpattern.structure.adaptor;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/7 上午10:16
 * @Description: //TODO
 */

public class Mobile {

    public void charge(PowerAdaptor power){
        power.v5Power();
        System.out.println("手机充电中...");
    }
}
