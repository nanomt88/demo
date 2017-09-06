package com.nanomt88.risk.util;

/**
 * @author nanomt88@gmail.com
 * @create 2017-09-06 7:07
 **/
public class App {

    private volatile RuleMap ruleMap;

    public RuleMap getRuleMap() {
        return ruleMap;
    }

    public void setRuleMap(RuleMap ruleMap) {
        this.ruleMap = ruleMap;
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.setRuleMap(new RuleMap());
        RuleMap old = null;
        RuleMap custom = null;

        for (int i = 0; i < 8; i++) {
            if(i == 3){
                old = app.getRuleMap();
                System.out.println("load custom class");
                CustomClassLoader classLoader =
                        new CustomClassLoader("D:\\workspace\\github\\demo\\storm-risk\\target\\classes",
                                new String[]{"com.nanomt88.risk.util.DefaultRule"});
                Class<RuleMap> clazz = classLoader.loadClass("com.nanomt88.risk.util.DefaultRule",true);
                RuleMap o = clazz.newInstance();
                app.setRuleMap(o);
            }


            if (i == 6){
                custom = app.getRuleMap();

                System.out.println("load default class");
                ClassLoader systemClassLoad = ClassLoader.getSystemClassLoader();
                Class<?> clazz = systemClassLoad.loadClass("com.nanomt88.risk.util.RuleMap");
                RuleMap o = (RuleMap) clazz.newInstance();
                app.setRuleMap(o);
            }

            app.getRuleMap().parseRule(" loop " + i);

            Thread.currentThread().sleep(500);
        }
        System.out.println("old:" + old + "  custom:" + custom);
        System.out.println(old.equals(custom));
        System.out.println(old.equals(app.getRuleMap()));
    }
}
