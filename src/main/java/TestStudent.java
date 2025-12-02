import entity.Student;
import service.StudentService;
import util.DBUtil;
import java.sql.Connection;
import java.util.Scanner;

public class TestStudent {
    public static void main(String[] args) {
        // 1. 先验证数据库连接是否正常
        System.out.println("=== 数据库连接测试 ===");
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            System.out.println("❌ 数据库连接失败，请检查配置后重试！");
            return; // 连接失败直接退出
        }
        DBUtil.close(conn, null, null);
        System.out.println("✅ 数据库连接正常\n");

        StudentService studentService = new StudentService();
        Scanner scanner = new Scanner(System.in);

        // 2. 测试注册功能
        System.out.println("=== 测试注册功能 ===");
        Student newStudent = new Student();

        // 读取用户输入（带验证）
        System.out.print("请设置用户名：");
        String username = scanner.nextLine().trim();
        if (username.isEmpty() || username.length() < 3) {
            System.out.println("❌ 用户名长度不能少于3位！");
            return;
        }
        newStudent.setUsername(username);

        System.out.print("请设置密码：");
        String password = scanner.nextLine().trim();
        if (password.length() < 6) {
            System.out.println("❌ 密码长度不能少于6位！");
            return;
        }
        newStudent.setPassword(password); // 明文密码，业务层会加密

        System.out.print("请输入姓名：");
        newStudent.setStudentName(scanner.nextLine().trim());
        System.out.print("请输入学号：");
        newStudent.setStudentNo(scanner.nextLine().trim());
        System.out.print("请输入专业：");
        newStudent.setMajor(scanner.nextLine().trim());
        System.out.print("请输入年级：");
        newStudent.setGrade(scanner.nextLine().trim());
        System.out.print("请输入手机号：");
        newStudent.setPhone(scanner.nextLine().trim());
        System.out.print("请输入邮箱（选填）：");
        newStudent.setEmail(scanner.nextLine().trim());

        // 执行注册
        boolean registerResult = studentService.register(newStudent);
        System.out.println("注册结果：" + (registerResult ? "✅ 成功" : "❌ 失败"));

        // 3. 测试登录功能
        System.out.println("\n=== 测试登录功能 ===");
        System.out.print("请输入用户名：");
        String loginUsername = scanner.nextLine().trim();
        System.out.print("请输入密码：");
        String loginPassword = scanner.nextLine().trim();

        Student loginStudent = studentService.login(loginUsername, loginPassword);
        if (loginStudent != null) {
            System.out.println("✅ 登录成功！");
            System.out.println("用户信息：");
            System.out.println("姓名：" + loginStudent.getStudentName());
            System.out.println("学号：" + loginStudent.getStudentNo());
            System.out.println("专业：" + loginStudent.getMajor());
            System.out.println("年级：" + loginStudent.getGrade());
            System.out.println("手机号：" + loginStudent.getPhone());
        } else {
            System.out.println("❌ 登录失败！");
        }

        scanner.close();
    }
}