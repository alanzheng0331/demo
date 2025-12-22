package test.test;

import connection.mypocket.AllOfImpl;
import dao.mypocket.RootLoginDao;

public class TestRoot {
    public void Test_Login() throws Exception {
        System.out.println(new AllOfImpl().RootLogin("root","root"));;
    }

    public void Test_Show() throws Exception {
        System.out.println(new AllOfImpl().RootShow());
    }
}
