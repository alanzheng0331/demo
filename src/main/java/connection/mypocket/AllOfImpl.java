package connection.mypocket;

import connection.model.RootImplModel;
import connection.model.UserImplModel;
import dao.mypocket.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AllOfImpl implements
        UserImplModel,
        RootImplModel
{
    /**
     * 看名字 都懂吧
     */
    //User
    UserLoginDao _User_LoginDao =new UserLoginDao();
    UserRegisterDao _user_registerDao =new UserRegisterDao();
    UserShowDao _user_showDao =new UserShowDao();
    //root
    RootLoginDao _Root_LoginDao =new RootLoginDao();
    RootShowDao _Root_ShowDao =new RootShowDao();

    /**
     *User
     */
    @Override
    public List<Map<String, Object>> Login(String PASSWORD, String USERNAME, String PHONE) throws Exception {
        return _User_LoginDao.Test_RUNNING(PASSWORD, USERNAME, PHONE);
    }

    @Override
    public String Register(String PASSWORD, String USERNAME, String PHONE) throws Exception {
        return _user_registerDao.Test_RUNNING(PASSWORD, USERNAME, PHONE);
    }

    @Override
    public List<Map<String,Object>> Show(String USERNAME) throws Exception {
        return  _user_showDao.Test_RUNNING(USERNAME);
    }

    /**
     *ROOT
     */
    @Override
    public List<Map<String, Object>> RootLogin(String PASSWORD, String USERNAME) throws Exception {
        return _Root_LoginDao.Test_RUNNING(PASSWORD, USERNAME);
    }

    @Override
    public List<Map<String, Object>> RootShow() throws Exception {
        return _Root_ShowDao.Test_RUNNING();
    }
}
