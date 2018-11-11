package com.wwa.widgetweatheralarm.util;

public class Tag {
    public static String get(Class c) {
        return "wwa.W" + c.getSimpleName();
    }
}
