package dao.model;

/**
 * 数据表名称统一封装类
 * 集中管理所有数据库表名，仅保留get方法用于获取表名（数据库由他人维护，无需set方法）
 */
public class TableNameBasis implements TableNameBasisModel {
    // 对应数据库表：test_user（普通用户表），默认赋值原始表名
    // 注：原数据库脚本中test_user的is_root=0语法不规范，此处仅封装表名，不涉及字段
    private String tableNameTestUser = "test_user";

    // ==================== test_user 表名 Get方法 ====================
    public String getTableNameTestUser() {
        return tableNameTestUser;
    }
}