package com.code.codeGenerator.util;

import com.code.codeGenerator.model.DbColumn;
import com.code.codeGenerator.model.DbTable;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 数据库连接类
 *
 * @author song.shi
 * @since 2017-07-21
 */
public class DBConnection {
    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

    private static Connection connection = null;

    public static List<DbTable> getTableList() {
        String dbName = PropertyUtils.get("jdbc.dbName");
        String table = PropertyUtils.get("jdbc.tableName");
        if (Strings.isNullOrEmpty(table)) {
            table = "%";
        }
        List<DbTable> tableList = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        try {
            // 获取数据库元数据
            DatabaseMetaData metaData = connection.getMetaData();
            // 获取所有表的结果集
            ResultSet tableSet = metaData.getTables(dbName, "", table, new String[]{"TABLE"});
            while (tableSet.next()) {
                String tableName = tableSet.getString("TABLE_NAME");
                // 生成表
                DbTable dbTable = new DbTable();
                // 设置表名
                dbTable.setTableName(tableName);
                // 设置表对应的实体名
                dbTable.setEntityName(underlineToCamel(tableName));
                // 设置实体的类名
                dbTable.setClassName(upperFirst(dbTable.getEntityName()));
                // 表注释
                dbTable.setRemarks(tableSet.getString("REMARKS"));
                // 遍历每一列
                ResultSet columnSet = metaData.getColumns(dbName, "", tableName, "%");
                while (columnSet.next()) {
                    DbColumn dbColumn = new DbColumn();
                    // 表中列名
                    dbColumn.setColumnName(columnSet.getString("COLUMN_NAME"));
                    // 对应实体的属性名
                    dbColumn.setFieldName(underlineToCamel(dbColumn.getColumnName()));
                    String jdbcType = columnSet.getString("TYPE_NAME");
                    jdbcType = Splitter.on(" ").omitEmptyStrings().splitToList(jdbcType).get(0);
                    // jdbcType
                    dbColumn.setJdbcType(jdbcType);
                    // javaType
                    dbColumn.setJavaType(Strings.nullToEmpty(PropertyUtils.get("jdbcType." + dbColumn.getJdbcType())));
                    // 字段注释
                    dbColumn.setRemarks(Strings.nullToEmpty(columnSet.getString("REMARKS")));
                    dbTable.addColumn(dbColumn);
                }
                tableList.add(dbTable);
            }
        } catch (SQLException e) {
            logger.error("读取数据库信息失败", e);
            return tableList;
        }
        DBConnection.closeConnection();
        return tableList;
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            Class.forName(PropertyUtils.get("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            logger.error("加载数据库驱动失败", e);
        }
        Properties properties = new Properties();
        String url = PropertyUtils.get("jdbc.url");
        String user = PropertyUtils.get("jdbc.username");
        String password = PropertyUtils.get("jdbc.password");
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        // 设置可以获取remarks信息
        properties.setProperty("remarks", "true");
        // 设置可以获取tables remarks信息(这个很重要)
        properties.setProperty("useInformationSchema", "true");
        properties.setProperty("useUnicode", "true");
        properties.setProperty("characterEncoding", "UTF-8");
        try {
            connection = DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            logger.error("数据库连接获取失败", e);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("关闭数据库连接失败", e);
            } finally {
                connection = null;
            }
        }
    }

    /**
     * 首字母大写
     */
    private static String upperFirst(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return "";
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }

    /**
     * 下划线式变量转换为驼峰式变量
     */
    private static String underlineToCamel(String str) {
        if (str == null || "".equals(str.trim())) {
            return "";
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(str.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
