package nanomt88.designpattern.structure.proxy;

/**
 * @author nanomt88@gmail.com
 * @create 2017-10-15 9:32
 **/
public class ProxyObject extends AbstractObject {

    private RealObject realObject = new RealObject();

    @Override
    public void operation() {
        System.out.println("before...");
        realObject.operation();
        System.out.println("after...");
    }
}
