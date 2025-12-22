package ALL_20252160A0925.D3_DAO;

import ALL_20252160A0925.D3_DAO.MODEL.TestClass_DAO_SQL_MOD;
import ALL_20252160A0925.D3_DAO.MODEL.TestClass_DAO_SQL_PUBLIC;
import ALL_20252160A0925.D3_DAO.MODEL.Test_TABLE_NAME_MODEL;

import java.util.List;
import java.util.Map;

public class TestClass_DAO_FOR_User_register extends TestClass_DAO_SQL_MOD {
    public TestClass_DAO_FOR_User_register() {
        super(
                new Test_TABLE_NAME_MODEL().getTableNameTestUser()
        );
    }

    /**
     *
     *注册功能 这是注册
     * 数据一般不会 输入到控制台 而是传到网页
     * 如果有错误 会抛出异常而不是进行处理
     * 空的数据可以返回
     *
     *
     *
     */
    public String Test_RUNNING(String PASSWORD, String USERNAME, String PHONE) throws Exception
    {
        // 获取数据模板
        List<Map<String, Object>> dataList = TestClass_DAO_SQL_PUBLIC.getTestUserInsertTemplate();
        //System.out.println(dataList);

        // 直接修改第一个Map中的值（索引为0）
        dataList.get(0).put("PASSWORD", PASSWORD);     // 修改USER_ID
        dataList.get(0).put("NAME", USERNAME);       // 修改NAME
        dataList.get(0).put("PHONE", PHONE);         // 修改

        //System.out.println(dataList);

        //直接插入
        return TestInterface_DAO_SQL_INSERT(dataList);
    }
}
