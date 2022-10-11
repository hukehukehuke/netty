package com.example.filelistner.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author huke
 * @date 2022/10/10/上午11:43
 */
@Slf4j
public class YmlUtil {


    private static Map<String, String> result = new HashMap<>();

    /**
     * 根据文件名获取yml的文件内容
     *
     * @param fileName fileName
     * @return
     */
    public static Map<String, String> getYmlByFileName(String fileName) {

        if (Strings.isEmpty(fileName)) {
            return Map.of();
        }
        try {
            InputStream in = YmlUtil.class.getClassLoader().getResourceAsStream(fileName);
            Yaml props = new Yaml();
            Object obj = props.loadAs(in, Map.class);
            Map<String, Object> param = (Map<String, Object>) obj;
            if (Objects.isNull(param)) {
                return Map.of();
            }
            param.forEach((key, val) -> {
                if (val instanceof Map) {
                    forEachYaml(key, (Map<String, Object>) val);
                } else {
                    result.put(key, val.toString());
                }
            });
        } catch (Exception e) {
            log.error("读取yml文件信息时，属性地址错误 e ={}", e);
        }

        return result;
    }

    /**
     * 遍历yml文件，获取map集合
     *
     * @param keyStr
     * @param obj
     * @return
     */
    public static Map<String, String> forEachYaml(String keyStr, Map<String, Object> obj) {
        if(Objects.isNull(obj)){
            return Map.of();
        }
        obj.forEach((key,val) ->{
            String strNew = "";
            if (Strings.isEmpty(keyStr)) {
                strNew = keyStr + "." + key;
            } else {
                strNew = key;
            }
            if (val instanceof Map) {
                forEachYaml(strNew, (Map<String, Object>) val);
            } else {
                result.put(strNew, val.toString());
            }
        });
        return result;
    }

    /**
     * 根据key获取值
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        Map<String, String> map = getYmlByFileName(null);
        if (map == null) {
            return null;
        }
        return map.get(key);
    }
}
