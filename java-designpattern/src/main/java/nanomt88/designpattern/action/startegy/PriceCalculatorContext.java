package nanomt88.designpattern.action.startegy;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/8 下午3:16
 * @Description:  概述：定义一组算法，将每个算法封装起来，并且使它们之间可以相互转换
 */

public class PriceCalculatorContext{

    private DiscountStrategy strategy;

    public PriceCalculatorContext(DiscountStrategy strategy){
        this.strategy = strategy;
    }

    /**
     * 计算价格
     * @param price
     * @return
     */
    public double calculator(double price){
        return strategy.discount(price);
    }
}
