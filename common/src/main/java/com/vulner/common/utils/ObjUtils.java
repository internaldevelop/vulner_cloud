package com.vulner.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

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

    public static <T> boolean nullOrEmptyList(List<T> objList) {
        if (objList == null || objList.size() == 0) {
            return true;
        }

        return false;
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

    /**
     * @Title: combineObjectProps
     * @Description: 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，
     *               那么 sourceObj 中的值会覆盖 targetObj 的值
     *
     *               Spring 中有 BeanUtils.copyProperties(Object source, Object target)
     * @author: Jason
     * @date: 2019/12/25
     * @param sourceObj
     *            被提取属性的对象
     * @param targetObj
     *            将要被合并或被覆盖属性的对象
     * @return targetObj 合并后的对象
     * @return: Object
     */
    public static Object combineObjectProps(Object targetObj, Object sourceObj) {
        Class sourceClass = sourceObj.getClass();
        Class targetClass = targetObj.getClass();

        Field[] sourceFields = sourceClass.getDeclaredFields();
        Field[] targetFields = sourceClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if (sourceField.get(sourceObj) != null) {
                    targetField.set(targetObj, sourceField.get(sourceObj));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetObj;
    }

    /**
     * @Title: getValuePropertyNames
     * @Description: 该方法获取所有有值的方法
     * @author: Jason
     * @date: 2019/12/25
     * @param source
     *            被提取属性的对象
     * @return 属性字符串数组
     * @return: String[]
     */
    public static String[] getValuePropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (null != srcValue) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
