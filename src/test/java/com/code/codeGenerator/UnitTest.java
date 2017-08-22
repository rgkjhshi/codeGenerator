package com.code.codeGenerator;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author song.shi
 * @since 2017-08-22
 */
public class UnitTest {
    private static final Logger logger = LoggerFactory.getLogger(UnitTest.class);

    @Test
    public void testRegex() {
        String str = "name is ${name}, and age is ${age}.";
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        List<String> resultList = new ArrayList<>();

        Map<String, String> data = Maps.newHashMap();
        data.put("name", "Tom");
        data.put("age", "18");

        Matcher matcher = pattern.matcher(str);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group(1); // 键名
            String value = Strings.nullToEmpty(data.get(name));// 键值
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        logger.info(sb.toString());
    }

}