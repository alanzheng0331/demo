package connection.model;

import java.util.List;
import java.util.Map;

public interface RootImplModel {

    /**
     * 实现的功能 ROOT登录
     * 输入：String PASSWORD, String USERNAME, String PHONE
     * 输出：查询过到的 关于用户的 所有东西
     */
    public List<Map<String,Object>> RootLogin(String PASSWORD, String USERNAME) throws Exception;

    /**
     * 实现的功能 ROOT查询 用户表【一次性搞所有】
     * 输入：
     * 输出：查询过到的 关于用户的 所有东西 Name 其实不许重复 但是 我很懒
     */
    public List<Map<String,Object>> RootShowUser() throws Exception;

    /*分页查询 不一定实现*/

    /**
     * 实现的功能 查询 企业表
     * 输入：
     * 输出：查询过到的 关于用户的 所有东西 Name 其实不许重复 但是 我很懒
     */
    public List<Map<String,Object>> RootShowCompany() throws Exception;

    /*分页查询 不一定实现*/

    /**
     * 实现的功能 删除 表
     * 输入：出
     * 输出：直接删除 List Map 其实是指条件
     */
    public String RootDeleteUser(Map<String, Object> MY_WAY) throws Exception;
    public String RootDeleteCompany(Map<String, Object> MY_WAY) throws Exception;
}
