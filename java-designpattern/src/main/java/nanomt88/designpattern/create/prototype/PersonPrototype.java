package nanomt88.designpattern.create.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 上午11:37
 * @Description: //TODO
 */

public class PersonPrototype implements Cloneable{

    private String name ;

    private int age;

    private String sex;

    private List<PersonPrototype> friends;

    public PersonPrototype() {
    }

    public PersonPrototype(String name, int age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public PersonPrototype(String name, int age, String sex, List<PersonPrototype> friends) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.friends = friends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<PersonPrototype> getFriends() {
        return friends;
    }

    public void setFriends(List<PersonPrototype> friends) {
        this.friends = friends;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        PersonPrototype clone = (PersonPrototype) super.clone();
        //深拷贝： 拷贝引用对象
        List<PersonPrototype> list = this.friends == null ? null : new ArrayList<PersonPrototype>(friends.size());
        for (int i = 0; friends != null && i < friends.size(); i++) {
            list.add((PersonPrototype) this.friends.get(i).clone());
        }
        clone.setFriends(list);
        return clone;
    }

    @Override
    public String toString() {
        return "PersonPrototype{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", friends=" + friends +
                '}';
    }
}
