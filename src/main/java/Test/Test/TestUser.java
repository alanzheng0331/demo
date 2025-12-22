package test.test;


import connection.mypocket.AllOfImpl;

public class TestUser {
    String TestName="root";
    String TestPassword="root";
    String TestPhone="11122223333";
    //(TestName,TestPassword,TestPhone)
    public void Test_Login() throws Exception {
        System.out.println( new AllOfImpl().Login(TestName,TestPassword,TestPhone));;
    }
    public void Test_Register() throws Exception {
        System.out.println(new AllOfImpl().Register(TestName,TestPassword,TestPhone));;
    }

    public void Test_Show() throws Exception {
        System.out.println(new AllOfImpl().Show(TestName));
    }
}
