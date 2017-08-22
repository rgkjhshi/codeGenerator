package com.code.codeGenerator.codeGen;

import com.code.codeGenerator.codeGen.xml.MapperGenerator;
import com.code.codeGenerator.model.DbTable;
import com.code.codeGenerator.util.DBConnection;
import org.junit.Test;

import java.util.List;

/**
 * @author song.shi
 * @since 2017-08-22
 */
public class MapperGeneratorTest {
    @Test
    public void generate() throws Exception {
        List<DbTable> tableList = DBConnection.getTableList();
        DbTable table = tableList.get(0);
        MapperGenerator mapperGenerator = new MapperGenerator();
        List<String> lines = mapperGenerator.generate(table);
        for (String line : lines) {
            System.out.println(line);
        }
    }

}