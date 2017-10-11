package nanomt88.designpattern.create.factory;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/9/12 下午5:01
 * @Description: //TODO
 */

public class HumanFactory extends AbstractHumanFactory {
    public Human createHumanInstance(Class<Human> clazz) {
        if(clazz == null){
            return null;
        }
        try {
            Class<Human> aClass = (Class<Human>) Class.forName(clazz.getName());
            Human human = aClass.newInstance();
            return human;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
