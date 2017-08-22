package com.code.codeGenerator.codeGen;

import com.code.codeGenerator.codeGen.java.EntityGenerator;
import com.code.codeGenerator.model.DbTable;
import com.code.codeGenerator.util.DBConnection;
import org.junit.Test;

import java.util.List;

/**
 * @author song.shi
 * @since 2017-08-22
 */
public class EntityGeneratorTest {

    @Test
    public void generate() throws Exception {
        List<DbTable> tableList = DBConnection.getTableList();
        DbTable table = tableList.get(0);
        EntityGenerator entityGenerator = new EntityGenerator();
        List<String> lines = entityGenerator.generate(table);
        for (String line : lines) {
            System.out.println(line);
        }
    }

}