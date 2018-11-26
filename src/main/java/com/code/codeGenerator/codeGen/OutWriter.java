package com.code.codeGenerator.codeGen;

import com.code.codeGenerator.model.DbTable;
import com.code.codeGenerator.util.DBConnection;
import com.code.codeGenerator.util.FileUtils;
import com.code.codeGenerator.util.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author song.shi
 * @since 2017-08-22
 */
public class OutWriter {
    private static final Logger logger = LoggerFactory.getLogger(OutWriter.class);

    private static final String ENTITY_TEMPLATE_FILE = "template/dao/entity/entity.str";
    private static final String DAO_TEMPLATE_FILE = "template/dao/mapper/mapper.str";
    private static final String MAPPER_TEMPLATE_FILE = "template/mapper/xml.str";

    public static void writeToFile() {
        List<DbTable> tableList = DBConnection.getTableList();
        String outputPath = PropertyUtils.get("output.path");
        writeEntity(tableList, outputPath);
        writeDao(tableList, outputPath);
        writeXml(tableList, outputPath);
    }

    private static void writeEntity(List<DbTable> tableList, String outputPath) {
        Generator generator = new Generator();
        for (DbTable dbTable : tableList) {
            String fileName = outputPath + "/dao/entity/" + dbTable.getClassName() + ".java";
            List<String> lines = generator.generate(dbTable, ENTITY_TEMPLATE_FILE);
            FileUtils.writeLines(lines, fileName);
            logger.info("已生成{}.java", dbTable.getClassName());
        }
    }

    private static void writeDao(List<DbTable> tableList, String outputPath) {
        Generator generator = new Generator();
        for (DbTable dbTable : tableList) {
            String fileName = outputPath + "/dao/mapper/" + dbTable.getClassName() + "Mapper.java";
            List<String> lines = generator.generate(dbTable, DAO_TEMPLATE_FILE);
            FileUtils.writeLines(lines, fileName);
            logger.info("已生成{}Mapper.java", dbTable.getClassName());
        }
    }

    private static void writeXml(List<DbTable> tableList, String outputPath) {
        Generator generator = new Generator();
        for (DbTable dbTable : tableList) {
            String fileName = outputPath + "/mapper/" + dbTable.getClassName() + ".mapper";
            List<String> lines = generator.generate(dbTable, MAPPER_TEMPLATE_FILE);
            FileUtils.writeLines(lines, fileName);
            logger.info("已生成{}.mapper", dbTable.getClassName());
        }
    }
}
