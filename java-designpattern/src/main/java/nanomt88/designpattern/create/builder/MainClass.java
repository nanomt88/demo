package nanomt88.designpattern.create.builder;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 下午4:04
 * @Description: //TODO
 */

public class MainClass {
    public static void main(String[] args) {
        //不同的房子，使用不同的builder
//        HouseBuilder builder = new VillaBuilder();
        HouseBuilder builder = new BothyBuilder();
        //建筑师负责实现具体建设逻辑
        HouseDesigner designer = new HouseDesigner();
        House house = designer.makeHouse(builder);

        System.out.println(house);
    }
}
