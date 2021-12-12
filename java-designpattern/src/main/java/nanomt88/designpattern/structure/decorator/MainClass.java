package nanomt88.designpattern.structure.decorator;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/6 上午9:40
 * @Description:
 *
 *  装饰模式的优点
 *　  （1）装饰模式与继承关系的目的都是要扩展对象的功能，但是装饰模式可以提供比继承更多的灵活性。
 *         装饰模式允许系统动态决定“贴上”一个需要的“装饰”，或者除掉一个不需要的“装饰”。继承关系则不同，
 *         继承关系是静态的，它在系统运行前就决定了。
 *　  （2）通过使用不同的具体装饰类以及这些装饰类的排列组合，设计师可以创造出很多不同行为的组合。
 *     装饰模式的缺点由于使用装饰模式，可以比使用继承关系需要较少数目的类。使用较少的类，当然使设计比较易
 *     于进行。但是，在另一方面，使用装饰模式会产生比使用继承关系更多的对象。更多的对象会使得查错变得困难，特别是这些对象看上去都很相像。
 *
 *   装饰模式的缺点
 *　      由于使用装饰模式，可以比使用继承关系需要较少数目的类。使用较少的类，当然使设计比较易于进行。
 *      但是，在另一方面，使用装饰模式会产生比使用继承关系更多的对象。更多的对象会使得查错变得困难，
 *      特别是这些对象看上去都很相像。
 */

public class MainClass {
    public static void main(String[] args) {
        JavaCoder coder = new JavaCoder();
        coder.coding();

        System.out.println("--------------");
        PHPAndJavaCoderDecorator php = new PHPAndJavaCoderDecorator(coder);
        php.coding();

        System.out.println("--------------");
        AllStackCoderDecorator allStack = new AllStackCoderDecorator(php);
        allStack.coding();

    }
}
