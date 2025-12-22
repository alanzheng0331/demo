package connection.model;

import java.util.List;
import java.util.Map;

public interface UserImplModel {

    /**
     * 实现的功能 用户登录
     * 输入：String PASSWORD, String USERNAME, String PHONE
     * 输出：查询过到的 关于用户的 所有东西
     */
    public List<Map<String,Object>> Login(String PASSWORD, String USERNAME, String PHONE) throws Exception;

    /**
     * 实现的功能 用户注册
     * 输入：String PASSWORD, String USERNAME, String PHONE
     * 输出：没有[暂时不知道是什么鬼]
     */
    public String Register(String PASSWORD, String USERNAME, String PHONE) throws Exception;

    /**
     * 实现的功能 查询
     * 输入：String USERNAME
     * 输出：查询过到的 关于用户的 所有东西 Name 其实不许重复 但是 我很懒
     */
    public List<Map<String,Object>> Show(String USERNAME) throws Exception;


}
