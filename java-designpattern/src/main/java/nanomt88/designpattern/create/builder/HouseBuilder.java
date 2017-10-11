package nanomt88.designpattern.create.builder;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/10/5 下午3:52
 * @Description:  建造者接口
 */

public interface HouseBuilder {

    /**
     * 修地基
     */
    void makeFloor();

    /**
     * 修墙壁
     */
    void makeWall();

    /**
     * 修墙壁
     */
    void makeHousetop();

    House getHouse();
}
