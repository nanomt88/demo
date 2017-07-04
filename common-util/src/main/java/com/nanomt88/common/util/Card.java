package com.nanomt88.common.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author nanomt88@gmail.com
 * @create 2017-07-03 21:49
 **/
public class Card {

    private static final int HEX_RADIX = 16;

    private static BigInteger[] MAIN_ACCOUNT = null;

    public static synchronized void init(String[] array){
        List<BigInteger> list = new ArrayList<BigInteger>();
        for(String cardNO : array){
            BigInteger no = hexToBigInteger(cardNO);
            if(no != null){
                list.add(no);
            }
        }

        MAIN_ACCOUNT = list.toArray(new BigInteger[list.size()]);

        //按照大小重新排序
        Arrays.sort(MAIN_ACCOUNT);
    }

    public static String searchCardBin(String cardNo){
        BigInteger integer = hexToBigInteger(cardNo);
        if(integer == null){
            return null;
        }
        //卡号在最大最小区间内
        if(integer.compareTo(MAIN_ACCOUNT[0]) == -1 || integer.compareTo(MAIN_ACCOUNT[MAIN_ACCOUNT.length-1]) == 1 ){
            return null;
        }

        return null;
    }

    /**
     * 二分查找最相近的两个数，其中有一个就是最匹配的卡bin
     * @return
     */
    private static int[] binarySearch(BigInteger cardNo){
        int low = 0;
        int high = MAIN_ACCOUNT.length-1;
        while (low <= high){
            int middle = (low + high)/2;
            if( cardNo.compareTo(MAIN_ACCOUNT[middle]) == 1){   //当前数大于中间数，低位数增大
                low = middle ;
            }else if(cardNo.compareTo(MAIN_ACCOUNT[middle]) == -1){  //当前数小于中间数，低位数增大
                high = middle ;
            }   //卡号不存在等于卡bin的情况，所以不需要判断
            if( high - low == 1){       //如果低位和高位刚好相差1位，则cardNo落在这两个数字中间
                return new int[]{low, high};
            }else if( high == low){     //如果低位和高位相等
                int small = middle > high ? low : middle;
                int big = middle > high ? middle : high;
                return new int[]{small, big};
            }
        }

        return null;
    }

    /**
     *  16进制字符串 转BigInteger
     * @param hex
     * @return
     */
    private static BigInteger hexToBigInteger(String hex){
        if(hex == null){
            return null;
        }
        BigInteger integer = null;
        try{
            integer = new BigInteger(hex, HEX_RADIX);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return integer;
    }

    /**
     *  BigInteger转16进制字符串
     * @param integer
     * @return
     */
    private static String toHexString(BigInteger integer){
        if(integer == null)
            return null;
        return integer.toString(HEX_RADIX).toUpperCase();
    }

    public static void main(String[] args) {

        init(Test.cards);
        long time = System.currentTimeMillis();
        int[] ints = binarySearch(hexToBigInteger("6236869999999999999"));
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - time));
        System.out.println(toHexString(MAIN_ACCOUNT[ints[0]]) + "," + toHexString(MAIN_ACCOUNT[ints[1]]));
    }
}
