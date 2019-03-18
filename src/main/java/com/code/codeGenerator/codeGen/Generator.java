package com.code.codeGenerator.codeGen;

import com.code.codeGenerator.model.DbColumn;
import com.code.codeGenerator.model.DbTable;
import com.code.codeGenerator.util.FileUtils;
import com.code.codeGenerator.util.PropertyUtils;
import com.google.common.base.Strings;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author song.shi
 * @since 2017-08-22
 */
public class Generator {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{(.+?)\\}");
    private static final String REPEAT_START = "${repeat start}";
    private static final String REPEAT_END = "${repeat end}";

    // 系统设置
    private static final String system_package = "system.package";
    private static final String system_author = "system.author";
    private static final String system_date = "system.date";
    // 实体模板变量替换
    private static final String entity_tableName = "entity.tableName";
    private static final String entity_entityName = "entity.entityName";
    private static final String entity_className = "entity.className";
    private static final String entity_remarks = "entity.remarks";
    private static final String entity_import = "entity.import";
    // 属性模板变量替换
    private static final String entity_field_columName = "entity.field.columnName";
    private static final String entity_field_fieldName = "entity.field.fieldName";
    private static final String entity_field_jdbcType = "entity.field.jdbcType";
    private static final String entity_field_javaType = "entity.field.javaType";
    private static final String entity_field_remarks = "entity.field.remarks";
    private static final String entity_field_columName_padding = "entity.field.columnName.padding";

    public List<String> generate(DbTable dbTable, String template) {
        List<String> templateLines = FileUtils.readLines(template);
        Map<String, String> data = getEntityMap(dbTable);
        List<String> resultLine = new ArrayList<>();
        List<String> repeatLines = new ArrayList<>();
        boolean repeat = false;
        for (String line : templateLines) {
            // 重复开始
            if (line.contains(REPEAT_START)) {
                repeat = true;
            } else if (line.contains(REPEAT_END)) {
                // 重复结束, 要处理重复的内容
                List<String> replacedLines = getReplacedLines(repeatLines, dbTable);
                resultLine.addAll(replacedLines);
                // 清空repeatLines
                repeatLines.clear();
                // 终止重复
                repeat = false;
            } else if (repeat) {
                // 需要重复, 重复的内容累加
                repeatLines.add(line);
            } else {
                // 不需要重复, 逐行处理
                resultLine.add(replaceLine(line, data));
            }
        }
        return resultLine;
    }

    private Map<String, String> getEntityMap(DbTable dbTable) {
        Map<String, String> data = new HashMap<>();
        data.put(entity_tableName, dbTable.getTableName());
        data.put(entity_entityName, dbTable.getEntityName());
        data.put(entity_className, dbTable.getClassName());
        data.put(entity_remarks, dbTable.getRemarks());
        data.put(entity_import, dbTable.getImport());
        data.put(system_package, Strings.nullToEmpty(PropertyUtils.get(system_package)));
        data.put(system_author, Strings.nullToEmpty(PropertyUtils.get(system_author)));
        data.put(system_date, DateTime.now().toString("yyyy-MM-dd"));
        return data;
    }

    private Map<String, String> getFieldMap(DbTable dbTable, DbColumn dbColumn) {
        Map<String, String> data = new HashMap<>();
        data.put(entity_field_columName, dbColumn.getColumnName());
        data.put(entity_field_fieldName, dbColumn.getFieldName());
        data.put(entity_field_jdbcType, dbColumn.getJdbcType());
        data.put(entity_field_javaType, dbColumn.getJavaType());
        data.put(entity_field_remarks, dbColumn.getRemarks());
        data.put(entity_field_columName_padding, Strings.padEnd(dbColumn.getColumnName(), dbTable.getMaxColumnLength(), ' '));
        return data;
    }

    private List<String> getReplacedLines(List<String> lines, DbTable dbTable) {
        // 这里针对xml中base_select的最后一行做个特殊处理
        if (lines.size() > 0) {
            int index = lines.size() - 1;
            String lastLine = lines.get(index);
            if (lastLine.contains("${entity.field.fieldName},")) {
                String newLastLine = lastLine.replace("${entity.field.fieldName},", "${entity.field.fieldName}");
                lines.set(index, newLastLine);
            }
        }
        // 字段替换
        List<String> resultList = new ArrayList<>();
        for (DbColumn dbColumn : dbTable.getColumnList()) {
            Map<String, String> data = getFieldMap(dbTable, dbColumn);
            List<String> fieldList = replaceLines(lines, data);
            resultList.addAll(fieldList);
        }
        return resultList;
    }

    /**
     * 模板变量替换
     */
    private String replaceLine(String line, Map<String, String> data) {
        Matcher matcher = PATTERN.matcher(line);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            // 键名
            String name = matcher.group(1);
            // 键值
            String value = Strings.nullToEmpty(data.get(name));
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 模板变量替换
     */
    private List<String> replaceLines(List<String> lines, Map<String, String> data) {
        List<String> resultList = new ArrayList<>();
        for (String line : lines) {
            resultList.add(replaceLine(line, data));
        }
        return resultList;
    }
}
