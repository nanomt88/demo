package nanomt88.designpattern.structure.decorator;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/5 下午7:51
 * @Description: //TODO
 */

public class AllStackCoderDecorator extends CoderDecorator{

    public AllStackCoderDecorator(Coder code) {
        super(code);
    }

    @Override
    public void coding() {
        super.getCoder().coding();
        allStack();
    }

    public void allStack(){
        System.out.println("write C++ program");
        System.out.println("write Python program");
        System.out.println("write Ruby program");
    }
}
