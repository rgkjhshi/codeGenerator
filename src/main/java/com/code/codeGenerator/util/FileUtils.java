package com.code.codeGenerator.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * 文件读写
 *
 * @author song.shi
 * @since 2017-08-21
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 读取资源目录下的文件内容
     *
     * @param fileName 资源目录下的文件名
     * @return 文件内容
     */
    public static List<String> readLines(String fileName) {
        List<String> lines = null;
        try {
            File file = new File(Resources.getResource(fileName).getFile()); // 获取文件
            lines = Files.readLines(file, Charsets.UTF_8); // 读文件内容，拆分成行
        } catch (IOException e) {
            String message = "读取文件" + fileName + "失败";
            logger.error(message, fileName, e);
            throw new IllegalArgumentException(message);
        }
        return lines;
    }

    /**
     * 将内容写入文件
     */
    public static void writeLines(List<String> lines, String fileName) {
        File file = new File(fileName);
        try {
            Files.createParentDirs(file);
            org.apache.commons.io.FileUtils.writeLines(file, lines);
        } catch (IOException e) {
            logger.error("写入文件{}失败", fileName);
        }
    }

}
