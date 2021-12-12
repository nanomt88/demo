package nanomt88.designpattern.create.builder;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/10/5 下午3:44
 * @Description: //TODO
 */

public class House {

    // 地板
    private String floor;
    // 墙
    private String wall;
    // 屋顶
    private String housetop;

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getWall() {
        return wall;
    }

    public void setWall(String wall) {
        this.wall = wall;
    }

    public String getHousetop() {
        return housetop;
    }

    public void setHousetop(String housetop) {
        this.housetop = housetop;
    }

    @Override
    public String toString() {
        return "House{" +
                "floor='" + floor + '\'' +
                ", wall='" + wall + '\'' +
                ", housetop='" + housetop + '\'' +
                '}';
    }
}
