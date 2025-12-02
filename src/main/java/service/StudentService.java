package service;

import dao.StudentDAO;
import entity.Student;
import util.PasswordUtil;

/**
 * 学生业务逻辑类：处理登录、注册等业务
 */
public class StudentService {
    private StudentDAO studentDAO = new StudentDAO(); // 依赖DAO层

    /**
     * 学生登录
     * @param username 用户名
     * @param password 明文密码
     * @return 登录成功返回学生对象（不含密码）；失败返回null
     */
    public Student login(String username, String password) {
        // 1. 调用DAO查询学生
        Student student = studentDAO.getByUsername(username);
        if (student == null) {
            System.out.println("用户名不存在！");
            return null;
        }

        // 2. 验证密码（输入密码加密后与数据库对比）
        String encryptedPassword = PasswordUtil.encrypt(password);
        if (!encryptedPassword.equals(student.getPassword())) {
            System.out.println("密码错误！");
            return null;
        }

        // 3. 登录成功，清除密码（安全起见，不返回密码）
        student.setPassword(null);
        System.out.println("登录成功！欢迎，" + student.getStudentName() + "同学~");
        return student;
    }

    /**
     * 学生注册
     * @param student 学生对象（密码需为明文）
     * @return true：注册成功；false：注册失败
     */
    public boolean register(Student student) {
        // 1. 检查用户名是否已存在
        if (studentDAO.usernameExists(student.getUsername())) {
            System.out.println("用户名已被占用！");
            return false;
        }

        // 2. 加密密码
        String encryptedPassword = PasswordUtil.encrypt(student.getPassword());
        student.setPassword(encryptedPassword);

        // 3. 调用DAO插入学生
        return studentDAO.addStudent(student);
    }
    /**
     * 更新学生信息
     */
    public boolean updateStudentInfo(Student student) {
        return studentDAO.updateStudent(student);
    }
}