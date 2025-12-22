package ALL_20252160A0925.D2_CONNECTION.MODEL;

import java.util.List;
import java.util.Map;

public interface IMPI_User {

    /**
     * 实现的功能 用户登录
     * 输入：String PASSWORD, String USERNAME, String PHONE
     * 输出：查询过到的 关于用户的 所有东西
     */
    public List<Map<String,Object>> Login(String PASSWORD, String USERNAME, String PHONE) throws Exception;

    /**
     * 实现的功能 用户注册
     * 输入：String PASSWORD, String USERNAME, String PHONE
     * 输出：没有
     */
    public String Register(String PASSWORD, String USERNAME, String PHONE) throws Exception;
}
