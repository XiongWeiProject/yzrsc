package com.commodity.yzrsc.utils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by liyushen on 2017/5/26 22:16
 * 获取想要结果的工具类
 */
public class ResultUtil {
    //获取数组的第一个数据
    public static Object getFirstDataFromArray(JSONArray jsonArray){
        if (jsonArray==null||jsonArray.length()==0){
            return "";
        }else {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    return jsonArray.get(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return "";
        }
    }
}
