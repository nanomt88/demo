package nanomt88.designpattern.create.singleton;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/5 上午10:07
 * @Description:    懒汉式 ： 双重检查
 */

public class DoubleCheckSingleton {

    private static DoubleCheckSingleton  INSTANCE = null;

    private DoubleCheckSingleton(){}

    /**
     * 双重检查
     * @return
     */
    private static DoubleCheckSingleton getInstance(){
        if(INSTANCE == null){
            synchronized (DoubleCheckSingleton.class){
                if(INSTANCE == null){
                    INSTANCE = new DoubleCheckSingleton();
                }
            }
        }
        return INSTANCE;
    }
}
