package com.commodity.yzrsc.ui.widget.citypickerview.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import org.apache.http.util.EncodingUtils;

import static javax.xml.transform.OutputKeys.ENCODING;

/**
 * 读取Assets目录下面的json文件
 * Created by liji on 2016/5/6.
 */
public class JAssetsUtils {

    /**
     * 读取Assets目录下面指定文件并返回String数据
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getJsonDataFromAssets(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
//        InputStream inputStream = context.getClass().getClassLoader().getResourceAsStream("assets/" + fileName);
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String json = new String(buffer, "utf-8");
            stringBuilder = stringBuilder.append(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    //从assets 文件夹中获取文件并读取数据
    public String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
//获取文件的字节数
            int lenght = in.available();
//创建byte数组
            byte[] buffer = new byte[lenght];
//将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
