package nanomt88.designpattern.create.factory.example;

import java.util.Scanner;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/5 上午8:09
 * @Description:  简单工厂的示例，实例化四个操作类型的运算符的时候，使用简单工厂进行实例
 *          调用者不需要关心具体实现逻辑
 */

public class MainClass {

    public static void main(String[] args) {

        System.out.print("请输入第一个数字：");
        Scanner scanner = new Scanner(System.in);
        String number1 = scanner.nextLine();

        System.out.print("请输入运算符：");
        String operation = scanner.nextLine();

        System.out.print("请输入第二个数字：");
        String number2 = scanner.nextLine();

        Operate operate = new OperateFactory().getOperateInstance(operation);
        double result = operate.setNumber1(Double.parseDouble(number1))
                .setNumber2(Double.parseDouble(number2))
                .getResult();
        System.out.println( number1 + operation + number2 + "=" + result);
    }
}
