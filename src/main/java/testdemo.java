

import entity.Student;
import entity.PartTimeJob;
import entity.Application;
import service.StudentService;
import service.JobService;
import service.ApplicationService;
import java.util.List;
import java.util.Scanner;

public class testdemo {
    private static Scanner scanner = new Scanner(System.in);
    private static StudentService studentService = new StudentService();
    private static JobService jobService = new JobService();
    private static ApplicationService applicationService = new ApplicationService();
    private static Student loginStudent = null; // 当前登录用户

    public static void main(String[] args) {
        System.out.println("=== 学生兼职管理系统 ===");
        while (true) {
            if (loginStudent == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    // 登录菜单（未登录状态）
    private static void showLoginMenu() {
        System.out.println("\n1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 退出");
        System.out.print("请选择功能（1-3）：");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 清空输入缓冲区（避免换行符干扰后续输入）

        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegister();
                break;
            case 3:
                System.out.println("谢谢使用，再见！");
                System.exit(0);
                break;
            default:
                System.out.println("无效选择，请重新输入！");
        }
    }

    // 主菜单（已登录状态）
    private static void showMainMenu() {
        System.out.println("\n=== 学生中心 ===");
        System.out.println("1. 查看兼职岗位");
        System.out.println("2. 投递兼职申请");
        System.out.println("3. 查看我的申请记录");
        System.out.println("4. 查看审核结果通知");
        System.out.println("5. 个人中心（修改信息）");
        System.out.println("6. 退出登录");
        System.out.print("请选择功能（1-6）：");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 清空缓冲区

        switch (choice) {
            case 1:
                showJobsList();
                break;
            case 2:
                submitApplication();
                break;
            case 3:
                showMyApplications();
                break;
            case 4:
                showAuditNotifications();
                break;
            case 5:
                showPersonalCenter();
                break;
            case 6:
                loginStudent = null;
                System.out.println("已退出登录！");
                break;
            default:
                System.out.println("无效选择，请重新输入！");
        }
    }

    // 处理登录逻辑
    private static void handleLogin() {
        System.out.println("\n=== 学生登录 ===");
        System.out.print("请输入用户名：");
        String username = scanner.nextLine().trim();
        System.out.print("请输入密码：");
        String password = scanner.nextLine().trim();

        // 调用业务层登录
        loginStudent = studentService.login(username, password);
        if (loginStudent == null) {
            System.out.println("登录失败！用户名或密码错误");
        } else {
            System.out.println("登录成功！欢迎你，" + loginStudent.getStudentName() + "同学~");
        }
    }

    // 处理注册逻辑
    private static void handleRegister() {
        System.out.println("\n=== 学生注册 ===");
        Student newStudent = new Student();

        // 输入并验证用户名（3-20位）
        System.out.print("请设置用户名（3-20位）：");
        String username = scanner.nextLine().trim();
        if (username.length() < 3 || username.length() > 20) {
            System.out.println("用户名长度不符合要求！注册失败");
            return;
        }
        newStudent.setUsername(username);

        // 输入并验证密码（6-16位）
        System.out.print("请设置密码（6-16位）：");
        String password = scanner.nextLine().trim();
        if (password.length() < 6 || password.length() > 16) {
            System.out.println("密码长度不符合要求！注册失败");
            return;
        }
        newStudent.setPassword(password);

        // 输入其他信息
        System.out.print("请输入姓名：");
        newStudent.setStudentName(scanner.nextLine().trim());
        System.out.print("请输入学号：");
        newStudent.setStudentNo(scanner.nextLine().trim());
        System.out.print("请输入专业：");
        newStudent.setMajor(scanner.nextLine().trim());
        System.out.print("请输入年级（如：大三）：");
        newStudent.setGrade(scanner.nextLine().trim());
        System.out.print("请输入手机号：");
        newStudent.setPhone(scanner.nextLine().trim());
        System.out.print("请输入邮箱（选填，直接回车跳过）：");
        newStudent.setEmail(scanner.nextLine().trim());

        // 确认注册
        System.out.print("是否确认注册？（Y/N）：");
        String confirm = scanner.nextLine().trim();
        if (!"Y".equalsIgnoreCase(confirm)) {
            System.out.println("已取消注册");
            return;
        }

        // 调用业务层注册
        boolean registerResult = studentService.register(newStudent);
        if (registerResult) {
            System.out.println("注册成功！请返回登录");
        } else {
            System.out.println("注册失败！用户名已被占用");
        }
    }

    // 查看兼职岗位列表
    private static void showJobsList() {
        System.out.println("\n=== 招募中兼职岗位 ===");
        // 调用业务层获取岗位列表（替换 var 为具体类型 List<PartTimeJob>）
        List<PartTimeJob> jobList = jobService.getAvailableJobs();

        if (jobList.isEmpty()) {
            System.out.println("暂无招募中的兼职岗位");
            return;
        }

        // 遍历岗位列表（替换 var 为具体类型 PartTimeJob）
        for (PartTimeJob job : jobList) {
            System.out.println("\n岗位ID：" + job.getJobId());
            System.out.println("岗位名称：" + job.getJobName());
            System.out.println("雇主名称：" + job.getEmployer());
            System.out.println("工作地点：" + job.getLocation());
            System.out.println("薪资标准：" + job.getSalary() + "元/小时");
            System.out.println("岗位要求：" + job.getRequirements());
            System.out.println("岗位描述：" + job.getDescription());
            System.out.println("招募周期：" + job.getStartDate() + " 至 " + job.getEndDate());
            System.out.println("----------------------------------------");
        }
    }

    // 投递兼职申请
    private static void submitApplication() {
        System.out.println("\n=== 投递兼职申请 ===");
        System.out.print("请输入要投递的岗位ID：");
        int jobId;

        // 验证岗位ID是否为数字
        try {
            jobId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("岗位ID格式错误！必须输入数字");
            return;
        }

        // 调用业务层投递申请
        boolean submitResult = applicationService.submitApplication(loginStudent.getStudentId(), jobId);
        if (submitResult) {
            System.out.println("投递成功！等待雇主审核");
        } else {
            System.out.println("投递失败！岗位不存在/已招满/已投递过");
        }
    }

    // 查看我的申请记录
    private static void showMyApplications() {
        System.out.println("\n=== 我的申请记录 ===");
        // 调用业务层获取申请记录（替换 var 为具体类型 List<Application>）
        List<Application> applicationList = applicationService.getApplicationsByStudentId(loginStudent.getStudentId());

        if (applicationList.isEmpty()) {
            System.out.println("暂无兼职申请记录");
            return;
        }

        // 遍历申请记录（替换 var 为具体类型 Application）
        for (Application app : applicationList) {
            // 根据岗位ID获取岗位名称（辅助显示）
            PartTimeJob job = jobService.getJobById(app.getJobId());
            String jobName = (job != null) ? job.getJobName() : "未知岗位";

            // 转换申请状态为文字描述
            String statusDesc = app.getStatus() == 0 ? "待审核" :
                    app.getStatus() == 1 ? "已录用" : "已拒绝";

            System.out.println("\n申请ID：" + app.getApplicationId());
            System.out.println("岗位名称：" + jobName);
            System.out.println("投递时间：" + app.getApplyTime());
            System.out.println("审核状态：" + statusDesc);
            System.out.println("雇主反馈：" + (app.getFeedback() == null ? "暂无" : app.getFeedback()));
            System.out.println("----------------------------------------");
        }
    }

    // 查看审核结果通知
    private static void showAuditNotifications() {
        System.out.println("\n=== 审核结果通知 ===");
        // 调用业务层获取未读通知（替换 var 为具体类型 List<Application>）
        List<Application> notificationList = applicationService.getUnreadAuditNotifications(loginStudent.getStudentId());

        if (notificationList.isEmpty()) {
            System.out.println("暂无新的审核结果通知");
            return;
        }

        // 遍历通知（替换 var 为具体类型 Application）
        for (Application notification : notificationList) {
            PartTimeJob job = jobService.getJobById(notification.getJobId());
            String jobName = (job != null) ? job.getJobName() : "未知岗位";
            String statusDesc = notification.getStatus() == 1 ? "已录用" : "已拒绝";

            System.out.println("\n【" + statusDesc + "】");
            System.out.println("岗位名称：" + jobName);
            System.out.println("通知时间：" + notification.getApplyTime());
            System.out.println("雇主反馈：" + notification.getFeedback());
            System.out.println("----------------------------------------");
        }

        // 标记通知为已读
        applicationService.markNotificationsAsRead(loginStudent.getStudentId());
    }

    // 个人中心（修改个人信息）
    private static void showPersonalCenter() {
        System.out.println("\n=== 个人中心 ===");
        System.out.println("当前个人信息：");
        System.out.println("姓名：" + loginStudent.getStudentName());
        System.out.println("学号：" + loginStudent.getStudentNo());
        System.out.println("专业：" + loginStudent.getMajor());
        System.out.println("年级：" + loginStudent.getGrade());
        System.out.println("手机号：" + loginStudent.getPhone());
        System.out.println("邮箱：" + (loginStudent.getEmail() == null ? "未填写" : loginStudent.getEmail()));

        System.out.print("\n是否修改个人信息？（Y/N）：");
        String confirm = scanner.nextLine().trim();
        if (!"Y".equalsIgnoreCase(confirm)) {
            System.out.println("已取消修改");
            return;
        }

        // 输入新信息（回车保留原信息）
        System.out.print("请输入新姓名（回车保留原信息）：");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            loginStudent.setStudentName(newName);
        }

        System.out.print("请输入新专业（回车保留原信息）：");
        String newMajor = scanner.nextLine().trim();
        if (!newMajor.isEmpty()) {
            loginStudent.setMajor(newMajor);
        }

        System.out.print("请输入新年级（回车保留原信息）：");
        String newGrade = scanner.nextLine().trim();
        if (!newGrade.isEmpty()) {
            loginStudent.setGrade(newGrade);
        }

        System.out.print("请输入新手机号（回车保留原信息）：");
        String newPhone = scanner.nextLine().trim();
        if (!newPhone.isEmpty()) {
            loginStudent.setPhone(newPhone);
        }

        System.out.print("请输入新邮箱（回车保留原信息）：");
        String newEmail = scanner.nextLine().trim();
        if (!newEmail.isEmpty()) {
            loginStudent.setEmail(newEmail);
        }

        // 调用业务层更新信息
        boolean updateResult = studentService.updateStudentInfo(loginStudent);
        if (updateResult) {
            System.out.println("个人信息修改成功！");
        } else {
            System.out.println("个人信息修改失败！");
        }
    }
}