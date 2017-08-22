package com.code.codeGenerator.codeGen;

import com.code.codeGenerator.model.DbTable;
import com.code.codeGenerator.util.DBConnection;
import org.junit.Test;

import java.util.List;

/**
 * @author song.shi
 * @since 2017-08-22
 */
public class GeneratorTest {

    @Test
    public void generateEntity() throws Exception {
        List<DbTable> tableList = DBConnection.getTableList();
        DbTable table = tableList.get(0);
        Generator generator = new Generator();
        List<String> lines = generator.generate(table, "template/entity/entity.str");
        for (String line : lines) {
            System.out.println(line);
        }
    }

    @Test
    public void generateXml() throws Exception {
        List<DbTable> tableList = DBConnection.getTableList();
        DbTable table = tableList.get(0);
        Generator generator = new Generator();
        List<String> lines = generator.generate(table, "template/mapper/mapper.str");
        for (String line : lines) {
            System.out.println(line);
        }
    }

    @Test
    public void generateDao() throws Exception {
        List<DbTable> tableList = DBConnection.getTableList();
        DbTable table = tableList.get(0);
        Generator generator = new Generator();
        List<String> lines = generator.generate(table, "template/dao/dao.str");
        for (String line : lines) {
            System.out.println(line);
        }
    }

}