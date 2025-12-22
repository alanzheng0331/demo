package ALL_20252160A0925.D2_CONNECTION;

import ALL_20252160A0925.D2_CONNECTION.MODEL.IMPI_User;
import ALL_20252160A0925.D3_DAO.TestClass_DAO_FOR_User_Login;
import ALL_20252160A0925.D3_DAO.TestClass_DAO_FOR_User_register;
import ALL_20252160A0925.D3_DAO.TestClass_DAO_FOR_User_showDATA;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IMPL_User implements
        IMPI_User
{
    /**
     * 看名字 都懂吧
     */
    TestClass_DAO_FOR_User_Login testClass_DAO_FOR_User_Login=new TestClass_DAO_FOR_User_Login();
    TestClass_DAO_FOR_User_register testClass_dao_for_user_register=new TestClass_DAO_FOR_User_register();
    TestClass_DAO_FOR_User_showDATA testClass_dao_for_user_showDATA=new TestClass_DAO_FOR_User_showDATA();

    @Override
    public List<Map<String, Object>> Login(String PASSWORD, String USERNAME, String PHONE) throws Exception {
        return testClass_DAO_FOR_User_Login.Test_RUNNING(PASSWORD, USERNAME, PHONE);
    }

    @Override
    public String Register(String PASSWORD, String USERNAME, String PHONE) throws Exception {
        return testClass_dao_for_user_register.Test_RUNNING(PASSWORD, USERNAME, PHONE);
    }

    @Override
    public List<Map<String,Object>> Show(String USERNAME) throws Exception {
        return  testClass_dao_for_user_showDATA.Test_RUNNING(USERNAME);
    }
}
