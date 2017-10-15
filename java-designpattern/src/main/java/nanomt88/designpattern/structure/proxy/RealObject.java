package nanomt88.designpattern.structure.proxy;

/**
 * @author nanomt88@gmail.com
 * @create 2017-10-14 17:10
 **/
public class RealObject extends AbstractObject {
    @Override
    public void operation() {
        System.out.println("real object invoke... ");
    }
}
