package ALL_20252160A0925.D3_DAO.MODEL;

/**
 * 数据表名称统一封装类
 * 集中管理所有数据库表名，仅保留get方法用于获取表名（数据库由他人维护，无需set方法）
 */
public class Test_TABLE_NAME_MODEL {
    // 对应数据库表：test_company（企业表），默认赋值原始表名
    private String tableNameTestCompany = "test_company";
    // 对应数据库表：test_resume（简历表），默认赋值原始表名
    private String tableNameTestResume = "test_resume";
    // 对应数据库表：test_root（管理员表），默认赋值原始表名
    // 注：原数据库脚本中test_root的is_root字段语法不规范，此处仅封装表名，不涉及字段
    private String tableNameTestRoot = "test_root";
    // 对应数据库表：test_user（普通用户表），默认赋值原始表名
    // 注：原数据库脚本中test_user的is_root=0语法不规范，此处仅封装表名，不涉及字段
    private String tableNameTestUser = "test_user";

    // ==================== test_company 表名 Get方法 ====================
    public String getTableNameTestCompany() {
        return tableNameTestCompany;
    }

    // ==================== test_resume 表名 Get方法 ====================
    public String getTableNameTestResume() {
        return tableNameTestResume;
    }

    // ==================== test_root 表名 Get方法 ====================
    public String getTableNameTestRoot() {
        return tableNameTestRoot;
    }

    // ==================== test_user 表名 Get方法 ====================
    public String getTableNameTestUser() {
        return tableNameTestUser;
    }
}