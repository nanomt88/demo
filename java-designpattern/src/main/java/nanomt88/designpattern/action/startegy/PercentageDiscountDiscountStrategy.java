package nanomt88.designpattern.action.startegy;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/8 下午3:11
 * @Description:  按照百分比折扣
 */

public class PercentageDiscountDiscountStrategy implements DiscountStrategy {
    /**
     * 计算折扣
     *
     * @param price
     */
    @Override
    public double discount(double price) {
        //打八折
        return price * 0.8D ;
    }
}
