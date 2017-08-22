package com.code.codeGenerator.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 从资源路径的 config.properties 文件中读取配置
 *
 * @author song.shi
 * @since 2017-07-21
 */
public class PropertyUtils {

    private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

    /**
     * 存放属性配置的map
     */
    private static Map<String, String> propertyMap;

    /**
     * 装载配置文件，转为map
     */
    private static void loadProperties() {
        if (propertyMap != null) {
            return;
        }
        propertyMap = Maps.newHashMap();
        Properties properties = new Properties();
        InputStream is = PropertyUtils.class.getClassLoader().getResourceAsStream("config.properties");
        Preconditions.checkNotNull(is, "初始化装载配置文件失败：config.properties文件未找到");
        try {
            properties.load(is); // 读文件
            is.close();
        } catch (IOException e) {
            throw new RuntimeException("初始化装载配置文件失败: 配置文件读取过程中出现IOE");
        }
        for (String key : properties.stringPropertyNames()) {
            propertyMap.put(key, properties.getProperty(key));
        }
    }

    /**
     * 读取配置
     */
    public static String get(String key) {
        loadProperties();
        return propertyMap.get(key);
    }

}
