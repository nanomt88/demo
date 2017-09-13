package com.nanomt88.risk;

/**
 * @author nanomt88@gmail.com
 * @create 2017-09-13 8:10
 **/
public enum  RiskStatus {
    PASS,
    WARNING,
    REJECTED;

    public static String MISSING_RISK_RULE = "未找到风控规则";
}
