package nanomt88.designpattern.create.factory;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/9/12 下午4:57
 * @Description: //TODO
 */

public abstract class AbstractHumanFactory {
    public abstract Human createHumanInstance(Class<Human> clazz);
}
