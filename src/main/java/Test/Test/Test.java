package ALL_20252160A0925.Test.Test;

import ALL_20252160A0925.D3_DAO.MODEL.TestClass_DAO_SQL_MOD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试类：匹配test_company、test_resume、test_root、test_user表结构
 * 覆盖所有表的INSERT/DELETE/UPDATE/SELECT操作（含模糊查询、异常场景）
 * 注意：SQL Server自增IDENTITY字段不手动插入，测试数据适配实际表字段
 */
public class Test {
    public  void mainX() {
        // ========== 1. 测试test_company表 ==========
        System.out.println("===== 开始测试test_company表 =====");
        TestClass_DAO_SQL_MOD companyDao = new TestClass_DAO_SQL_MOD("test_company");
        testCompanyTable(companyDao);

        // ========== 2. 测试test_resume表（关联test_company） ==========
        System.out.println("\n===== 开始测试test_resume表 =====");
        TestClass_DAO_SQL_MOD resumeDao = new TestClass_DAO_SQL_MOD("test_resume");
        // 先获取test_company中新增的COMPANY_ID（假设新增后返回的第一条数据COMPANY_ID为1，可根据实际调整）
        int testCompanyId = 1;
        testResumeTable(resumeDao, testCompanyId);

        // ========== 3. 测试test_root表 ==========
        System.out.println("\n===== 开始测试test_root表 =====");
        TestClass_DAO_SQL_MOD rootDao = new TestClass_DAO_SQL_MOD("test_root");
        testRootTable(rootDao);

        // ========== 4. 测试test_user表 ==========
        System.out.println("\n===== 开始测试test_user表 =====");
        TestClass_DAO_SQL_MOD userDao = new TestClass_DAO_SQL_MOD("test_user");
        testUserTable(userDao);

        // ========== 5. 测试异常场景（空参数、SQL注入、无效条件等） ==========
        System.out.println("\n===== 开始测试异常场景 =====");
        testExceptionScenarios();
    }

    /**
     * 测试test_company表的所有核心方法
     * @param dao test_company表的DAO实例
     */
    private static void testCompanyTable(TestClass_DAO_SQL_MOD dao) {
        try {
            // ========== 步骤1：测试INSERT（批量插入，不包含自增的COMPANY_ID） ==========
            System.out.println("\n--- 测试INSERT方法 ---");
            List<Map<String, Object>> insertList = new ArrayList<>();
            // 第一条插入数据
            Map<String, Object> companyData1 = new HashMap<>();
            companyData1.put("ROOT_ID", 1); // 关联test_root的ROOT_ID
            companyData1.put("Company_name", "阿里云科技有限公司");
            companyData1.put("Company_address", "浙江省杭州市余杭区文一西路969号");
            companyData1.put("TXT", "阿里云是全球领先的云计算服务商");
            insertList.add(companyData1);
            // 第二条插入数据（包含%，用于后续模糊查询）
            Map<String, Object> companyData2 = new HashMap<>();
            companyData2.put("ROOT_ID", 2);
            companyData2.put("Company_name", "腾讯科技%有限公司"); // 包含%，测试模糊查询
            companyData2.put("Company_address", "广东省深圳市南山区腾讯大厦");
            companyData2.put("TXT", "腾讯专注于互联网及科技服务");
            insertList.add(companyData2);
            // 执行插入
            String insertSql = dao.TestInterface_DAO_SQL_INSERT(insertList);
            System.out.println("test_company插入SQL：" + insertSql);

            // ========== 步骤2：测试SELECT_1（全表查询） ==========
            System.out.println("\n--- 测试SELECT_1（全表查询） ---");
            List<Map<String, Object>> select1Result = dao.TestInterface_DAO_SQL_SELECT_1();
            System.out.println("test_company全表数据：" + select1Result);

            // ========== 步骤3：测试SELECT_2（条件查询：COMPANY_ID） ==========
            System.out.println("\n--- 测试SELECT_2（COMPANY_ID条件查询） ---");
            Map<String, Object> companyCondition = new HashMap<>();
            companyCondition.put("COMPANY_ID", 1); // 查询新增的第一条数据（自增ID=1）
            List<Map<String, Object>> select2Result = dao.TestInterface_DAO_SQL_SELECT_2(companyCondition);
            System.out.println("test_company中COMPANY_ID=1的数据：" + select2Result);

            // ========== 步骤4：测试SELECT_3（多条件+模糊查询） ==========
            System.out.println("\n--- 测试SELECT_3（多条件+模糊查询） ---");
            List<Map<String, Object>> select3List = new ArrayList<>();
            // 第一个条件Map：Company_name模糊查询（包含“腾讯”）+ ROOT_ID=2（AND连接）
            Map<String, Object> condition1 = new HashMap<>();
            condition1.put("Company_name", "%腾讯%"); // 模糊查询：公司名称包含“腾讯”
            condition1.put("ROOT_ID", 2);
            select3List.add(condition1);
            // 第二个条件Map：COMPANY_ID=1（OR连接）
            Map<String, Object> condition2 = new HashMap<>();
            condition2.put("COMPANY_ID", 1);
            select3List.add(condition2);
            // 执行多条件查询
            List<Map<String, Object>> select3Result = dao.TestInterface_DAO_SQL_SELECT_3(select3List);
            System.out.println("test_company多条件查询结果：" + select3Result);

            // ========== 步骤5：测试UPDATE（更新，条件：COMPANY_ID） ==========
            System.out.println("\n--- 测试UPDATE方法 ---");
            List<Map<String, Object>> updateList = new ArrayList<>();
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("Company_address", "浙江省杭州市余杭区文一西路969号阿里云大厦"); // 更新地址
            updateData.put("where_COMPANY_ID", 1); // 条件：COMPANY_ID=1（where_前缀表示条件列）
            updateList.add(updateData);
            // 执行更新
            String updateSql = dao.TestInterface_DAO_SQL_UPDATE(updateList);
            System.out.println("test_company更新SQL：" + updateSql);
            // 验证更新结果
            List<Map<String, Object>> updatedResult = dao.TestInterface_DAO_SQL_SELECT_2(companyCondition);
            System.out.println("test_company中COMPANY_ID=1更新后的数据：" + updatedResult);

            // ========== 步骤6：测试DELETE（条件删除：COMPANY_ID） ==========
            System.out.println("\n--- 测试DELETE方法 ---");
            Map<String, Object> deleteCondition1 = new HashMap<>();
            deleteCondition1.put("COMPANY_ID", 1);
            String deleteSql1 = dao.TestInterface_DAO_SQL_DELETE(deleteCondition1);
            System.out.println("test_company删除COMPANY_ID=1的SQL：" + deleteSql1);

            Map<String, Object> deleteCondition2 = new HashMap<>();
            deleteCondition2.put("COMPANY_ID", 2);
            String deleteSql2 = dao.TestInterface_DAO_SQL_DELETE(deleteCondition2);
            System.out.println("test_company删除COMPANY_ID=2的SQL：" + deleteSql2);

            // 验证删除结果
            List<Map<String, Object>> afterDeleteResult = dao.TestInterface_DAO_SQL_SELECT_1();
            System.out.println("test_company删除后全表数据：" + afterDeleteResult);

        } catch (Exception e) {
            System.err.println("test_company表测试异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试test_resume表的所有核心方法（关联test_company的COMPANY_ID）
     * @param dao test_resume表的DAO实例
     * @param companyId 关联的test_company的COMPANY_ID
     */
    private static void testResumeTable(TestClass_DAO_SQL_MOD dao, int companyId) {
        try {
            // ========== 步骤1：测试INSERT（批量插入，不包含自增的RESUME_ID） ==========
            System.out.println("\n--- 测试INSERT方法 ---");
            List<Map<String, Object>> insertList = new ArrayList<>();
            Map<String, Object> resumeData1 = new HashMap<>();
            resumeData1.put("COMPANY_ID", companyId); // 关联test_company的COMPANY_ID
            resumeData1.put("TXT", "Java开发工程师简历：熟练掌握SpringBoot、MyBatis等框架");
            insertList.add(resumeData1);

            Map<String, Object> resumeData2 = new HashMap<>();
            resumeData2.put("COMPANY_ID", companyId);
            resumeData2.put("TXT", "前端开发工程师简历：熟练掌握Vue、React、TypeScript");
            insertList.add(resumeData2);

            String insertSql = dao.TestInterface_DAO_SQL_INSERT(insertList);
            System.out.println("test_resume插入SQL：" + insertSql);

            // ========== 步骤2：测试SELECT_1（全表查询） ==========
            System.out.println("\n--- 测试SELECT_1（全表查询） ---");
            List<Map<String, Object>> select1Result = dao.TestInterface_DAO_SQL_SELECT_1();
            System.out.println("test_resume全表数据：" + select1Result);

            // ========== 步骤3：测试SELECT_2（条件查询：RESUME_ID） ==========
            System.out.println("\n--- 测试SELECT_2（RESUME_ID条件查询） ---");
            Map<String, Object> resumeCondition = new HashMap<>();
            resumeCondition.put("RESUME_ID", 1);
            List<Map<String, Object>> select2Result = dao.TestInterface_DAO_SQL_SELECT_2(resumeCondition);
            System.out.println("test_resume中RESUME_ID=1的数据：" + select2Result);

            // ========== 步骤4：测试SELECT_3（多条件+模糊查询：TXT包含“Java”） ==========
            System.out.println("\n--- 测试SELECT_3（多条件+模糊查询） ---");
            List<Map<String, Object>> select3List = new ArrayList<>();
            Map<String, Object> condition1 = new HashMap<>();
            condition1.put("TXT", "%Java%"); // 模糊查询：简历包含“Java”
            condition1.put("COMPANY_ID", companyId);
            select3List.add(condition1);

            List<Map<String, Object>> select3Result = dao.TestInterface_DAO_SQL_SELECT_3(select3List);
            System.out.println("test_resume多条件查询结果：" + select3Result);

            // ========== 步骤5：测试UPDATE（更新简历内容，条件：RESUME_ID） ==========
            System.out.println("\n--- 测试UPDATE方法 ---");
            List<Map<String, Object>> updateList = new ArrayList<>();
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("TXT", "Java开发工程师简历：熟练掌握SpringBoot、MyBatis、Redis等技术");
            updateData.put("where_RESUME_ID", 1);
            updateList.add(updateData);

            String updateSql = dao.TestInterface_DAO_SQL_UPDATE(updateList);
            System.out.println("test_resume更新SQL：" + updateSql);

            // ========== 步骤6：测试DELETE（条件删除：RESUME_ID） ==========
            System.out.println("\n--- 测试DELETE方法 ---");
            Map<String, Object> deleteCondition = new HashMap<>();
            deleteCondition.put("RESUME_ID", 1);
            String deleteSql = dao.TestInterface_DAO_SQL_DELETE(deleteCondition);
            System.out.println("test_resume删除RESUME_ID=1的SQL：" + deleteSql);

        } catch (Exception e) {
            System.err.println("test_resume表测试异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试test_root表的所有核心方法（处理is_root字段，按bit类型0/1）
     * @param dao test_root表的DAO实例
     */
    private static void testRootTable(TestClass_DAO_SQL_MOD dao) {
        try {
            // ========== 步骤1：测试INSERT（不包含自增的ROOT_ID） ==========
            System.out.println("\n--- 测试INSERT方法 ---");
            List<Map<String, Object>> insertList = new ArrayList<>();
            Map<String, Object> rootData1 = new HashMap<>();
            rootData1.put("NAME", "超级管理员");
            rootData1.put("PASSWORD", "root123456");
            rootData1.put("PHONE", "11122223333");
            rootData1.put("EMAIL", "root@example.com");
            rootData1.put("GENDER", "男");
            insertList.add(rootData1);

            Map<String, Object> rootData2 = new HashMap<>();
            rootData2.put("NAME", "管理员小张");
            rootData2.put("PASSWORD", "admin123456");
            rootData2.put("PHONE", "11122223333");
            rootData2.put("EMAIL", "admin@example.com");
            rootData2.put("GENDER", "女");
            insertList.add(rootData2);

            String insertSql = dao.TestInterface_DAO_SQL_INSERT(insertList);
            System.out.println("test_root插入SQL：" + insertSql);

            // ========== 步骤2：测试SELECT_1（全表查询） ==========
            System.out.println("\n--- 测试SELECT_1（全表查询） ---");
            List<Map<String, Object>> select1Result = dao.TestInterface_DAO_SQL_SELECT_1();
            System.out.println("test_root全表数据：" + select1Result);

            // ========== 步骤3：测试SELECT_2（条件查询：ROOT_ID） ==========
            System.out.println("\n--- 测试SELECT_2（ROOT_ID条件查询） ---");
            Map<String, Object> rootCondition = new HashMap<>();
            rootCondition.put("ROOT_ID", 1);
            List<Map<String, Object>> select2Result = dao.TestInterface_DAO_SQL_SELECT_2(rootCondition);
            System.out.println("test_root中ROOT_ID=1的数据：" + select2Result);

            // ========== 步骤4：测试SELECT_3（多条件：is_root=1 + NAME包含“管理员”） ==========
            System.out.println("\n--- 测试SELECT_3（多条件+模糊查询） ---");
            List<Map<String, Object>> select3List = new ArrayList<>();
            Map<String, Object> condition1 = new HashMap<>();
            condition1.put("NAME", "%管理员%");
            select3List.add(condition1);

            List<Map<String, Object>> select3Result = dao.TestInterface_DAO_SQL_SELECT_3(select3List);
            System.out.println("test_root多条件查询结果：" + select3Result);

            // ========== 步骤5：测试UPDATE（更新密码，条件：ROOT_ID） ==========
            System.out.println("\n--- 测试UPDATE方法 ---");
            List<Map<String, Object>> updateList = new ArrayList<>();
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("PASSWORD", "newRoot123456");
            updateData.put("where_ROOT_ID", 1);
            updateList.add(updateData);

            String updateSql = dao.TestInterface_DAO_SQL_UPDATE(updateList);
            System.out.println("test_root更新SQL：" + updateSql);

            // ========== 步骤6：测试DELETE（条件删除：ROOT_ID） ==========
            System.out.println("\n--- 测试DELETE方法 ---");
            Map<String, Object> deleteCondition = new HashMap<>();
            deleteCondition.put("ROOT_ID", 1);
            String deleteSql = dao.TestInterface_DAO_SQL_DELETE(deleteCondition);
            System.out.println("test_root删除ROOT_ID=1的SQL：" + deleteSql);

        } catch (Exception e) {
            System.err.println("test_root表测试异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试test_user表的所有核心方法（is_root默认0）
     * @param dao test_user表的DAO实例
     */
    private static void testUserTable(TestClass_DAO_SQL_MOD dao) {
        try {
            // ========== 步骤1：测试INSERT（不包含自增的USER_ID，is_root默认0） ==========
            System.out.println("\n--- 测试INSERT方法 ---");
            List<Map<String, Object>> insertList = new ArrayList<>();
            Map<String, Object> userData1 = new HashMap<>();
            userData1.put("PASSWORD", "user123456");
            userData1.put("NAME", "普通用户小李");
            userData1.put("PHONE", "11122223333");
            userData1.put("EMAIL", "user1@example.com");
            userData1.put("GENDER", "男");
            insertList.add(userData1);

            Map<String, Object> userData2 = new HashMap<>();
            userData2.put("PASSWORD", "user654321");
            userData2.put("NAME", "普通用户小王%"); // 包含%，测试模糊查询
            userData2.put("PHONE", "11122223333");
            userData2.put("EMAIL", "user2@example.com");
            userData2.put("GENDER", "女");
            insertList.add(userData2);

            String insertSql = dao.TestInterface_DAO_SQL_INSERT(insertList);
            System.out.println("test_user插入SQL：" + insertSql);

            // ========== 步骤2：测试SELECT_1（全表查询） ==========
            System.out.println("\n--- 测试SELECT_1（全表查询） ---");
            List<Map<String, Object>> select1Result = dao.TestInterface_DAO_SQL_SELECT_1();
            System.out.println("test_user全表数据：" + select1Result);

            // ========== 步骤3：测试SELECT_2（条件查询：USER_ID） ==========
            System.out.println("\n--- 测试SELECT_2（USER_ID条件查询） ---");
            Map<String, Object> userCondition = new HashMap<>();
            userCondition.put("USER_ID", 1);
            List<Map<String, Object>> select2Result = dao.TestInterface_DAO_SQL_SELECT_2(userCondition);
            System.out.println("test_user中USER_ID=1的数据：" + select2Result);

            // ========== 步骤4：测试SELECT_3（多条件：NAME包含“小王” + GENDER=女） ==========
            System.out.println("\n--- 测试SELECT_3（多条件+模糊查询） ---");
            List<Map<String, Object>> select3List = new ArrayList<>();
            Map<String, Object> condition1 = new HashMap<>();
            condition1.put("NAME", "%小王%");
            condition1.put("GENDER", "女");
            select3List.add(condition1);

            List<Map<String, Object>> select3Result = dao.TestInterface_DAO_SQL_SELECT_3(select3List);
            System.out.println("test_user多条件查询结果：" + select3Result);

            // ========== 步骤5：测试UPDATE（更新邮箱，条件：USER_ID） ==========
            System.out.println("\n--- 测试UPDATE方法 ---");
            List<Map<String, Object>> updateList = new ArrayList<>();
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("EMAIL", "new_user1@example.com");
            updateData.put("where_USER_ID", 1);
            updateList.add(updateData);

            String updateSql = dao.TestInterface_DAO_SQL_UPDATE(updateList);
            System.out.println("test_user更新SQL：" + updateSql);

            // ========== 步骤6：测试DELETE（条件删除：USER_ID） ==========
            System.out.println("\n--- 测试DELETE方法 ---");
            Map<String, Object> deleteCondition = new HashMap<>();
            deleteCondition.put("USER_ID", 1);
            String deleteSql = dao.TestInterface_DAO_SQL_DELETE(deleteCondition);
            System.out.println("test_user删除USER_ID=1的SQL：" + deleteSql);

        } catch (Exception e) {
            System.err.println("test_user表测试异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试异常场景：空参数、SQL注入、无效条件等（适配所有表）
     */
    private static void testExceptionScenarios() {
        try {
            TestClass_DAO_SQL_MOD dao = new TestClass_DAO_SQL_MOD("test_company");

            // ========== 场景1：INSERT传入空List ==========
            System.out.println("\n--- 测试INSERT传入空List ---");
            try {
                dao.TestInterface_DAO_SQL_INSERT(new ArrayList<>());
            } catch (Exception e) {
                System.out.println("预期异常：" + e.getMessage());
            }

            // ========== 场景2：SELECT_2传入空Map ==========
            System.out.println("\n--- 测试SELECT_2传入空Map ---");
            try {
                dao.TestInterface_DAO_SQL_SELECT_2(new HashMap<>());
            } catch (Exception e) {
                System.out.println("预期异常：" + e.getMessage());
            }

            // ========== 场景3：SELECT_3传入包含SQL注入关键字的条件 ==========
            System.out.println("\n--- 测试SELECT_3传入SQL注入关键字 ---");
            List<Map<String, Object>> injectList = new ArrayList<>();
            Map<String, Object> injectCondition = new HashMap<>();
            injectCondition.put("Company_name", "test' OR 1=1 --"); // SQL注入尝试
            injectList.add(injectCondition);
            try {
                dao.TestInterface_DAO_SQL_SELECT_3(injectList);
            } catch (Exception e) {
                System.out.println("预期异常（SQL注入过滤）：" + e.getMessage());
            }

            // ========== 场景4：SELECT_3传入包含1=1的条件 ==========
            System.out.println("\n--- 测试SELECT_3传入包含1=1的条件 ---");
            List<Map<String, Object>> invalidList = new ArrayList<>();
            Map<String, Object> invalidCondition = new HashMap<>();
            invalidCondition.put("Company_name", "1=1"); // 包含1=1，被过滤
            invalidList.add(invalidCondition);
            try {
                dao.TestInterface_DAO_SQL_SELECT_3(invalidList);
            } catch (Exception e) {
                System.out.println("预期异常（1=1过滤）：" + e.getMessage());
            }

            // ========== 场景5：UPDATE无更新列（只有条件列） ==========
            System.out.println("\n--- 测试UPDATE无更新列 ---");
            List<Map<String, Object>> updateEmptyList = new ArrayList<>();
            Map<String, Object> updateEmptyData = new HashMap<>();
            updateEmptyData.put("where_COMPANY_ID", 1); // 只有条件列，无更新列
            updateEmptyList.add(updateEmptyData);
            try {
                dao.TestInterface_DAO_SQL_UPDATE(updateEmptyList);
            } catch (Exception e) {
                System.out.println("预期异常：" + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("异常场景测试未预期异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
}