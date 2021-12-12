package nanomt88.designpattern.proxy;

/**
 * ${DESCRIPTION}
 *
 * @author nanomt88@gmail.com
 * @create 2018-03-10 20:27
 **/
public class House {

    private boolean isBigWindows;

    private boolean isBigBed;

    private Integer price;

    private Integer area;

    public House(){}

    public House(boolean isBigWindows, boolean isBigBed, Integer price, Integer area) {
        this.isBigWindows = isBigWindows;
        this.isBigBed = isBigBed;
        this.price = price;
        this.area = area;
    }

    public boolean isBigWindows() {
        return isBigWindows;
    }

    public void setBigWindows(boolean bigWindows) {
        isBigWindows = bigWindows;
    }

    public boolean isBigBed() {
        return isBigBed;
    }

    public void setBigBed(boolean bigBed) {
        isBigBed = bigBed;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "房子信息如下， 有大床：" + isBigBed + "，有大窗户：" + isBigWindows + "，面积："+ area + " 租金：" + price + "\n";
    }
}
