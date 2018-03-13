package nanomt88.designpattern.proxy;

/**
 *  北漂屌丝
 *
 * @author hongxudong
 * @create 2018-03-10 20:15
 **/
public class BeijingLoser implements Renter{

    /**  姓名   */
    private String name;
    /**  预期房子   */
    private House expect;

    public BeijingLoser(){}

    public BeijingLoser(String name, House house){
        this.name = name;
        this.expect = house;
    }

    @Override
    public boolean findHouse(House house){
        System.out.print("我的名字是：" + name + ", 预期 " + expect.toString());
        System.out.println("要求1 ： 大床是否满足 " + (!expect.isBigBed() ||  (expect.isBigBed() && house.isBigBed())));
        System.out.println("要求2 ： 大窗户是否满足 " + (!expect.isBigWindows() || (expect.isBigWindows()&&house.isBigWindows())));
        System.out.println("要求3 ： 面积是否满足  " + ( expect.getArea() <= house.getArea()));
        System.out.println("要求4 ： 价格是否满足  " + ( expect.getPrice() >= house.getPrice()));

        if((!expect.isBigBed() ||  (expect.isBigBed() && house.isBigBed()))
                &&  (!expect.isBigWindows() || (expect.isBigWindows()&&house.isBigWindows()))
                &&  ( expect.getArea() <= house.getArea()) && ( expect.getPrice() >= house.getPrice())) {
            System.out.println("所有条件都满足，就租这了！");
            return true;
        }
        if( expect.getPrice() > house.getPrice()){
            System.out.println("穷啊，租不起啊，呜呜呜呜呜……");
        }
        System.out.println("有条件不满足，不住不住~~~");
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    public House getExpect() {
        return expect;
    }

}
