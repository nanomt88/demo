package nanomt88.designpattern.structure.decorator;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/6 上午9:29
 * @Description: //TODO
 */

public abstract class CoderDecorator implements Coder {

    private Coder coder = null;

    public CoderDecorator(Coder code){
        this.coder = code;
    }

    public Coder getCoder() {
        return coder;
    }

    public void setCoder(Coder coder) {
        this.coder = coder;
    }

    @Override
    public abstract void coding();
}
