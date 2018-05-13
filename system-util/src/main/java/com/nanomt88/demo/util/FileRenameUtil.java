package com.nanomt88.demo.util;

import org.junit.Test;

import java.io.File;

public class FileRenameUtil {

    @Test
    public void batchRename(){
        String[] toName = {
                "从认知到行动，管理你的健康               ",
                "纠正职场健康管理的谣言和误区             ",
                "如何有效解决困、倦、疲、乏？             ",
                "上班族如何避免驼背、耸肩？               ",
                "学会正确的站姿和坐姿                     ",
                "长期伏案，也能颈椎不痛！                 ",
                "加班党如何调整饮食和睡眠？               ",
                "延缓衰老的正确姿势                       ",
                "菜鸟从零开始学健身                       ",
                "如何避免过劳肥？                         ",
                "压力大，性欲不振怎么办？                 ",
                "不节食，如何变瘦变美？                   ",
                "如何变成行走的荷尔蒙？                   ",
                "种让自己变健康漂亮的方法                 ",
                "如何同时拥有蜜桃臀和大长腿？(上)         ",
                "如何同时拥有蜜桃臀和大长腿？(下)         ",
                "博士教练的私人健身计划                   ",
        };
        String path = "E:\\学习\\健身\\毕义明 - 2018的健身必修课";
        File dir = new File(path);
        File[] files = dir.listFiles();
        for(File f : files){
            String name = f.getName();
            String[] ns = name.split("\\.");

            String newName = f.getParentFile().getAbsolutePath() + File.separator ;
            int index = -1;
            for(int i=0; i<toName.length; i++){
                if(( ns[0] ).indexOf(toName[i].trim()) != -1 ){
                    index = i;
                    break;
                }
            }
            newName = newName +  String.format("%02d", index+1)  +"_"+ name.substring(3,name.length());
            System.out.println(name +  " --> " + newName);
            f.renameTo(new File(newName));
//
//            if(ns[0].indexOf("00") == -1 ){
//                System.out.print(name);
//                int index = Integer.parseInt(ns[0]);
//                String newName = f.getParentFile().getAbsolutePath() + File.separator + toName[index-1].trim() + "." +ns[1];
////                f.renameTo(new File(f.getParentFile().getAbsolutePath()))
//                System.out.println(name + " --> " + newName);
//                f.renameTo(new File(newName));
//            }
        }
    }

    @Test
    public void rename2(){
        String[] toName = {
                "从认知到行动，管理你的健康               ",
                "纠正职场健康管理的谣言和误区             ",
                "如何有效解决困、倦、疲、乏？             ",
                "上班族如何避免驼背、耸肩？               ",
                "学会正确的站姿和坐姿                     ",
                "长期伏案，也能颈椎不痛！                 ",
                "加班党如何调整饮食和睡眠？               ",
                "延缓衰老的正确姿势                       ",
                "菜鸟从零开始学健身                       ",
                "如何避免过劳肥？                         ",
                "压力大，性欲不振怎么办？                 ",
                "不节食，如何变瘦变美？                   ",
                "如何变成行走的荷尔蒙？                   ",
                "种让自己变健康漂亮的方法                 ",
                "如何同时拥有蜜桃臀和大长腿？(上)         ",
                "如何同时拥有蜜桃臀和大长腿？(下)         ",
                "博士教练的私人健身计划                   ",
        };
    }
}
