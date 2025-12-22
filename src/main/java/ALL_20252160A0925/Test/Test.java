package ALL_20252160A0925.Test;

import ALL_20252160A0925.D3_DAO.MODEL.TestClass_DAO_SQL_MOD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试类：测试TestClass_DAO_SQL_MOD的所有核心方法
 * 覆盖test表、test_company表的INSERT/DELETE/UPDATE/SELECT操作
 */
public class Test {
    public static void mainX() {
        // ========== 1. 初始化DAO实例（分别对应test表和test_company表） ==========
        TestClass_DAO_SQL_MOD testTableDao = new TestClass_DAO_SQL_MOD("test");
        TestClass_DAO_SQL_MOD companyTableDao = new TestClass_DAO_SQL_MOD("test_company");
        System.out.println(testTableDao.TABLE_NAME);
        try {
            // ========== 2. 测试test表的INSERT操作 ==========
            System.out.println("===== 开始测试test表INSERT操作 =====");
            // 准备插入数据（符合test表结构：ID、N1、N2、N3、N4、N5）
            List<Map<String, Object>> testInsertList = new ArrayList<>();
            // 第一条数据
            Map<String, Object> testData1 = new HashMap<>();

            testData1.put("N1", "测试数据1");
            testData1.put("N2", "数据1-2");
            testData1.put("N3", "数据1-3");
            testData1.put("N4", "数据1-4");
            testData1.put("N5", "1");
            testData1.put("ID", 1);
            testInsertList.add(testData1);
            // 第二条数据
            Map<String, Object> testData2 = new HashMap<>();

            testData2.put("N1", "测试数据2");
            testData2.put("N2", "数据2-2");
            testData2.put("N3", "数据2-3");
            testData2.put("N4", "数据2-4");
            testData2.put("N5", "2");
            testData2.put("ID", 2);
            testInsertList.add(testData2);
            // 执行INSERT并打印SQL
            String testInsertSql = testTableDao.TestInterface_DAO_SQL_INSERT(testInsertList);
            System.out.println("test表INSERT SQL：" + testInsertSql);

            // ========== 3. 测试test表的SELECT_1（全表查询） ==========
            System.out.println("\n===== 开始测试test表SELECT_1（全表查询） =====");
            List<Map<String, Object>> testSelect1Result = testTableDao.TestInterface_DAO_SQL_SELECT_1();
            printResult(testSelect1Result);

            // ========== 4. 测试test表的SELECT_2（根据ID条件查询） ==========
            System.out.println("\n===== 开始测试test表SELECT_2（ID=1查询） =====");
            Map<String, Object> testSelect2Condition = new HashMap<>();
            testSelect2Condition.put("ID", 1);
            List<Map<String, Object>> testSelect2Result = testTableDao.TestInterface_DAO_SQL_SELECT_2(testSelect2Condition);
            printResult(testSelect2Result);

            // ========== 5. 测试test表的SELECT_3（模糊查询） ==========
            System.out.println("\n===== 开始测试test表SELECT_3（N1模糊查询：包含'测试'） =====");
            Map<String, Object> testSelect3Condition = new HashMap<>();
            testSelect3Condition.put("N1", "%测试%"); // %表示模糊查询
            List<Map<String, Object>> testSelect3Result = testTableDao.TestInterface_DAO_SQL_SELECT_3(testSelect3Condition);
            printResult(testSelect3Result);

            // ========== 6. 测试test表的UPDATE操作 ==========
            System.out.println("\n===== 开始测试test表UPDATE操作（更新ID=1的N1字段） =====");
            List<Map<String, Object>> testUpdateList = new ArrayList<>();
            Map<String, Object> testUpdateData = new HashMap<>();
            testUpdateData.put("N1", "更新后的测试数据1"); // 要更新的字段
            testUpdateData.put("where_ID", 1); // 条件字段（where_前缀）
            testUpdateList.add(testUpdateData);
            // 执行UPDATE并打印SQL
            String testUpdateSql = testTableDao.TestInterface_DAO_SQL_UPDATE(testUpdateList);
            System.out.println("test表UPDATE SQL：" + testUpdateSql);
            // 更新后再次查询ID=1的数据
            System.out.println("更新后ID=1的数据：");
            List<Map<String, Object>> testUpdateResult = testTableDao.TestInterface_DAO_SQL_SELECT_2(testSelect2Condition);
            printResult(testUpdateResult);

            // ========== 7. 测试test_company表的INSERT操作 ==========
            System.out.println("\n===== 开始测试test_company表INSERT操作 =====");
            List<Map<String, Object>> companyInsertList = new ArrayList<>();
            Map<String, Object> companyData1 = new HashMap<>();
            companyData1.put("Company_name", "测试公司1");
            companyData1.put("Company_address", "测试地址1");
            companyData1.put("TXT", "公司简介1");
            companyInsertList.add(companyData1);
            String companyInsertSql = companyTableDao.TestInterface_DAO_SQL_INSERT(companyInsertList);
            System.out.println("test_company表INSERT SQL：" + companyInsertSql);

            // ========== 8. 测试test_company表的SELECT_3（模糊查询公司名称） ==========
            System.out.println("\n===== 开始测试test_company表SELECT_3（公司名称包含'测试'） =====");
            Map<String, Object> companySelect3Condition = new HashMap<>();
            companySelect3Condition.put("Company_name", "%测试%");
            List<Map<String, Object>> companySelect3Result = companyTableDao.TestInterface_DAO_SQL_SELECT_3(companySelect3Condition);
            printResult(companySelect3Result);

            // ========== 9. 测试test表的DELETE操作 ==========
            System.out.println("\n===== 开始测试test表DELETE操作（ID=1删除） =====");
            Map<String, Object> testDeleteCondition = new HashMap<>();
            testDeleteCondition.put("ID", 1);
            String testDeleteSql = testTableDao.TestInterface_DAO_SQL_DELETE(testDeleteCondition);
            System.out.println("test表DELETE SQL：" + testDeleteSql);
            // 删除后全表查询
            System.out.println("删除后test表全表数据：");
            List<Map<String, Object>> testDeleteResult = testTableDao.TestInterface_DAO_SQL_SELECT_1();
            printResult(testDeleteResult);

        } catch (Exception e) {
            // 捕获并打印异常信息
            System.err.println("测试过程中出现异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 工具方法：打印查询结果集
     * @param result 结果集（List<Map<String, Object>>）
     */
    private static void printResult(List<Map<String, Object>> result) {
        if (result == null || result.isEmpty()) {
            System.out.println("结果集为空");
            return;
        }
        for (int i = 0; i < result.size(); i++) {
            Map<String, Object> row = result.get(i);
            System.out.println("第" + (i + 1) + "行数据：");
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                System.out.println("  " + entry.getKey() + "：" + entry.getValue());
            }
        }
    }
}