------------------------------------------------------------------------------------------------------
使用方式：

1.  配置config.properties
    选择数据源、生成路径、要生成的模块

2.  如只需生成默认风格（fpp风格）的代码，则直接执行Test类main方法即可
    如需生成自定义风格的代码，则修改/resources/template下的各种模板，再运行test即可
------------------------------------------------------------------------------------------------------
备注：模板中的替换表达式

格式简单如 ${表达式key}

表达式包括两种
1.  值表达式：表达式将直接被替换为其替换值
    比如：${sys.author} ${javaType}

2.  引用表达式：当模板中需要引入其他模板，需要用到引用表达式，其格式为：${SUB_TEMPLATE.子模板名 = 引入方式}
    比如：
        ${SUB_TEMPLATE.sub_getterAndSetter = REPEAT_BY_FIELD} 引入get和set方法子模板，循环规则为 按属性循环
        ${SUB_TEMPLATE.bottom = NO_REPEAT} 引入bottom子模板，循环规则为 不循环，直接铺过来
    引入方式只支持两种：
        REPEAT_BY_FIELD 按属性循环：数据库每有一列，就会循环依次
        NO_REPEAT 不循环：相当于插入一段新的文本到父模板中

值表达式API：
有一些系统已经提供好，可以直接在模板中用：

    sys.dateNow   当前时间，格式为yyyy-MM-dd HH:mm:ss
    modelName    模型名称，为表名经首字母大写、驼峰转换后的字符串
    modelMemo    模型注释，为表注释
    modelImports 基本import行，包含模型中需要import的属性的import行，以换行符分割，比如 import java.util.Date;

    javaName        列对应的java名
    javaType        列对应的java类型
    javaMemo        列注释
    getterName      列对应的get方法名
    setterName      列对应的set方法名
    dbname          数据库列名
    dbType          数据库列类型
    tableName      表名
    tableShortName 表简写

有其他需求，可以在config.properties中随意配置
配置方式：以replace.为前缀即可。
比如：
    replace.sys.author = micheal.li      在模板中以${sys.author}进行使用
    replace.logPrefix = 出现了系统异常     在模板中以${logPrefix}进行使用