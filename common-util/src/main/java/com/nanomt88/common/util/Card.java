package com.nanomt88.common.util;

import java.math.BigInteger;
import java.util.*;

/**
 * @author nanomt88@gmail.com
 * @create 2017-07-03 21:49
 **/
public class Card {

    private static final int HEX_RADIX = 16;

    private static BigInteger[] MAIN_ACCOUNT = null;

    private static Set<String> SET = new HashSet<>(5000*3);

    public static synchronized void init(String[] array){
        List<BigInteger> list = new ArrayList<BigInteger>();
        for(String cardNO : array){
            BigInteger no = hexToBigInteger(cardNO);
            if(no != null){
                list.add(no);
            }
            //
            SET.add(cardNO.replaceAll("F",""));
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
        if(integer.compareTo(MAIN_ACCOUNT[MAIN_ACCOUNT.length-1]) == 1 ){
            return null;
        }
        if(integer.compareTo(MAIN_ACCOUNT[0]) == -1){
            String card = toHexString(MAIN_ACCOUNT[0]);
            if(cardNo.startsWith(card.replace("F",""))){
                return card;
            }else {
                return null;
            }
        }
        int[] indexs = binarySearch(integer);
        return verify(cardNo, indexs[0]);
    }

    private static String verify(String cardNo, int indexs){

        for(int j=indexs; j<MAIN_ACCOUNT.length ; j++) {
            String origin = toHexString(MAIN_ACCOUNT[j]);

            if(origin.length() != cardNo.length()){
                return null;
            }
            for (int i = 0; i < origin.length(); i++) {
                if (origin.charAt(i) == 'F') {
                    if(j-indexs > 0 )System.out.println("     match in : " + (j-indexs));
                    return origin;
                }
                if (origin.charAt(i) == cardNo.charAt(i)) {
                    continue;
                } else {
                    break;
                }
            }
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
        for ( int j=0 ; low <= high; j++){
            int middle = (low + high)/2;
            if( cardNo.compareTo(MAIN_ACCOUNT[middle]) == 1){   //当前数大于中间数，低位数增大
                low = middle ;
            }else if(cardNo.compareTo(MAIN_ACCOUNT[middle]) == -1){  //当前数小于中间数，低位数增大
                high = middle ;
            }   //卡号不存在等于卡bin的情况，所以不需要判断
            if( high - low == 1){       //如果低位和高位刚好相差1位，则cardNo落在这两个数字中间
//                return new int[]{low, high};
                System.out.println("循环次数：" + j);
                return new int[]{high};
            }else if( high == low){     //如果低位和高位相等
                int small = middle > high ? low : middle;
                int big = middle > high ? middle : high;
//                return new int[]{small, big};
                System.out.println("循环次数：" + j);
                return new int[]{big};
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

    /**
     * 构造真实卡号
     * @param random
     * @param card
     * @return
     */
    public static String cloneCard(Random random, String card){
        char[] chars = new char[card.length()];
        for (int i = 0; i < card.length(); i++) {
            chars[i] = card.charAt(i);
            if(chars[i] == 'F'){
                chars[i] =  (random.nextInt(9)+"").charAt(0);
            }
        }
        //构造真实卡号
        String realCard = new String(chars, 0, chars.length);
        return realCard;
    }

    public static String setSearch(String card){
        for(int i=10; i>1; i--){
            String cardBin = card.substring(0,i);
            if(SET.contains(cardBin)){
                return cardBin;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        BigInteger i1 = hexToBigInteger("9FFFFFFFFFFFFFFF");
        System.out.println(i1);
        BigInteger i2 = hexToBigInteger("1FFFFFFFFFF");
        System.out.println(i2);
        System.out.println(i1.subtract(i2));
//        System.out.println("123456789012".substring(0,10));
//        testAll();
//        test1();

    }

    private static void testAll(){
        init(Test.cards);

        Random r = new Random();

        long count1 = 0;
        long count2 = 0;
        for(String str : Test.cards){

            String cardNo = cloneCard(r, str);

            long start = System.nanoTime();
            String cardBin = searchCardBin(cardNo);
            long end = System.nanoTime();
            long time1= end - start; count1 = time1+count1;

            long start2 = System.nanoTime();
            String cardBin2 = setSearch(cardNo);
            long end2 = System.nanoTime();
            long time2 = end2 - start2;count2 = time2+count2;

            System.out.println("CardNo: "+ cardNo  +"   耗时二分："+ time1
                    +"   耗时Map："+ time2 + "  是否相等：" + cardBin.replaceAll("F","").equals(cardBin2));

        }
        System.out.println("总耗时对比， 二分："+ count1 + "  Map：" + count2);
        System.out.println("平均耗时对比， 二分："+ count1/Test.cards.length + "  Map：" + count2/Test.cards.length);
    }

    private static void test1(){
        init(Test.cards);

        Random r = new Random();

        String cardNo = "4041748162286777";  //CardNo: 6011267858055075 期望：60112FFFFFFFFFFF  实际：601126FFFFFFFFFF
//        String cardNo = cloneCard(r, "60112FFFFFFFFFFF");  //CardNo: 6011267858055075 期望：60112FFFFFFFFFFF  实际：601126FFFFFFFFFF

        long time1 = System.nanoTime();
        String cardBin = searchCardBin(cardNo);
        long time2 = System.nanoTime();
        System.out.println( "cardNo: " + cardNo + "  耗时: " + (time2 - time1));
    }
}
