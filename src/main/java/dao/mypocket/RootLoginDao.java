package dao.mypocket;

import dao.model.PublicDao;
import dao.model.ModelDao;
import dao.model.TableNameBasis;

import java.util.List;
import java.util.Map;

public class RootLoginDao extends ModelDao {

    public RootLoginDao() {
        super(
               "root"
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
    public List<Map<String, Object>> Test_RUNNING(String USERNAME,String PASSWORD) throws Exception {
        if(USERNAME.equals("root") && PASSWORD.equals("root")){
            // 获取数据模板
            List<Map<String, Object>> dataList = PublicDao.getTestUserInsertTemplate();
            // 直接修改第一个Map中的值（索引为0）
            dataList.get(0).put("NAME", USERNAME);       // 修改NAME
            dataList.get(0).put("PASSWORD", USERNAME);       // 修改PASSWORD
            //直接插入
            return dataList;
        }else{
            return null;
        }

    }

}