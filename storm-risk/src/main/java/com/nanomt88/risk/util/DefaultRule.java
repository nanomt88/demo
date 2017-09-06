package com.nanomt88.risk.util;

/**
 * @author nanomt88@gmail.com
 * @create 2017-09-04 7:37
 **/
public class DefaultRule extends RuleMap{
    @Override
    public boolean parseRule(String name) {
        System.out.println("this is default rule engine");
        return true;
    }
}
