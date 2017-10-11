package nanomt88.designpattern.create.builder;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 下午3:52
 * @Description:  别墅
 */

public class VillaBuilder implements HouseBuilder{

    private House house = new House();
    /**
     * 修地基
     */
    public void makeFloor() {
        house.setFloor("别墅 --> 地基");
    }

    /**
     * 修墙壁
     */
    public void makeWall() {
        house.setWall("别墅 --> 墙壁");
    }

    /**
     * 修墙壁
     */
    public void makeHousetop() {
        house.setHousetop("别墅 --> 屋顶");
    }

    public House getHouse() {
        return house;
    }
}
