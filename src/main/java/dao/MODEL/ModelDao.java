package dao.model;

import util.mypocket.TestUtil;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 实现类：仅负责SQL拼装、过滤，SQL执行完全依赖TestUtil
 * 遵循Java 8规范，数据库操作全部调用TestUtil
 */
public class ModelDao implements TestClass_DAO_SQL, TestInterface_DAO_SQL_PUBLIC, TestInterface_DAO_SQL_Filter {

    // 正则表达式：匹配1=1（忽略大小写和任意空格）
    private Pattern PATTERN_1_EQ_1 = Pattern.compile("1\\s*=\\s*1", Pattern.CASE_INSENSITIVE);
    // 正则表达式：匹配'2'='2'、2='2'、'2'=2、2=2（忽略大小写和任意空格）
    private Pattern PATTERN_2_EQ_2 = Pattern.compile("'?2'?\\s*=\\s*'?2'?", Pattern.CASE_INSENSITIVE);
    // SQL注入相关的特殊字符和关键字（可根据业务需求扩展）
    private String[] SQL_INJECTION_KEYWORDS = {
            "'", "\"", ";", "--", "/*", "*/",  // 特殊符号和SQL注释符
            "exec", "xp_", "sp_", "union",    // SQL执行和联合查询关键字
            "insert", "update", "delete",     // 数据增删改关键字
            "drop", "alter", "create", "truncate" // 表结构操作关键字
    };

    // 数据库表名（可根据业务需求调整，或通过配置注入）
    public String TABLE_NAME = "test";
    public ModelDao(String tableName) {
        if(tableName.isEmpty() || tableName==null){
            TABLE_NAME = "test";
        }else{
            TABLE_NAME = tableName;
        }
    }

    /**
     * 核心过滤方法：按照规则过滤不符合要求的数据
     * @param MY_SELECT 待过滤的数据集
     * @return 符合规则的数据集
     * @throws Exception 过滤过程中出现的异常
     */
    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_Filter(List<Map<String, Object>> MY_SELECT) throws Exception {
        // 初始化合格数据结果集
        List<Map<String, Object>> validDataList = new ArrayList<>();

        // 第一步：校验输入数据集是否为空
        if (MY_SELECT == null || MY_SELECT.isEmpty()) {
            throw new IllegalArgumentException("输入的数据集MY_SELECT不能为空");
        }

        // 遍历每个Map数据进行过滤校验
        for (Map<String, Object> dataMap : MY_SELECT) {
            // 过滤null或空的Map
            if (dataMap == null || dataMap.isEmpty()) {
                continue;
            }
            // 标记当前Map是否符合所有规则
            boolean isValid = true;
            // 第二步：校验Map的Key是否有效（非空，若需列名校验可扩展Util）
            for (String key : dataMap.keySet()) {
                if (key == null || key.trim().isEmpty()) {
                    isValid = false;
                    break; // Key无效，直接跳过当前Map
                }
            }
            if (!isValid) {
                continue;
            }
            // 第三步：校验Value是否包含1=1或'2'='2'，以及SQL注入特殊内容
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                Object valueObj = entry.getValue();
                String value = valueObj == null ? "" : valueObj.toString().trim();

                // 检查是否包含1=1或'2'='2'
                if (PATTERN_1_EQ_1.matcher(value).find() || PATTERN_2_EQ_2.matcher(value).find()) {
                    isValid = false;
                    break;
                }
                // 检查是否包含SQL注入特殊字符/关键字
                if (containsSqlInjectionKeywords(value)) {
                    isValid = false;
                    break;
                }
            }
            // 所有校验通过，加入合格数据集
            if (isValid) {
                validDataList.add(dataMap);
            }
        }

        return validDataList;
    }

    /**
     * 辅助方法：检查字符串是否包含SQL注入相关的特殊字符或关键字
     * @param value 要检查的字符串
     * @return true-包含非法内容，false-不包含
     */
    private boolean containsSqlInjectionKeywords(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        // 转为小写，忽略大小写匹配
        String lowerValue = value.toLowerCase();
        for (String keyword : SQL_INJECTION_KEYWORDS) {
            if (lowerValue.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 工具方法：转义SQL中的值，处理字符串的单引号，null值转为NULL
     * @param value 原始值
     * @return 转义后的SQL值字符串
     */
    private String escapeValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof String) {
            // 转义单引号：将'替换为''，防止SQL注入
            String strValue = (String) value;
            return "'" + strValue.replace("'", "''") + "'";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        // 其他类型转为字符串并转义
        return "'" + value.toString().replace("'", "''") + "'";
    }

    /**
     * 工具方法：拼装WHERE条件SQL语句（AND连接，仅支持等于条件）
     * @param conditionMap 条件键值对（key=列名，value=列值）
     * @return WHERE条件字符串（如：id=1 AND name='test'，若为空返回空字符串）
     * @throws Exception 条件数据非法时抛出异常
     */
    private String buildConditionSql(Map<String, Object> conditionMap) throws Exception {
        if (conditionMap == null || conditionMap.isEmpty()) {
            return "";
        }
        // 过滤条件数据，确保无非法内容
        List<Map<String, Object>> filterList = TestInterface_DAO_SQL_Filter(Collections.singletonList(conditionMap));
        if (filterList.isEmpty()) {
            throw new IllegalArgumentException("条件数据包含非法内容，无法拼装WHERE语句");
        }
        Map<String, Object> validCondition = filterList.get(0);
        StringBuilder conditionSb = new StringBuilder();
        for (Map.Entry<String, Object> entry : validCondition.entrySet()) {
            String colName = entry.getKey().trim();
            String colValue = escapeValue(entry.getValue());
            if (conditionSb.length() > 0) {
                conditionSb.append(" AND ");
            }
            conditionSb.append(colName).append(" = ").append(colValue);
        }
        return conditionSb.toString();
    }

    /**
     * 执行拼装好的SQL语句，返回数据库执行结果
     * @param sql 拼装完成的SQL语句（由其他类负责拼装）
     * @return List<Map<String, Object>>：查询语句返回结果集，增删改语句返回空列表
     * @throws Exception SQL执行过程中的异常（包括空SQL、SQL语法错误、数据库连接异常等）
     */
    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_PUBLIC(String sql) throws Exception {
        // 1. 校验SQL语句是否为空（避免执行空SQL）
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("传入的SQL语句不能为空");
        }

        // 2. 调用工具类执行SQL（工具类负责数据库连接、SQL执行、结果集解析、资源关闭）
        List<Map<String, Object>> result = TestUtil.executeSql(sql);

        // 3. 返回执行结果（符合接口要求：查询返回结果集，增删改返回空列表）
        return result;
    }

    /**
     * 拼装INSERT语句（支持批量插入）
     * @param list 插入数据列表（每个Map对应一条数据，key=列名，value=列值）
     * @return 拼装好的INSERT SQL语句
     * @throws Exception 数据非法或拼装失败时抛出异常
     */
    @Override
    public String TestInterface_DAO_SQL_INSERT(List<Map<String, Object>> list) throws Exception {
        // 1. 校验输入列表是否为空
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("插入数据列表不能为空");
        }

        // 2. 过滤非法数据（调用过滤方法）
        List<Map<String, Object>> validList = TestInterface_DAO_SQL_Filter(list);
        if (validList.isEmpty()) {
            throw new IllegalArgumentException("所有插入数据均包含非法内容，无法拼装INSERT语句");
        }

        // 3. 获取列名（取第一条有效数据的列名，假设所有数据列名一致）
        Map<String, Object> firstData = validList.get(0);
        List<String> colNames = new ArrayList<>(firstData.keySet());
        if (colNames.isEmpty()) {
            throw new IllegalArgumentException("插入数据的列名不能为空");
        }

        // 4. 拼装INSERT语句的列名部分
        StringBuilder colSb = new StringBuilder();
        for (int i = 0; i < colNames.size(); i++) {
            String colName = colNames.get(i).trim();
            if (colSb.length() > 0) {
                colSb.append(", ");
            }
            colSb.append(colName);
        }

        // 5. 拼装VALUES部分（批量插入）
        StringBuilder valuesSb = new StringBuilder();
        for (Map<String, Object> dataMap : validList) {

            if (dataMap.values()==null ||dataMap.values().isEmpty()) {
                // 您的处理逻辑
                continue;
            }

            if (valuesSb.length() > 0) {
                valuesSb.append(", ");
            }
            valuesSb.append("(");
            for (int i = 0; i < colNames.size(); i++) {
                String colName = colNames.get(i);
                Object value = dataMap.get(colName);
                String escValue = escapeValue(value);
                if (i > 0) {
                    valuesSb.append(", ");
                }
                valuesSb.append(escValue);
            }
            valuesSb.append(")");
        }

        // 6. 拼接完整的INSERT SQL语句
        //System.out.println(String.format("INSERT INTO %s (%s) VALUES %s", TABLE_NAME, colSb.toString(), valuesSb.toString()));
        TestInterface_DAO_SQL_PUBLIC(
                String.format("INSERT INTO %s (%s) VALUES %s", TABLE_NAME, colSb.toString(), valuesSb.toString())
        );
        return String.format("INSERT INTO %s (%s) VALUES %s", TABLE_NAME, colSb.toString(), valuesSb.toString());
    }

    /**
     * 拼装DELETE语句（根据MY_ID条件删除）
     * @param MY_ID 删除条件（key=列名，value=列值）
     * @return 拼装好的DELETE SQL语句
     * @throws Exception 条件非法或拼装失败时抛出异常
     */
    @Override
    public String TestInterface_DAO_SQL_DELETE(Map<String, Object> MY_ID) throws Exception {
        // 1. 校验输入条件是否为空
        if (MY_ID == null || MY_ID.isEmpty()) {
            throw new IllegalArgumentException("删除条件MY_ID不能为空");
        }

        // 2. 拼装WHERE条件语句
        String conditionSql = buildConditionSql(MY_ID);
        if (conditionSql.isEmpty()) {
            throw new IllegalArgumentException("删除条件无效，无法拼装DELETE语句");
        }

        // 3. 拼接完整的DELETE SQL语句
        TestInterface_DAO_SQL_PUBLIC(
                String.format("DELETE FROM %s WHERE %s", TABLE_NAME, conditionSql)
        );
        return String.format("DELETE FROM %s WHERE %s", TABLE_NAME, conditionSql);
    }

    /**
     * 拼装UPDATE语句（支持批量更新，条件列以where_为前缀）
     * @param list 更新数据列表（每个Map中：key=列名（where_前缀为条件列），value=列值）
     * @return 拼装好的UPDATE SQL语句（多条语句用分号分隔）
     * @throws Exception 数据非法或拼装失败时抛出异常
     */
    @Override
    public String TestInterface_DAO_SQL_UPDATE(List<Map<String, Object>> list) throws Exception {
        // 1. 校验输入列表是否为空
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("更新数据列表不能为空");
        }

        // 2. 过滤非法数据（调用过滤方法）
        List<Map<String, Object>> validList = TestInterface_DAO_SQL_Filter(list);
        if (validList.isEmpty()) {
            throw new IllegalArgumentException("所有更新数据均包含非法内容，无法拼装UPDATE语句");
        }

        // 3. 拼装UPDATE语句（多条语句用分号分隔）
        StringBuilder sqlSb = new StringBuilder();
        for (Map<String, Object> dataMap : validList) {
            // 拆分更新列（非where_前缀）和条件列（where_前缀，去掉前缀作为列名）
            Map<String, Object> setMap = new HashMap<>();
            Map<String, Object> whereMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                String key = entry.getKey().trim();
                Object value = entry.getValue();
                if (key.startsWith("where_")) {
                    // 条件列：去掉where_前缀
                    String colName = key.substring("where_".length()).trim();
                    if (!colName.isEmpty()) {
                        whereMap.put(colName, value);
                    }
                } else {
                    // 更新列
                    setMap.put(key, value);
                }
            }

            // 校验更新列和条件列是否为空
            if (setMap.isEmpty()) {
                throw new IllegalArgumentException("更新数据中未包含有效更新列（非where_前缀的键）");
            }
            if (whereMap.isEmpty()) {
                throw new IllegalArgumentException("更新数据中未包含有效条件列（where_前缀的键）");
            }

            // 拼装SET部分
            StringBuilder setSb = new StringBuilder();
            for (Map.Entry<String, Object> entry : setMap.entrySet()) {

                if (entry.getValue() == null || entry.getValue().toString().trim().isEmpty()) {
                    // 您的处理逻辑
                    //为空 不管
                    continue;
                }

                String colName = entry.getKey().trim();
                String colValue = escapeValue(entry.getValue());
                if (setSb.length() > 0) {
                    setSb.append(", ");
                }
                setSb.append(colName).append(" = ").append(colValue);
            }

            // 拼装WHERE部分
            String conditionSql = buildConditionSql(whereMap);
            if (conditionSql.isEmpty()) {
                throw new IllegalArgumentException("更新条件无效，无法拼装UPDATE语句");
            }

            // 拼接单条UPDATE语句
            String singleSql = String.format("UPDATE %s SET %s WHERE %s", TABLE_NAME, setSb.toString(), conditionSql);
            if (sqlSb.length() > 0) {
                sqlSb.append("; ");
            }
            sqlSb.append(singleSql);
        }
        TestInterface_DAO_SQL_PUBLIC(
                sqlSb.toString()
        );
        // 4. 返回拼装好的SQL语句
        return sqlSb.toString();
    }

    /**
     * 执行全表查询，返回结果集
     * @return 全表数据结果集
     * @throws Exception 查询执行失败时抛出异常
     */
    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_SELECT_1() throws Exception {
        // 拼装全表查询SQL
        String sql = String.format("SELECT * FROM %s", TABLE_NAME);
        // 执行SQL并返回结果
        return TestInterface_DAO_SQL_PUBLIC(sql);
    }

    /**
     * 根据ID条件查询，返回结果集
     * @param MY_ID 查询条件（key=列名，value=列值）
     * @return 符合条件的结果集
     * @throws Exception 查询执行失败时抛出异常
     */
    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_SELECT_2(Map<String, Object> MY_ID) throws Exception {
        // 1. 校验输入条件是否为空
        if (MY_ID == null || MY_ID.isEmpty()) {
            throw new IllegalArgumentException("查询条件MY_ID不能为空");
        }

        // 2. 拼装WHERE条件语句
        String conditionSql = buildConditionSql(MY_ID);
        if (conditionSql.isEmpty()) {
            throw new IllegalArgumentException("查询条件无效，无法拼装SELECT语句");
        }

        // 3. 拼装查询SQL并执行
        String sql = String.format("SELECT * FROM %s WHERE %s", TABLE_NAME, conditionSql);
        return TestInterface_DAO_SQL_PUBLIC(sql);
    }

    /**
     * 多条件灵活查询（支持模糊查询），返回结果集
     * @param MY_WAY 查询条件列表（单个Map内键值对为AND连接，多个Map之间为OR连接；key=列名，value=列值，值包含%则为模糊查询）
     * @return 符合条件的结果集
     * @throws Exception 查询执行失败时抛出异常
     */
    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_SELECT_3(List<Map<String, Object>> MY_WAY) throws Exception {
        // 1. 校验输入条件是否为空（List本身或内部无有效Map均需校验）
        if (MY_WAY == null || MY_WAY.isEmpty()) {
            throw new IllegalArgumentException("查询条件MY_WAY不能为空");
        }

        // 2. 过滤条件数据，确保无非法内容（直接传入List，无需包装）
        List<Map<String, Object>> filterList = TestInterface_DAO_SQL_Filter(MY_WAY);
        if (filterList.isEmpty()) {
            throw new IllegalArgumentException("查询条件包含非法内容，无法拼装SELECT语句");
        }

        // 3. 拼装WHERE条件语句（支持模糊查询：值包含%则用LIKE）
        // 外层：多个Map之间用OR连接（加括号）；内层：单个Map内键值对用AND连接
        StringBuilder conditionSb = new StringBuilder();
        for (Map<String, Object> validWay : filterList) {
            // 单个Map的条件拼装容器
            StringBuilder singleConditionSb = new StringBuilder();
            for (Map.Entry<String, Object> entry : validWay.entrySet()) {
                // 跳过空值（null或空字符串）
                if (entry.getValue() == null || entry.getValue().toString().trim().isEmpty()) {
                    continue;
                }

                String colName = entry.getKey().trim();
                Object valueObj = entry.getValue();
                String colValue = escapeValue(valueObj);

                // 处理模糊查询：字符串且包含%则用LIKE（保留原逻辑）
                String operator = "=";
                if (valueObj instanceof String && ((String) valueObj).contains("%")) {
                    operator = "LIKE";
                }

                // 单个Map内的键值对用AND连接
                if (singleConditionSb.length() > 0) {
                    singleConditionSb.append(" AND ");
                }
                singleConditionSb.append(colName).append(" ").append(operator).append(" ").append(colValue);
            }

            // 跳过空的单个Map条件（所有键值对都是空值的情况）
            if (singleConditionSb.length() == 0) {
                continue;
            }

            // 多个Map之间用OR连接，并用括号包裹单个Map的条件（避免优先级问题）
            if (conditionSb.length() > 0) {
                conditionSb.append(" OR ");
            }
            conditionSb.append("(").append(singleConditionSb).append(")");
        }

        // 校验最终条件是否为空（所有Map都无有效键值对）
        if (conditionSb.length() == 0) {
            throw new IllegalArgumentException("查询条件无效，无法拼装SELECT语句");
        }

        // 4. 拼装查询SQL并执行
        String sql = String.format("SELECT * FROM %s WHERE %s", TABLE_NAME, conditionSb.toString());
        System.out.println(sql);
        return TestInterface_DAO_SQL_PUBLIC(sql);
    }
}