package com.code.codeGenerator.codeGen.java;

import com.google.common.base.Strings;
import com.code.codeGenerator.codeGen.BaseGenerator;
import com.code.codeGenerator.model.DbColumn;
import com.code.codeGenerator.model.DbTable;
import com.code.codeGenerator.util.FileUtils;
import com.code.codeGenerator.util.PropertyUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于生成数据库表对应实体的生成器
 *
 * @author song.shi
 * @since 2017-08-21
 */
public class EntityGenerator extends BaseGenerator {
    // 模板文件
    private static final String ENTITY_TEMPLATE = "template/entity/entity.str";
    // 实体模板变量替换
    private static final String SYSTEM_PACKAGE = "system.package";
    private static final String SYSTEM_AUTHOR = "system.author";
    private static final String SYSTEM_DATE = "system.date";
    private static final String ENTITY_IMPORT = "entity.import";

    public List<String> generate(DbTable dbTable) {
        List<String> templateLines = FileUtils.readLines(ENTITY_TEMPLATE);
        Map<String, String> data = getEntityMap(dbTable);
        List<String> resultLine = new ArrayList<>();
        List<String> repeatLines = new ArrayList<>();
        boolean repeat = false;
        for (String line : templateLines) {
            if (line.contains(REPEAT_START)) {  // 重复开始
                repeat = true;
            } else if (line.contains(REPEAT_END)) {  // 重复结束, 要处理重复的内容
                // 处理重复的内容
                List<String> replacedLines = getReplacedLines(repeatLines, dbTable);
                resultLine.addAll(replacedLines);
                // 清空repeatLines
                repeatLines.clear();
                // 终止重复
                repeat = false;
            } else if (repeat) {  // 需要重复, 重复的内容累加
                repeatLines.add(line);
            } else {  // 不需要重复, 逐行处理
                resultLine.add(replaceLine(line, data));
            }
        }
        return resultLine;
    }

    @Override
    protected Map<String, String> getEntityMap(DbTable dbTable) {
        Map<String, String> data = super.getEntityMap(dbTable);
        data.put(ENTITY_IMPORT, dbTable.getImport());
        data.put(SYSTEM_PACKAGE, Strings.nullToEmpty(PropertyUtils.get(SYSTEM_PACKAGE)));
        data.put(SYSTEM_AUTHOR, Strings.nullToEmpty(PropertyUtils.get(SYSTEM_AUTHOR)));
        data.put(SYSTEM_DATE, DateTime.now().toString("yyyy-MM-dd"));
        return data;
    }

    private List<String> getReplacedLines(List<String> lines, DbTable dbTable) {
        List<String> resultList = new ArrayList<>();
        for (DbColumn dbColumn : dbTable.getColumnList()) {
            Map<String, String> data = super.getFieldMap(dbColumn);
            List<String> fieldList = replaceLines(lines, data);
            resultList.addAll(fieldList);
        }
        return resultList;
    }

}
