package DAO_UTIL_SQL_SERVER_20252160A0925.DAO;



import DAO_UTIL_SQL_SERVER_20252160A0925.DAO.FINISHED.TestCompanyDAOImpl;
import DAO_UTIL_SQL_SERVER_20252160A0925.DAO.FINISHED.TestInterface_DAO_SQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试主类：优先测试test表（仅ID字段），解决Util异常提示、自动生成数据问题
 */
public class Main {
    // 只用于测试
    public static void main(String[] args) {
        // 初始化test_company的DAO实现类
        TestInterface_DAO_SQL companyDAO = new TestCompanyDAOImpl();

        try {
            // 1. 插入数据
            List<Map<String, Object>> insertList = new ArrayList<>();
            Map<String, Object> row1 = new HashMap<>();
            row1.put("ROOT_ID", 1);
            row1.put("Company_name", "测试公司");
            row1.put("Company_address", "北京市海淀区");
            row1.put("TXT", "这是一家测试公司");
            insertList.add(row1);
            String insertResult = companyDAO.TestInterface_DAO_SQL_INSERT(insertList);
            System.out.println(insertResult);

            // 2. 全部查询
            List<Map<String, Object>> allData = companyDAO.TestInterface_DAO_SQL_SELECT_1();
            System.out.println("全部数据：" + allData);

            // 3. 主键条件查询（假设插入的主键是1）
            Map<String, Object> idMap = new HashMap<>();
            idMap.put("COMPANY_ID", 1);
            List<Map<String, Object>> idData = companyDAO.TestInterface_DAO_SQL_SELECT_2(idMap);
            System.out.println("主键查询数据：" + idData);

            // 4. 普通条件查询
            Map<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("Company_name", "测试公司");
            List<Map<String, Object>> conditionData = companyDAO.TestInterface_DAO_SQL_SELECT_3(conditionMap);
            System.out.println("条件查询数据：" + conditionData);

            // 5. 更新数据
            List<Map<String, Object>> updateList = new ArrayList<>();
            Map<String, Object> updateRow = new HashMap<>();
            updateRow.put("COMPANY_ID", 1);
            updateRow.put("Company_name", "更新后的测试公司");
            updateList.add(updateRow);
            String updateResult = companyDAO.TestInterface_DAO_SQL_UPDATE(updateList);
            System.out.println(updateResult);

            // 6. 删除数据
            String deleteResult = companyDAO.TestInterface_DAO_SQL_DELETE(idMap);
            System.out.println(deleteResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}