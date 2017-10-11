package nanomt88.designpattern.structure.decorator;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 下午7:05
 * @Description: //TODO
 */

public class PHPAndJavaCoderDecorator extends CoderDecorator{

    public PHPAndJavaCoderDecorator(Coder code) {
        super(code);
    }

    @Override
    public void coding() {
        super.getCoder().coding();
        writePHP();
    }

    public void writePHP(){
        System.out.println("write PHP program");
    }
}
