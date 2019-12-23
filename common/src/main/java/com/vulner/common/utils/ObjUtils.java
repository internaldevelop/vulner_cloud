package com.vulner.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class ObjUtils {

    /**
     * 获取一个List集合的第一项
     *
     * @param objList List 集合
     * @return List 集合的第一项，如果集合为空，则返回 null
     */
    static public <T> T getFirstValid(List<T> objList) {
        if (objList == null)
            return null;

        if (objList.size() == 0)
            return null;
        else
            return objList.get(0);
    }

    public static String getValueFromJsonString(String jsonString, String key) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            return jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

//    public static <T> List<T> deepCopy(List<T> src, Class<?> clazz) {
//        String json = JSON.toJSONString(src);
//        return JSON.parseObject(json, (Type) clazz);
//    }

    public static <T> List<T> deepCopyList(List<T> src, Class<T> clazz) {
        String json = JSON.toJSONString(src);
        return JSON.parseArray(json, clazz);
    }

    public static void addSwaggerRegistry(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
