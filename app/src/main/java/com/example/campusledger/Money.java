package com.example.campusledger;

import java.math.BigDecimal;

/** 金额以“分”为单位保存，避免用 double 计算时产生小数误差。 */
public class Money {
    public static long parseCents(String text) {
        // 只接受正数，最多六位整数、两位小数，例如 18、18.5、18.50。
        if (!text.matches("[0-9]{1,6}(\\.[0-9]{1,2})?")) {
            throw new IllegalArgumentException("请输入正确金额，最多两位小数");
        }
        long cents = new BigDecimal(text).movePointRight(2).longValueExact();
        if (cents <= 0) throw new IllegalArgumentException("金额必须大于 0");
        return cents;
    }

    public static String format(long cents) {
        return BigDecimal.valueOf(cents, 2).toPlainString();
    }
}
