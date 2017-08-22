package com.code.codeGenerator.model;

/**
 * 对应数据库中表的字段
 *
 * @author song.shi
 * @since 2017-08-21
 */
public class DbColumn {

    /**
     * 数据库中的列名
     */
    private String columnName;

    /**
     * 转换成java的属性名
     */
    private String fieldName;

    /**
     * 数据库中属性类型
     */
    private String jdbcType;

    /**
     * 转换成java的属性类型
     */
    private String javaType;

    /**
     * 属性注释
     */
    private String remarks;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
