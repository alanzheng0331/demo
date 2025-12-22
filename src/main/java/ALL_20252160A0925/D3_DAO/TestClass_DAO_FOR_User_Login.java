package ALL_20252160A0925.D3_DAO;

import ALL_20252160A0925.D3_DAO.MODEL.TestClass_DAO_SQL_MOD;
import ALL_20252160A0925.D3_DAO.MODEL.TestClass_DAO_SQL_PUBLIC;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TestClass_DAO_FOR_User_Login extends TestClass_DAO_SQL_MOD {
    public TestClass_DAO_FOR_User_Login() {
        super("test_user");
    }

    /**
     * 这是关于 普通用户登录 的类 方法在TestClass_DAO_SQL_MOD 中
     * 数据一般不会 输入到控制台 而是传到网页
     * 如果有错误 会抛出异常而不是进行处理
     * 空的数据可以返回
     */

    /**
     * 登录要用到的类 查询
     * 输入 User NAME +Password
     *
     * 返回 对应的值 List<Map<String, Object>>  User NAME +Password
     */

    //测试 手动输入：
    public List<Map<String, Object>> Test_RUNNING(String PASSWORD, String USERNAME, String PHONE) throws Exception {
        // 获取数据模板
        List<Map<String, Object>> dataList = TestClass_DAO_SQL_PUBLIC.getTestUserInsertTemplate();
        //System.out.println(dataList);
        // 直接修改第一个Map中的值（索引为0）
                dataList.get(0).put("PASSWORD", PASSWORD);     // 修改USER_ID
                dataList.get(0).put("NAME", USERNAME);       // 修改NAME
                dataList.get(0).put("PHONE", PHONE); // 修改
        //System.out.println(dataList);
        //直接插入
        return TestInterface_DAO_SQL_SELECT_3(dataList.get(0));
    }

}
