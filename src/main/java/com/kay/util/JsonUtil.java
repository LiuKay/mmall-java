package com.kay.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kay.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kay on 2018/5/16.
 * 对 Jackson 序列化的封装
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 序列化与反序列化的全局配置
     */
    static {
        //序列化时包括所有字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //取消默认转换日期为时间戳的设置
//        objectMapper.configure(JsonParser.Feature.WRITE_DATES_AS_TIMESTAMPS,false);

        //格式化时间日期
//        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT_STR));

        //忽略空bean转json的报错，默认情况下 empty bean 会报错
//        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        //忽略在json中存在属性却在bean无对应属性转换时报错
//        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String  obj2string(T obj){
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("parse obj2string warn",e);
            return null;
        }
    }

    /**
     * 格式化json字符串
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String  obj2stringPretty(T obj){
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("parse obj2string warn",e);
            return null;
        }
    }

    /**
     * 转换单个对象，无法正确处理集合
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T string2obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T)str :  objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("parse string2obj warn",e);
            return null;
        }
    }

    /**
     * 通过 TypeReference 指定要返回的集合对象
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T string2obj(String str, TypeReference<T> typeReference){
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (Exception e) {
            log.warn("parse string2obj warn",e);
            return null;
        }
    }

    /**
     * 通过传入多个class对象来进行类型转换
     * @param str
     * @param collectionClass
     * @param classes
     * @param <T>
     * @return
     */
    public static <T> T string2obj(String str, Class<?> collectionClass,Class<?>... classes){
        if (StringUtils.isEmpty(str) || collectionClass == null || classes==null) {
            return null;
        }
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, classes);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            log.warn("parse string2obj warn",e);
            return null;
        }
    }


    public static void main(String[] args) {
        User u1 = new User();
        u1.setId(1);
        u1.setUsername("kay");

        String s1 = JsonUtil.obj2string(u1);

        String s2 = JsonUtil.obj2stringPretty(u1);

        log.info(s1);

        log.info(s2);

        User user = JsonUtil.string2obj(s1, User.class);

        User u2 = new User();
        u2.setId(2);
        u2.setUsername("cxf");

        List<User> userList = new ArrayList<>();
        userList.add(u1);
        userList.add(u2);

        String users = JsonUtil.obj2stringPretty(userList);
        log.info("userList:{}", users);


        List<User> list = JsonUtil.string2obj(users, new TypeReference<List<User>>() {
        });

        List<User> list1 = JsonUtil.string2obj(null, List.class, User.class);

        log.info("=============");
    }

}
