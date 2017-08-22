package com.code.codeGenerator.codeGen.xml;

import com.code.codeGenerator.codeGen.BaseGenerator;
import com.code.codeGenerator.model.DbTable;
import com.google.common.base.Strings;
import com.code.codeGenerator.model.DbColumn;
import com.code.codeGenerator.util.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author song.shi
 * @since 2017-08-22
 */
public class MapperGenerator extends BaseGenerator {
    // 模板文件
    private static final String ENTITY_TEMPLATE = "template/mapper/mapper.str";
    private static final String entity_field_columName_padding = "entity.field.columnName.padding";

    public List<String> generate(DbTable dbTable) {
        List<String> templateLines = FileUtils.readLines(ENTITY_TEMPLATE);
        Map<String, String> data = super.getEntityMap(dbTable);
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


    private List<String> getReplacedLines(List<String> lines, DbTable dbTable) {
        List<String> resultList = new ArrayList<>();
        for (DbColumn dbColumn : dbTable.getColumnList()) {
            Map<String, String> data = super.getFieldMap(dbColumn);
            data.put(entity_field_columName_padding, Strings.padEnd(dbColumn.getColumnName(), dbTable.getMaxColumnLength(), ' '));
            List<String> fieldList = replaceLines(lines, data);
            resultList.addAll(fieldList);
        }
        return resultList;
    }

}
