package nanomt88.designpattern.structure.adaptor;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/7 上午10:29
 * @Description: //TODO
 */

public class MainClass {
    public static void main(String[] args) {
        Mobile mobile = new Mobile();
        mobile.charge(new PowerAdaptor(new V220Power()));
    }
}
