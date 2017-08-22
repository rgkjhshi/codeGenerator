package com.code.codeGenerator.codeGen;

import com.google.common.base.Strings;
import com.code.codeGenerator.model.DbColumn;
import com.code.codeGenerator.model.DbTable;

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
public class BaseGenerator {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{(.+?)\\}");
    protected static final String REPEAT_START = "${repeat start}";
    protected static final String REPEAT_END = "${repeat end}";

    // 实体模板变量替换
    private static final String entity_tableName = "entity.tableName";
    private static final String entity_entityName = "entity.entityName";
    private static final String entity_className = "entity.className";
    private static final String entity_remarks = "entity.remarks";
    // 属性模板变量替换
    private static final String entity_field_columName = "entity.field.columnName";
    private static final String entity_field_fieldName = "entity.field.fieldName";
    private static final String entity_field_jdbcType = "entity.field.jdbcType";
    private static final String entity_field_javaType = "entity.field.javaType";
    private static final String entity_field_remarks = "entity.field.remarks";

    protected Map<String, String> getEntityMap(DbTable dbTable) {
        Map<String, String> data = new HashMap<>();
        data.put(entity_tableName, dbTable.getTableName());
        data.put(entity_entityName, dbTable.getEntityName());
        data.put(entity_className, dbTable.getClassName());
        data.put(entity_remarks, dbTable.getRemarks());
        return data;
    }

    protected Map<String, String> getFieldMap(DbColumn dbColumn) {
        Map<String, String> data = new HashMap<>();
        data.put(entity_field_columName, dbColumn.getColumnName());
        data.put(entity_field_fieldName, dbColumn.getFieldName());
        data.put(entity_field_jdbcType, dbColumn.getJdbcType());
        data.put(entity_field_javaType, dbColumn.getJavaType());
        data.put(entity_field_remarks, dbColumn.getRemarks());
        return data;
    }

    /**
     * 模板变量替换
     */
    protected String replaceLine(String line, Map<String, String> data) {
        Matcher matcher = PATTERN.matcher(line);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group(1);  // 键名
            String value = Strings.nullToEmpty(data.get(name));  // 键值
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 模板变量替换
     */
    protected List<String> replaceLines(List<String> lines, Map<String, String> data) {
        List<String> resultList = new ArrayList<>();
        for (String line : lines) {
            resultList.add(replaceLine(line, data));
        }
        return resultList;
    }
}
