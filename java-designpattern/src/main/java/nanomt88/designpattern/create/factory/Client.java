package nanomt88.designpattern.create.factory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/9/12 下午4:13
 * @Description: //TODO
 */

public class Client {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add(null);
        System.out.println(list.size());
        System.out.println(list.get(0));
    }
}
