package nanomt88.designpattern.create.builder;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/5 下午4:04
 * @Description: //TODO
 */

public class HouseDesigner{

    public House makeHouse(HouseBuilder builder){
        builder.makeFloor();
        builder.makeWall();
        builder.makeHousetop();
        return builder.getHouse();
    }
}
