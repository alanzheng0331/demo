package test;


import test.test.TestModelDao;
import test.test.TestRoot;


/**
 * 测试类：基于用户提供的测试表（test/test_user/test_company）测试核心方法
 */
public class Main {
    public static void main(String[] args) {
        //new TestModelDao().mainX();
        try{

            new TestRoot().Test_Login();
            new TestRoot().Test_Show();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        //gitee 令牌：4abaf9534790728771b50d3a44256aff
        //System.out.println("gitee 密码 :NPC.88=55+tt%tt+j**j.Class");
    }
}