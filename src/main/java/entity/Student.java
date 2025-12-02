package entity;
/**
 * 学生实体类：对应数据库Student表
 */
public class Student {
    // 字段（与数据库表字段一一对应）
    private Integer studentId;      // 学生ID（自增主键）
    private String username;        // 登录用户名（唯一）
    private String password;        // 加密后的密码
    private String studentName;     // 学生姓名
    private String studentNo;       // 学号（唯一）
    private String major;           // 专业
    private String grade;           // 年级（如：大三）
    private String phone;           // 联系电话（必填）
    private String email;           // 邮箱（选填）

    // Getter和Setter方法（用于访问和修改私有字段）
    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}