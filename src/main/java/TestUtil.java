import util.DBUtil;
import util.PasswordUtil;
import java.sql.Connection;

public class TestUtil {
    public static void main(String[] args) {
        // 测试数据库连接
        System.out.println("=== 测试数据库连接 ===");
        Connection conn = DBUtil.getConnection();
        DBUtil.close(conn, null, null);

        // 测试密码加密
        System.out.println("\n=== 测试密码加密 ===");
        String password = "123456";
        String encrypted = PasswordUtil.encrypt(password);
        System.out.println("明文密码：" + password);
        System.out.println("加密后：" + encrypted); // 应输出固定值（因盐值固定）
    }
}