package test.test;

import dao.model.ModelDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试类：匹配test_user表结构
 * 覆盖所有表的INSERT/DELETE/UPDATE/SELECT操作（含模糊查询、异常场景）
 * 注意：SQL Server自增IDENTITY字段（USER_ID）不手动插入，测试数据适配表字段
 */
public class TestModelDao {

    // 初始化ModelDao实例，指定表名为test_user
    private static final ModelDao modelDao = new ModelDao("test_user");

    // 修复：Java程序入口必须是main(String[] args)
    public static void mainX() {
        try {
            // 执行所有测试方法
            System.out.println("===== 开始测试INSERT操作 =====");
            testInsert();

            System.out.println("\n===== 开始测试SELECT1（全表查询）操作 =====");
            testSelect1();

            System.out.println("\n===== 开始测试SELECT2（条件查询）操作 =====");
            testSelect2();

            System.out.println("\n===== 开始测试SELECT3（多条件模糊查询）操作 =====");
            testSelect3();

            System.out.println("\n===== 开始测试UPDATE操作 =====");
            testUpdate();

            System.out.println("\n===== 开始测试DELETE操作 =====");
            testDelete();

            System.out.println("\n===== 开始测试过滤方法（异常数据） =====");
            testFilter();

        } catch (Exception e) {
            System.err.println("测试过程中出现异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试INSERT操作：正常单条/批量插入、异常场景（空列表、非法数据、空Map）
     */
    public static void testInsert() throws Exception {
        // 场景1：正常单条插入（不含USER_ID，自增）
        Map<String, Object> singleData = new HashMap<>();
        singleData.put("PASSWORD", "123456");
        singleData.put("NAME", "张三");
        singleData.put("PHONE", "13800138000");
        singleData.put("EMAIL", "zhangsan@test.com");
        singleData.put("GENDER", "男");
        List<Map<String, Object>> singleList = new ArrayList<>();
        singleList.add(singleData);
        String singleInsertSql = modelDao.TestInterface_DAO_SQL_INSERT(singleList);
        System.out.println("单条插入SQL：" + singleInsertSql);

        // 场景2：正常批量插入
        Map<String, Object> batchData1 = new HashMap<>();
        batchData1.put("PASSWORD", "654321");
        batchData1.put("NAME", "李四");
        batchData1.put("PHONE", "13900139000");
        batchData1.put("EMAIL", "lisi@test.com");
        batchData1.put("GENDER", "女");

        Map<String, Object> batchData2 = new HashMap<>();
        batchData2.put("PASSWORD", "987654");
        batchData2.put("NAME", "王五");
        batchData2.put("PHONE", "13700137000");
        batchData2.put("EMAIL", "wangwu@test.com");
        batchData2.put("GENDER", "男");

        List<Map<String, Object>> batchList = new ArrayList<>();
        batchList.add(batchData1);
        batchList.add(batchData2);
        String batchInsertSql = modelDao.TestInterface_DAO_SQL_INSERT(batchList);
        System.out.println("批量插入SQL：" + batchInsertSql);

        // 场景3：异常-空列表（预期抛出异常）
        try {
            modelDao.TestInterface_DAO_SQL_INSERT(new ArrayList<>());
        } catch (IllegalArgumentException e) {
            System.out.println("空列表插入异常：" + e.getMessage());
        }

        // 场景4：异常-含非法数据（1=1）（预期被过滤，抛出异常）
        Map<String, Object> illegalData1 = new HashMap<>();
        illegalData1.put("PASSWORD", "123456");
        illegalData1.put("NAME", "1=1"); // 包含1=1，非法
        illegalData1.put("PHONE", "13800138000");
        List<Map<String, Object>> illegalList1 = new ArrayList<>();
        illegalList1.add(illegalData1);
        try {
            modelDao.TestInterface_DAO_SQL_INSERT(illegalList1);
        } catch (IllegalArgumentException e) {
            System.out.println("含1=1的非法数据插入异常：" + e.getMessage());
        }

        // 场景5：异常-含SQL注入关键字（drop）（预期被过滤，抛出异常）
        Map<String, Object> illegalData2 = new HashMap<>();
        illegalData2.put("PASSWORD", "123456");
        illegalData2.put("NAME", "张三; drop table test_user;"); // 含注入关键字
        illegalData2.put("PHONE", "13800138000");
        List<Map<String, Object>> illegalList2 = new ArrayList<>();
        illegalList2.add(illegalData2);
        try {
            modelDao.TestInterface_DAO_SQL_INSERT(illegalList2);
        } catch (IllegalArgumentException e) {
            System.out.println("含SQL注入关键字的非法数据插入异常：" + e.getMessage());
        }

        // 场景6：异常-空Map（预期被过滤，抛出异常）
        List<Map<String, Object>> emptyMapList = new ArrayList<>();
        emptyMapList.add(new HashMap<>());
        try {
            modelDao.TestInterface_DAO_SQL_INSERT(emptyMapList);
        } catch (IllegalArgumentException e) {
            System.out.println("空Map插入异常：" + e.getMessage());
        }
    }

    /**
     * 测试SELECT1：全表查询
     */
    public static void testSelect1() throws Exception {
        List<Map<String, Object>> result = modelDao.TestInterface_DAO_SQL_SELECT_1();
        System.out.println("全表查询结果条数：" + result.size());
        // 打印前两条结果（可选）
        for (int i = 0; i < Math.min(result.size(), 2); i++) {
            System.out.println("全表查询结果[" + i + "]：" + result.get(i));
        }
    }

    /**
     * 测试SELECT2：根据条件查询（适配模拟的自增USER_ID）
     */
    public static void testSelect2() throws Exception {
        // 场景1：正常条件查询（模拟USER_ID=1，对应第一条插入数据）
        Map<String, Object> condition = new HashMap<>();
        condition.put("USER_ID", 1);
        condition.put("NAME", "张三");
        List<Map<String, Object>> result = modelDao.TestInterface_DAO_SQL_SELECT_2(condition);
        System.out.println("条件查询结果条数：" + result.size());
        result.forEach(item -> System.out.println("条件查询结果：" + item));

        // 场景2：异常-空条件（预期抛出异常）
        try {
            modelDao.TestInterface_DAO_SQL_SELECT_2(new HashMap<>());
        } catch (IllegalArgumentException e) {
            System.out.println("空条件查询异常：" + e.getMessage());
        }

        // 场景3：异常-含非法数据（2=2）（预期抛出异常）
        Map<String, Object> illegalCondition = new HashMap<>();
        illegalCondition.put("USER_ID", 1);
        illegalCondition.put("NAME", "2=2"); // 包含2=2，非法
        try {
            modelDao.TestInterface_DAO_SQL_SELECT_2(illegalCondition);
        } catch (IllegalArgumentException e) {
            System.out.println("含2=2的非法条件查询异常：" + e.getMessage());
        }
    }

    /**
     * 测试SELECT3：多条件模糊查询（单个Map内AND，多个Map间OR）
     */
    public static void testSelect3() throws Exception {
        // 场景1：正常多条件模糊查询
        // Map1：NAME模糊匹配“张%” AND PHONE等于13800138000（AND连接）
        Map<String, Object> condition1 = new HashMap<>();
        condition1.put("NAME", "张%"); // 模糊查询
        condition1.put("PHONE", "13800138000"); // 精确查询

        // Map2：GENDER等于“男” AND EMAIL模糊匹配“%@test.com”（AND连接）
        Map<String, Object> condition2 = new HashMap<>();
        condition2.put("GENDER", "男");
        condition2.put("EMAIL", "%@test.com"); // 模糊查询

        List<Map<String, Object>> conditionList = new ArrayList<>();
        conditionList.add(condition1);
        conditionList.add(condition2); // 两个Map间OR连接

        List<Map<String, Object>> result = modelDao.TestInterface_DAO_SQL_SELECT_3(conditionList);
        System.out.println("多条件模糊查询结果条数：" + result.size());
        result.forEach(item -> System.out.println("多条件模糊查询结果：" + item));

        // 场景2：异常-空列表（预期抛出异常）
        try {
            modelDao.TestInterface_DAO_SQL_SELECT_3(new ArrayList<>());
        } catch (IllegalArgumentException e) {
            System.out.println("空列表多条件查询异常：" + e.getMessage());
        }

        // 场景3：异常-含SQL注入关键字（union）（预期抛出异常）
        Map<String, Object> illegalCondition = new HashMap<>();
        illegalCondition.put("NAME", "张三' union select * from test_user; --"); // 注入关键字
        List<Map<String, Object>> illegalList = new ArrayList<>();
        illegalList.add(illegalCondition);
        try {
            modelDao.TestInterface_DAO_SQL_SELECT_3(illegalList);
        } catch (IllegalArgumentException e) {
            System.out.println("含SQL注入关键字的多条件查询异常：" + e.getMessage());
        }

        // 场景4：异常-所有条件值为空（预期抛出异常）
        Map<String, Object> emptyValueCondition = new HashMap<>();
        emptyValueCondition.put("NAME", "");
        emptyValueCondition.put("PHONE", null);
        List<Map<String, Object>> emptyValueList = new ArrayList<>();
        emptyValueList.add(emptyValueCondition);
        try {
            modelDao.TestInterface_DAO_SQL_SELECT_3(emptyValueList);
        } catch (IllegalArgumentException e) {
            System.out.println("所有条件值为空的多条件查询异常：" + e.getMessage());
        }
    }

    /**
     * 测试UPDATE操作：正常更新、批量更新、异常场景
     */
    public static void testUpdate() throws Exception {
        // 场景1：正常单条更新（更新NAME，条件为USER_ID=1）
        Map<String, Object> updateData1 = new HashMap<>();
        updateData1.put("NAME", "张三_更新"); // 要更新的列
        updateData1.put("PASSWORD", "新密码123"); // 要更新的列
        updateData1.put("where_USER_ID", 1); // 条件列（where_前缀，对应USER_ID）

        List<Map<String, Object>> updateList1 = new ArrayList<>();
        updateList1.add(updateData1);
        String singleUpdateSql = modelDao.TestInterface_DAO_SQL_UPDATE(updateList1);
        System.out.println("单条更新SQL：" + singleUpdateSql);

        // 场景2：正常批量更新
        Map<String, Object> updateData2 = new HashMap<>();
        updateData2.put("NAME", "李四_更新");
        updateData2.put("where_USER_ID", 2); // 条件：USER_ID=2

        Map<String, Object> updateData3 = new HashMap<>();
        updateData3.put("EMAIL", "wangwu_new@test.com");
        updateData3.put("where_USER_ID", 3); // 条件：USER_ID=3
        updateData3.put("where_NAME", "王五"); // 多条件：NAME=王五

        List<Map<String, Object>> updateList2 = new ArrayList<>();
        updateList2.add(updateData2);
        updateList2.add(updateData3);
        String batchUpdateSql = modelDao.TestInterface_DAO_SQL_UPDATE(updateList2);
        System.out.println("批量更新SQL：" + batchUpdateSql);

        // 场景3：异常-空列表（预期抛出异常）
        try {
            modelDao.TestInterface_DAO_SQL_UPDATE(new ArrayList<>());
        } catch (IllegalArgumentException e) {
            System.out.println("空列表更新异常：" + e.getMessage());
        }

        // 场景4：异常-无更新列（只有where_列）（预期抛出异常）
        Map<String, Object> noSetData = new HashMap<>();
        noSetData.put("where_USER_ID", 1); // 只有条件列，无更新列
        List<Map<String, Object>> noSetList = new ArrayList<>();
        noSetList.add(noSetData);
        try {
            modelDao.TestInterface_DAO_SQL_UPDATE(noSetList);
        } catch (IllegalArgumentException e) {
            System.out.println("无更新列的更新异常：" + e.getMessage());
        }

        // 场景5：异常-无条件列（只有更新列）（预期抛出异常）
        Map<String, Object> noWhereData = new HashMap<>();
        noWhereData.put("NAME", "无条件更新"); // 只有更新列，无where_列
        List<Map<String, Object>> noWhereList = new ArrayList<>();
        noWhereList.add(noWhereData);
        try {
            modelDao.TestInterface_DAO_SQL_UPDATE(noWhereList);
        } catch (IllegalArgumentException e) {
            System.out.println("无条件列的更新异常：" + e.getMessage());
        }

        // 场景6：异常-含非法数据（--注释符）（预期抛出异常）
        Map<String, Object> illegalUpdateData = new HashMap<>();
        illegalUpdateData.put("NAME", "张三--"); // 含SQL注释符，非法
        illegalUpdateData.put("where_USER_ID", 1);
        List<Map<String, Object>> illegalUpdateList = new ArrayList<>();
        illegalUpdateList.add(illegalUpdateData);
        try {
            modelDao.TestInterface_DAO_SQL_UPDATE(illegalUpdateList);
        } catch (IllegalArgumentException e) {
            System.out.println("含非法数据的更新异常：" + e.getMessage());
        }
    }

    /**
     * 测试DELETE操作：正常删除、异常场景
     */
    public static void testDelete() throws Exception {
        // 场景1：正常删除（条件：USER_ID=1）
        Map<String, Object> deleteCondition = new HashMap<>();
        deleteCondition.put("USER_ID", 1);
        String deleteSql = modelDao.TestInterface_DAO_SQL_DELETE(deleteCondition);
        System.out.println("删除SQL：" + deleteSql);

        // 场景2：异常-空条件（预期抛出异常）
        try {
            modelDao.TestInterface_DAO_SQL_DELETE(new HashMap<>());
        } catch (IllegalArgumentException e) {
            System.out.println("空条件删除异常：" + e.getMessage());
        }

        // 场景3：异常-含非法数据（'单引号）（预期抛出异常）
        Map<String, Object> illegalDeleteCondition = new HashMap<>();
        illegalDeleteCondition.put("USER_ID", 1);
        illegalDeleteCondition.put("NAME", "张三'"); // 含单引号，非法
        try {
            modelDao.TestInterface_DAO_SQL_DELETE(illegalDeleteCondition);
        } catch (IllegalArgumentException e) {
            System.out.println("含非法数据的删除异常：" + e.getMessage());
        }
    }

    /**
     * 测试过滤方法：覆盖各种非法数据场景
     */
    public static void testFilter() throws Exception {
        // 构造各种测试数据
        Map<String, Object> normalData = new HashMap<>();
        normalData.put("NAME", "赵六");
        normalData.put("PHONE", "13600136000");

        Map<String, Object> nullKeyData = new HashMap<>();
        nullKeyData.put(null, "赵六"); // 空Key：会被过滤
        nullKeyData.put("PHONE", "13600136000");

        Map<String, Object> emptyKeyData = new HashMap<>();
        emptyKeyData.put("   ", "赵六"); // 空字符串Key：会被过滤
        emptyKeyData.put("PHONE", "13600136000");

        Map<String, Object> oneEqOneData = new HashMap<>();
        oneEqOneData.put("NAME", "1 = 1"); // 包含1=1：会被过滤
        oneEqOneData.put("PHONE", "13600136000");

        Map<String, Object> twoEqTwoData = new HashMap<>();
        twoEqTwoData.put("NAME", "'2'=2"); // 包含'2'=2：会被过滤
        twoEqTwoData.put("PHONE", "13600136000");

        Map<String, Object> sqlInjectData = new HashMap<>();
        sqlInjectData.put("NAME", "张三; exec xp_cmdshell 'dir'"); // 含注入关键字：会被过滤
        sqlInjectData.put("PHONE", "13600136000");

        Map<String, Object> emptyMap = new HashMap<>(); // 空Map：会被过滤
        Map<String, Object> nullValueData = new HashMap<>();
        nullValueData.put("NAME", null); // null值：不会被过滤（转为空字符串，无非法内容）
        nullValueData.put("PHONE", "13600136000");

        // 组装测试列表
        List<Map<String, Object>> testList = new ArrayList<>();
        testList.add(normalData);
        testList.add(nullKeyData);
        testList.add(emptyKeyData);
        testList.add(oneEqOneData);
        testList.add(twoEqTwoData);
        testList.add(sqlInjectData);
        testList.add(emptyMap);
        testList.add(nullValueData);

        // 执行过滤
        List<Map<String, Object>> validList = modelDao.TestInterface_DAO_SQL_Filter(testList);

        // 输出结果
        System.out.println("过滤前数据条数：" + testList.size());
        System.out.println("过滤后有效数据条数：" + validList.size());
        System.out.println("有效数据：" + validList);

        // 异常场景：空列表（预期抛出异常）
        try {
            modelDao.TestInterface_DAO_SQL_Filter(new ArrayList<>());
        } catch (IllegalArgumentException e) {
            System.out.println("空列表过滤异常：" + e.getMessage());
        }

        // 异常场景：null列表（预期抛出异常）
        try {
            modelDao.TestInterface_DAO_SQL_Filter(null);
        } catch (IllegalArgumentException e) {
            System.out.println("null列表过滤异常：" + e.getMessage());
        }
    }
}