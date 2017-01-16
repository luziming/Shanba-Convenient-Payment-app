package com.shaba.app.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class GsonTools {
    //泛型设置成null，解析的时候可以接受任何类型的数据
    public static <T> T getProdjectList(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * ʹ��Gson����List<T>
     *
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> List<T> getProdjects(String jsonString, Class<T[]> type) {
        List<T> list = new ArrayList<T>();
        Gson gson = new Gson();
        T[] ary = gson.fromJson(jsonString, type);
        return Arrays.asList(ary);
    }

    public static List<String> getList(String jsonString) {
        List<String> list = new ArrayList<String>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Map<String, Object>> listKeyMaps(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString,
                    new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
