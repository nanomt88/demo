package nanomt88.designpattern.action.startegy;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/8 下午3:07
 * @Description: //TODO
 */

public interface DiscountStrategy {

    /**
     * 计算折扣
     * @param price
     */
    double discount(double price);
}
