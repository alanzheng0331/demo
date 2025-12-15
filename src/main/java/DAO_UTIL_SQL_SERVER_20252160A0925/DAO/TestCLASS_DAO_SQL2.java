package DAO_UTIL_SQL_SERVER_20252160A0925.DAO;

import DAO_UTIL_SQL_SERVER_20252160A0925.Util.TestUtil;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TestCLASS_DAO_SQL2 implements TestInterface_DAO_SQL {
    // 固定表名（你替换为实际表名，比如"user"）
    private static final String TABLE_NAME = "XXX_TABLE";
    // 固定主键列名（你替换为实际主键名，比如"userid"）
    private static final String PRIMARY_KEY = "XXXid";

    /**
     * 公共方法：仅调用TestUtil执行SQL，返回结果（完全按你说的“Util干的事情”）
     */
    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_PUBLIC(String sql) throws Exception {
        // 直接调用Util执行SQL，返回结果（这一步就是“数据库的粘结Util干的事情”）
        return TestUtil.executeSql(sql);
    }

    /**
     * 新增：拼接INSERT SQL → 调用公共方法 → 返回"true"（失败抛异常）
     */
    @Override
    public String TestInterface_DAO_SQL_INSERT(List<Map<String, Object>> list) throws Exception {
        for (Map<String, Object> map : list) {
            // 1. 拼接SQL：insert into XXX (key1,key2) values (value1,value2)
            Set<String> keys = map.keySet();
            // 拼接列名
            String columns = String.join(",", keys);
            // 拼接值（处理字符串加单引号，数字直接拼）
            StringBuilder values = new StringBuilder();
            for (Object value : map.values()) {
                values.append(value instanceof String ? "'" + value + "'" : value).append(",");
            }
            values.deleteCharAt(values.length() - 1); // 删最后一个逗号

            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", TABLE_NAME, columns, values);

            // 2. 调用公共方法执行SQL
            this.TestInterface_DAO_SQL_PUBLIC(sql);
        }
        // 3. 返回成功标识（按你注释要求，成功返回true，失败抛异常）
        return "true";
    }

    /**
     * 删除：拼接DELETE SQL → 调用公共方法 → 返回"true"
     */
    @Override
    public String TestInterface_DAO_SQL_DELETE(Map<String, Object> MY_ID) throws Exception {
        // 1. 拼接SQL：delete from XXX where XXXid=ID值（MY_ID的key是XXXid，value是ID）
        Object idValue = MY_ID.get(PRIMARY_KEY);
        String sql = String.format("DELETE FROM %s WHERE %s = %s",
                TABLE_NAME,
                PRIMARY_KEY,
                idValue instanceof String ? "'" + idValue + "'" : idValue);

        // 2. 调用公共方法执行SQL
        this.TestInterface_DAO_SQL_PUBLIC(sql);

        // 3. 返回成功标识
        return "true";
    }

    /**
     * 修改：拼接UPDATE SQL → 调用公共方法 → 返回"true"
     */
    @Override
    public String TestInterface_DAO_SQL_UPDATE(List<Map<String, Object>> list) throws Exception {
        for (Map<String, Object> map : list) {
            // 1. 拼接SQL：update XXX set key1=value1,key2=value2 where XXXid=ID值
            // 拼接SET子句
            StringBuilder setClause = new StringBuilder();
            Object idValue = null;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (PRIMARY_KEY.equals(key)) {
                    // 主键值单独存，用于WHERE条件
                    idValue = value;
                } else {
                    setClause.append(key).append(" = ").append(value instanceof String ? "'" + value + "'" : value).append(",");
                }
            }
            setClause.deleteCharAt(setClause.length() - 1); // 删最后一个逗号

            String sql = String.format("UPDATE %s SET %s WHERE %s = %s",
                    TABLE_NAME,
                    setClause,
                    PRIMARY_KEY,
                    idValue instanceof String ? "'" + idValue + "'" : idValue);

            // 2. 调用公共方法执行SQL
            this.TestInterface_DAO_SQL_PUBLIC(sql);
        }
        // 3. 返回成功标识
        return "true";
    }

    /**
     * 全查：拼接SELECT * SQL → 调用公共方法 → 返回结果
     */
    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_SELECT_1() throws Exception {
        // 1. 拼接SQL：select * from XXX
        String sql = String.format("SELECT * FROM %s", TABLE_NAME);

        // 2. 调用公共方法执行SQL，获取结果
        List<Map<String, Object>> result = this.TestInterface_DAO_SQL_PUBLIC(sql);

        // 3. 返回结果
        return result;
    }

    /**
     * ID查询：拼接SELECT * WHERE XXXid=ID → 调用公共方法 → 返回结果
     */
    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_SELECT_2(Map<String, Object> MY_ID) throws Exception {
        // 1. 拼接SQL：select * from XXX where XXXid=ID值
        Object idValue = MY_ID.get(PRIMARY_KEY);
        String sql = String.format("SELECT * FROM %s WHERE %s = %s",
                TABLE_NAME,
                PRIMARY_KEY,
                idValue instanceof String ? "'" + idValue + "'" : idValue);

        // 2. 调用公共方法执行SQL，获取结果
        List<Map<String, Object>> result = this.TestInterface_DAO_SQL_PUBLIC(sql);

        // 3. 返回结果
        return result;
    }

    /**
     * 普通条件查询：拼接SELECT * WHERE key=value → 调用公共方法 → 返回结果
     */
    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_SELECT_3(Map<String, Object> MY_WAY) throws Exception {
        // 1. 拼接SQL：select * from XXX where key1=value1 and key2=value2
        StringBuilder whereClause = new StringBuilder();
        for (Map.Entry<String, Object> entry : MY_WAY.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            whereClause.append(key).append(" = ").append(value instanceof String ? "'" + value + "'" : value).append(" AND ");
        }
        whereClause.delete(whereClause.length() - 5, whereClause.length()); // 删最后一个AND

        String sql = String.format("SELECT * FROM %s WHERE %s", TABLE_NAME, whereClause);

        // 2. 调用公共方法执行SQL，获取结果
        List<Map<String, Object>> result = this.TestInterface_DAO_SQL_PUBLIC(sql);

        // 3. 返回结果
        return result;
    }
}