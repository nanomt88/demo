package netty.pojo;

import java.util.Date;

/**
 * Created by ZBOOK-17 on 2017/3/31.
 */
public class UnixTime {

    private final long val ;

    public UnixTime() {
        val = System.currentTimeMillis();
    }

    public UnixTime(long time){
        this.val = time;
    }

    public long getTime(){
        return val;
    }

    @Override
    public String toString() {
        //转换成标准的格林威治时间
        return new Date((getTime() - 2208988800L) * 1000L).toString();
    }
}
