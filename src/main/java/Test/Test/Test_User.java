package ALL_20252160A0925.Test.Test;


import ALL_20252160A0925.D2_CONNECTION.IMPL_User;
import ALL_20252160A0925.D3_DAO.TestClass_DAO_FOR_User_Login;
import ALL_20252160A0925.D3_DAO.TestClass_DAO_FOR_User_register;

public class Test_User {
    public void Test_Login() throws Exception {
        System.out.println( new IMPL_User().Login("root","tttt","55566667777"));;
    }
    public void Test_Register() throws Exception {
        System.out.println(new IMPL_User().Register("root","tttt","55566667777"));;
    }

    public void Test_Show() throws Exception {
        System.out.println(new IMPL_User().Show("tttt"));
    }
}
