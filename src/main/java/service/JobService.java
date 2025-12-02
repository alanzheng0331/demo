
package service;  // 修正包名

import entity.Student;  // 修正包路径
import entity.PartTimeJob;
import entity.Application;
import dao.JobDAO;  // 添加必要的DAO依赖
import dao.ApplicationDAO;

import java.util.List;

// 移除错误的Main类定义，实现正确的JobService逻辑
public class JobService {
    private JobDAO jobDAO = new JobDAO();

    /**
     * 获取所有可用岗位
     */
    public List<PartTimeJob> getAvailableJobs() {
        return jobDAO.getAllAvailableJobs();
    }

    /**
     * 根据ID查询岗位
     */
    public PartTimeJob getJobById(Integer jobId) {
        return jobDAO.getJobById(jobId);
    }
}