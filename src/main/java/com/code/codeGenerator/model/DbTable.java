package com.code.codeGenerator.model;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 对应数据库中的表
 *
 * @author song.shi
 * @since 2017-08-21
 */
public class DbTable {
    /**
     * 数据库中表名
     */
    private String tableName;

    /**
     * 表名对应的实体名(首字母小写)
     */
    private String entityName;

    /**
     * 表名对应的类名(首字母大写)
     */
    private String className;

    /**
     * 表注释
     */
    private String remarks;

    /**
     * 属性列表
     */
    private List<DbColumn> columnList = new ArrayList<>();

    /**
     * 这个表中字段名的最大长度
     */
    private int maxColumnLength = 0;

    private Set<String> importSet = new HashSet<>();

    public void addColumn(DbColumn column) {
        columnList.add(column);
        // 得到最长字段名的长度
        if (column.getColumnName().length() > maxColumnLength) {
            maxColumnLength = column.getColumnName().length();
        }
        if ("BigDecimal".equals(column.getJavaType())) {
            importSet.add("import java.math.BigDecimal;");
        }
        if ("Date".equals(column.getJavaType())) {
            importSet.add("import java.util.Date;");
        }
    }

    public String getImport() {
        return Joiner.on(System.lineSeparator()).join(importSet);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getMaxColumnLength() {
        return maxColumnLength;
    }

    public List<DbColumn> getColumnList() {
        return columnList;
    }
}
