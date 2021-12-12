package nanomt88.designpattern.create.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/5 下午12:08
 * @Description: //TODO
 */

public class MainClass {
    public static void main(String[] args) throws CloneNotSupportedException {
        PersonPrototype person = new PersonPrototype("张三", 33 , "男");
        List<PersonPrototype> friends = new ArrayList<PersonPrototype>();
        friends.add(new PersonPrototype("王小狗", 19, "男"));
        friends.add(new PersonPrototype("王小猫", 22, "男"));
        friends.add(new PersonPrototype("韩梅梅", 18, "女"));
        person.setFriends(friends);

        //使用原形模式进行复制
        PersonPrototype clone = (PersonPrototype) person.clone();
        System.out.println(person);
        System.out.println(clone);

        System.out.println("-----------------");
        clone.getFriends().add(new PersonPrototype("李雷", 99, "男"));
        System.out.println(person);
        System.out.println(clone);
    }
}
