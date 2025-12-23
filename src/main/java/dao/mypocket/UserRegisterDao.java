package dao.mypocket;



import dao.model.PublicDao;
import dao.model.ModelDao;
import dao.model.TableNameBasis;

import java.util.List;
import java.util.Map;

public class UserRegisterDao extends ModelDao {
    public UserRegisterDao() {
        super(
                new TableNameBasis().getTableNameUser()
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
        List<Map<String, Object>> dataList =new PublicDao().getUserInsertTemplate();
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
