package com.commodity.yzrsc.utils;

import com.google.gson.Gson;

/**
 * json工具
 * Created by 328789 on 2017/4/15.
 */

public class GsonUtils {
    private static Gson gson = new Gson();
    public static <T> T jsonToObject(String json,Class<T> clazz){
        T t = gson.fromJson(json, clazz);
        return t;
    }
    public static String objectToJson(Object o){
        String s = gson.toJson(o);
        return s;
    }
}
