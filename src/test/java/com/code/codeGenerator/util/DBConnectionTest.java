package com.code.codeGenerator.util;

import com.google.common.base.Strings;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * @author song.shi
 * @since 2017-08-21
 */
public class DBConnectionTest {

    @Test
    public void getConnection() throws Exception {
        String dbName = "pay_venice";
        String defaultTableName = "payment_asset";
        Connection connection = DBConnection.getConnection();
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet tableSet = dbMetaData.getTables(null, dbName, "%", new String[]{"TABLE"});
        while (tableSet.next()) {
            String tableName = tableSet.getString("TABLE_NAME");
            String tableRemark = tableSet.getString("REMARKS");
            System.out.println(tableName);
            System.out.println(tableRemark);
            if (defaultTableName.equals(tableName)) {
                ResultSet columnSet = dbMetaData.getColumns(null, dbName, tableName, "%");
                while (columnSet.next()) {
                    String columnName = columnSet.getString("COLUMN_NAME");
                    String jdbcType = columnSet.getString("TYPE_NAME");
                    String remark = Strings.nullToEmpty(columnSet.getString("REMARKS"));
                    System.out.println(columnName + " " + jdbcType + " " + remark);
                }
            }
        }
        DBConnection.closeConnection();
    }

}