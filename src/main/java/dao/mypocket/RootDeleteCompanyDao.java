package dao.mypocket;

import dao.model.ModelDao;
import dao.model.TableNameBasis;

import java.util.Map;

public class RootDeleteCompanyDao extends ModelDao {

    public RootDeleteCompanyDao() {
        super(
                /*表【来源 模板TableNameBasis】*/
                new TableNameBasis().getTableNameCompany()
        );
    }

    /**
     * 这是关于 管理员登录 的类 方法在TestClass_DAO_SQL_MOD 中
     * 数据一般不会 输入到控制台 而是传到网页
     * 如果有错误 会抛出异常而不是进行处理
     * 空的数据可以返回
     */

    /**
     * 登录要用到的类 查询
     * 输入 User NAME + PASSWORD
     *
     * 返回 对应的值 List<Map<String, Object>>  User NAME +Password
     * 【但是 ROOT 没有表 所以 直接比较 是不是 ROOT】
     */

    //测试 手动输入：
    public String Test_RUNNING(Map<String, Object> MY_WAY) throws Exception {

        //1.传入数据 格式List<Map<String, Object> MY_ID>
        //2.
        //删
        // DELETE/DROP
        /**
         *输入: 用户 ID号->
         *输出：true[成功]/抛出异常【失败】
         *内容：输入-》delete from XXX where XXXid=我的ID ->Util
         */
        //public String TestInterface_DAO_SQL_DELETE(Map<String, Object> MY_ID) throws Exception;

        return TestInterface_DAO_SQL_DELETE(MY_WAY);
    }

}