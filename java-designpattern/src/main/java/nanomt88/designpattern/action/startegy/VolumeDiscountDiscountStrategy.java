package nanomt88.designpattern.action.startegy;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/8 下午3:12
 * @Description: //TODO
 */

public class VolumeDiscountDiscountStrategy implements DiscountStrategy {
    /**
     * 计算折扣： 满200减50
     *
     * @param price
     */
    @Override
    public double discount(double price) {
        if(price > 200){
            return price - 50;
        }
        return price;
    }
}
