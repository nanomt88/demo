package nanomt88.designpattern.action.startegy;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/8 上午10:52
 * @Description: //TODO
 */

public class MainClass {
    public static void main(String[] args) {

        double price = 210D;

        PriceCalculatorContext context = new PriceCalculatorContext(new VolumeDiscountDiscountStrategy());
        double amount = context.calculator(price);
        System.out.println("折扣前金额： "+ price +" ，折扣后金额"+ amount);

        context = new PriceCalculatorContext(new PercentageDiscountDiscountStrategy());
        amount = context.calculator(price);
        System.out.println("折扣前金额： "+ price +" ，折扣后金额"+ amount);

    }
}
