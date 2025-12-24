package dao.model;

/**
 * 数据表名称统一封装类
 * 集中管理学生兼职平台（StudentPartTimeJobDB）的所有数据库表名，仅保留get方法用于获取表名
 * 数据库由他人维护，无需set方法，表名与SQL脚本中严格一致
 */
public class TableNameBasis {
    // ==================== 1. 求职申请表（t_apply） ====================
    private String tableNameApply = "t_apply";

    // ==================== 2. 企业信息表（t_company） ====================
    private String tableNameCompany = "t_company";

    // ==================== 3. 兼职评价表（t_evaluate） ====================
    private String tableNameEvaluate = "t_evaluate";

    // ==================== 4. 兼职岗位表（t_job） ====================
    private String tableNameJob = "t_job";

    // ==================== 5. 用户消息表（t_message） ====================
    private String tableNameMessage = "t_message";

    // ==================== 6. 用户简历表（t_resume） ====================
    private String tableNameResume = "t_resume";

    // ==================== 7. 用户信息表（t_user） ====================
    private String tableNameUser = "t_user";

    // ==================== 以下为兼容示例的测试表（可选保留，根据实际需求调整） ====================
    // private String tableNameTestUser = "test_user";

    // ==================== 表名 Get方法（按表名顺序排列） ====================
    public String getTableNameApply() {
        return tableNameApply;
    }

    public String getTableNameCompany() {
        return tableNameCompany;
    }

    public String getTableNameEvaluate() {
        return tableNameEvaluate;
    }

    public String getTableNameJob() {
        return tableNameJob;
    }

    public String getTableNameMessage() {
        return tableNameMessage;
    }

    public String getTableNameResume() {
        return tableNameResume;
    }

    public String getTableNameUser() {
        return tableNameUser;
    }

    // 测试表Get方法（若不需要可删除）
    // public String getTableNameTestUser() {
    //     return tableNameTestUser;
    // }
}