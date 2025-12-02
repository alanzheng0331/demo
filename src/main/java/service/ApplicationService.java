package service;

import dao.ApplicationDAO;
import entity.Application;
import java.util.List;

public class ApplicationService {
    private ApplicationDAO applicationDAO = new ApplicationDAO();

    /**
     * 提交兼职申请
     */
    public boolean submitApplication(Integer studentId, Integer jobId) {
        Application application = new Application();
        application.setStudentId(studentId);
        application.setJobId(jobId);
        return applicationDAO.addApplication(application);
    }

    /**
     * 查询学生的申请记录
     */
    public List<Application> getApplicationsByStudentId(Integer studentId) {
        return applicationDAO.getApplicationsByStudentId(studentId);
    }

    public void markNotificationsAsRead(Integer studentId) {
    }

    public List<Application> getUnreadAuditNotifications(Integer studentId) {

        return java.util.Collections.emptyList();
    }

    // 其他需要的方法...
}