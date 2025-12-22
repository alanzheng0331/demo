package dao.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 插入数据的数据源工具类
 * 规则：
 * 1. 移除自增主键字段（数据库自动生成，插入时无需传递）；
 * 2. 仅保留Map的Key（表业务字段名，严格对应数据库）；
 * 3. Value设为空值（数值类型null/字符类型空字符串）；
 * 4. 不使用所有字段（可选部分字段）。
 * DAO层可调用此类方法获取空值数据模板，后续由Servlet赋值后执行INSERT
 */
public class PublicDao {

    public static List<Map<String, Object>> getTestUserInsertTemplate() {
        List<Map<String, Object>> dataList = new ArrayList<>();

        // 仅保留全字段模板（业务场景单一的情况）
        Map<String, Object> data = new HashMap<>();
        //次要
        data.put("PASSWORD", "");
        data.put("NAME", "");
        data.put("PHONE", "");
        data.put("EMAIL", "");
        data.put("GENDER", "");
        dataList.add(data);
        //额外添加
        //【】
        return dataList;
    }
}