package ALL_20252160A0925.Test;


import ALL_20252160A0925.D3_DAO.TestClass_DAO_FOR_User_Login;
import ALL_20252160A0925.D3_DAO.TestClass_DAO_FOR_User_register;

public class Test_User {
    public void Test_Login() {
        TestClass_DAO_FOR_User_Login n=new TestClass_DAO_FOR_User_Login();
        try{
            System.out.println(n.Test_RUNNING("root","root",""));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void Test_Register() {
        TestClass_DAO_FOR_User_register n=new TestClass_DAO_FOR_User_register();
        try{
            System.out.println(n.Test_RUNNING("rootX","rootX","22233334444"));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
}
