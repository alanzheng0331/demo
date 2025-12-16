package DAO_UTIL_SQL_SERVER_20252160A0925.DAO.FINISHED;

import DAO_UTIL_SQL_SERVER_20252160A0925.DAO.TestUtil;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * test_company表的DAO实现类
 */
public class TestCompanyDAOImpl implements TestInterface_DAO_SQL {

    // 数据库名、表名、主键列名
    private static final String SQL_DATABASE = "StudentPartTimeJobDB";
    private static final String SQL_TABLE = "test_company";
    private static final String PRIMARY_KEY = "COMPANY_ID";


    public String getSQLDatabase() {
        return SQL_DATABASE;
    }

    public String getSQLTable() {
        return SQL_TABLE;
    }

    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_PUBLIC(String sql) throws Exception {
        // 调用工具类执行SQL
        return TestUtil.executeSql(sql);
    }

    @Override
    public String TestInterface_DAO_SQL_INSERT(List<Map<String, Object>> list) throws Exception {
        // 参数校验
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("插入的数据列表不能为空");
        }

        Map<String, Object> firstRow = list.get(0);
        Set<String> columns = firstRow.keySet();
        columns.remove(PRIMARY_KEY); // 排除自增主键

        if (columns.isEmpty()) {
            throw new IllegalArgumentException("插入的数据中没有有效列（排除主键后无列）");
        }

        // 拼装列部分：col1,col2,col3
        StringBuilder columnPart = new StringBuilder();
        for (String column : columns) {
            columnPart.append(column).append(",");
        }
        columnPart.deleteCharAt(columnPart.length() - 1);

        // 拼装值部分：(val1,val2),(val3,val4)...
        StringBuilder valuesPart = new StringBuilder();
        for (Map<String, Object> row : list) {
            valuesPart.append("(");
            for (String column : columns) {
                valuesPart.append(formatValue(row.get(column))).append(",");
            }
            valuesPart.deleteCharAt(valuesPart.length() - 1);
            valuesPart.append("),");
        }
        valuesPart.deleteCharAt(valuesPart.length() - 1);

        // 完整INSERT SQL
        String sql = String.format("INSERT INTO %s (%s) VALUES %s", getSQLTable(), columnPart, valuesPart);
        TestInterface_DAO_SQL_PUBLIC(sql);

        return String.format("插入成功，共插入%d条数据到表%s", list.size(), getSQLTable());
    }

    @Override
    public String TestInterface_DAO_SQL_DELETE(Map<String, Object> MY_ID) throws Exception {
        // 参数校验
        if (MY_ID == null || MY_ID.isEmpty()) {
            throw new IllegalArgumentException("删除的ID参数不能为空");
        }
        if (!MY_ID.containsKey(PRIMARY_KEY)) {
            throw new IllegalArgumentException(String.format("删除参数必须包含主键列：%s", PRIMARY_KEY));
        }

        // 拼装DELETE SQL
        Object idValue = MY_ID.get(PRIMARY_KEY);
        String sql = String.format("DELETE FROM %s WHERE %s = %s", getSQLTable(), PRIMARY_KEY, formatValue(idValue));
        TestInterface_DAO_SQL_PUBLIC(sql);

        return String.format("删除成功，表%s中主键%s=%s的记录已删除", getSQLTable(), PRIMARY_KEY, idValue);
    }

    @Override
    public String TestInterface_DAO_SQL_UPDATE(List<Map<String, Object>> list) throws Exception {
        // 参数校验
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("更新的数据列表不能为空");
        }

        int updateCount = 0;
        StringBuilder resultMsg = new StringBuilder();

        for (Map<String, Object> row : list) {
            if (!row.containsKey(PRIMARY_KEY)) {
                throw new IllegalArgumentException(String.format("更新参数必须包含主键列：%s，当前行数据：%s", PRIMARY_KEY, row));
            }

            // 拼装SET部分：col1=val1,col2=val2
            StringBuilder setPart = new StringBuilder();
            Object idValue = row.get(PRIMARY_KEY);

            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String column = entry.getKey();
                if (PRIMARY_KEY.equals(column)) {
                    continue; // 主键不更新
                }
                setPart.append(column).append(" = ").append(formatValue(entry.getValue())).append(",");
            }

            if (setPart.length() == 0) {
                throw new IllegalArgumentException(String.format("更新参数中没有可更新的列，主键%s=%s", PRIMARY_KEY, idValue));
            }

            setPart.deleteCharAt(setPart.length() - 1);

            // 完整UPDATE SQL
            String sql = String.format("UPDATE %s SET %s WHERE %s = %s", getSQLTable(), setPart, PRIMARY_KEY, formatValue(idValue));
            TestInterface_DAO_SQL_PUBLIC(sql);
            updateCount++;
            resultMsg.append(String.format("主键%s=%s更新成功；", PRIMARY_KEY, idValue));
        }

        resultMsg.insert(0, String.format("共更新%d条数据到表%s，", updateCount, getSQLTable()));
        return resultMsg.toString();
    }

    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_SELECT_1() throws Exception {
        // 全部查询
        String sql = String.format("SELECT * FROM %s", getSQLTable());
        return TestInterface_DAO_SQL_PUBLIC(sql);
    }

    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_SELECT_2(Map<String, Object> MY_ID) throws Exception {
        // 参数校验
        if (MY_ID == null || MY_ID.isEmpty()) {
            throw new IllegalArgumentException("查询的ID参数不能为空");
        }
        if (!MY_ID.containsKey(PRIMARY_KEY)) {
            throw new IllegalArgumentException(String.format("查询参数必须包含主键列：%s", PRIMARY_KEY));
        }

        // 主键条件查询
        Object idValue = MY_ID.get(PRIMARY_KEY);
        String sql = String.format("SELECT * FROM %s WHERE %s = %s", getSQLTable(), PRIMARY_KEY, formatValue(idValue));
        return TestInterface_DAO_SQL_PUBLIC(sql);
    }

    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_SELECT_3(Map<String, Object> MY_WAY) throws Exception {
        // 参数校验
        if (MY_WAY == null || MY_WAY.isEmpty()) {
            throw new IllegalArgumentException("查询条件参数不能为空");
        }

        // 拼装WHERE条件：col1=val1 AND col2=val2
        StringBuilder wherePart = new StringBuilder();
        for (Map.Entry<String, Object> entry : MY_WAY.entrySet()) {
            wherePart.append(entry.getKey()).append(" = ").append(formatValue(entry.getValue())).append(" AND ");
        }
        wherePart.delete(wherePart.length() - 5, wherePart.length()); // 移除最后一个" AND "

        // 普通条件查询
        String sql = String.format("SELECT * FROM %s WHERE %s", getSQLTable(), wherePart);
        return TestInterface_DAO_SQL_PUBLIC(sql);
    }

    /**
     * 格式化SQL值：处理字符串单引号转义、null值、数字直接输出
     */
    private String formatValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof String) {
            return "'" + ((String) value).replace("'", "''") + "'"; // SQL Server单引号转义
        }
        return value.toString();
    }
}