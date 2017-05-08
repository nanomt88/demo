package com.nanomt88.demo;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/5/6 下午9:55
 * @Description:
 *              测试栈调用深度
 */

public class StackOOM {

    private static int count;

    /**
     * 递归调用，测试栈调用深度
     */
    public static void recursion() {
        count++;
        recursion();
    }

    public static void main(String[] args) {

        //-Xss1m
        //-Xss4m
        /**
         *  1M ：栈调用最大深入：20969
         *      1M / 20969 = 50B
         *  2M ：栈调用最大深入：48924
         *      2M / 48924 = 43B
         *  4M ：栈调用最大深入：100138
         *      4M / 100138 = 42B
         */

        try {
            recursion();
        }catch (Throwable e){
            System.out.println("栈调用最大深入：" + count);
            e.printStackTrace();
        }

    }
}
