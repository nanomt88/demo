package nanomt88.designpattern.create.builder;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 下午4:02
 * @Description: //TODO
 */

public class BothyBuilder implements HouseBuilder {

    private House house = new House();
    /**
     * 修地基
     */
    public void makeFloor() {
        house.setFloor("茅屋 --> 地面");
    }

    /**
     * 修墙壁
     */
    public void makeWall() {
        house.setWall("茅屋 --> 墙壁");
    }

    /**
     * 修墙壁
     */
    public void makeHousetop() {
        house.setHousetop("茅屋 --> 屋顶");
    }

    public House getHouse() {
        return house;
    }
}
