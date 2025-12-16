package DAO_UTIL_SQL_SERVER_20252160A0925.DAO.FINISHED;

import java.util.List;
import java.util.Map;

public interface TestInterface_DAO_SQL {


    // 数据库的处理-》Dao
    // 数据库的链接-》Util


    //【异常的处理：DAO 层只抛出异常，由上层（Service 层）统一捕获处理，避免异常信息丢失；】
    //【因为为了防止大删改 我们 不用pojo 作为公共数据库模板】

    //如果 需要更新数据 请调用 查询方法

    //数据表+数据库 的确认 【如果确定了名称 请写在这里】
    public String SQL_DATABASE = null;
    public String SQL_TABLE = null;

    //我们数据库链接的理念：

    /**
     * 【【重要】】
     *1. 数据库的链接+sql执行 放到 TestInterface_DAO_SQL_PUBLIC
     * 2.不是TestInterface_DAO_SQL_PUBLIC 的类 只做一件事 拼装 SQL语句
     * 3.查询  尤其是普通条件  拼装是语句查询 的格式 是  :SELECT * FROM XXXXX where Map_key=Map_value,Map_key2=Map_value2………………
     * 4.有查询的语句返回值 一定是 List < Map<String,Object> >  的数据条
     */

    //为了节省代码 所以我把重复率很高的代码 高儒景区
    //把语句评好 ——》PUBLIC ->把数据带回来
    public List<Map<String,Object>> TestInterface_DAO_SQL_PUBLIC(String sql) throws Exception;


    /**
     * 注意 Map_key 的字段 要与 数据库的字段对应上
     */
    //增
    // ADD/insert
    /**
     *输入: List Map->
     *输出：true[成功]/抛出异常【失败】
     *内容：输入-》insert into XXX（Map_key） values Map_value ->Util
     */
    public String TestInterface_DAO_SQL_INSERT(List<Map<String, Object>> list) throws Exception;

    //删
    // DELETE/DROP
    /**
     *输入: 用户 ID号->
     *输出：true[成功]/抛出异常【失败】
     *内容：输入-》delete from XXX where XXXid=我的ID ->Util
     */
    public String TestInterface_DAO_SQL_DELETE(Map<String, Object> MY_ID) throws Exception;

    //改
    // UPDATE/CHANGE
    /**
     *输入: List Map->
     *输出：true[成功]/抛出异常【失败】
     *内容：输入-》UPDATE XXX SET MAP_key=MAP_value,………… WHERE  XXXID=MYid  ->Util
     */
    public String TestInterface_DAO_SQL_UPDATE(List<Map<String, Object>> list) throws Exception;

    //查
    // SELECT
    /**
     * 1.全部查询
     *输入:
     *输出：List Map/抛出异常【失败】
     *内容：输入-》SELECT *FROM XXXXX ->Util ->返回 ——》return
     */
    public List<Map<String,Object>> TestInterface_DAO_SQL_SELECT_1() throws Exception;
    /**
     * 2.ID条件查询
     *输入: 用户 ID号->
     *输出：List Map/抛出异常【失败】
     *内容：输入-》SELECT *FROM XXXXX where XXXid=我的ID ->Util ->返回 ——》return
     */
    public List<Map<String,Object>> TestInterface_DAO_SQL_SELECT_2(Map<String, Object> MY_ID) throws Exception;
    /**
     * 3.普通条件查询
     *输入: Map
     *输出：List Map/抛出异常【失败】
     *内容：输入-》SELECT *FROM XXXXX where Map_key=Map_value ->Util ->返回 ——》return
     */
    public List<Map<String,Object>> TestInterface_DAO_SQL_SELECT_3(Map<String, Object> MY_WAY) throws Exception;
}


/**
 *数据库的参考：
 *
 * CREATE TABLE test_company (
 *   COMPANY_ID int IDENTITY(1,1) NOT NULL,
 *   ROOT_ID int,
 *   Company_name varchar(999),
 *   Company_address varchar(999),
 *   TXT varchar(9999),
 *   PRIMARY KEY (COMPANY_ID)
 * );
 *
 * -- 文档2修改后:
 * CREATE TABLE test_resume (
 *   RESUME_ID int IDENTITY(1,1) NOT NULL,
 *   COMPANY_ID int,
 *   TXT varchar(9999),
 *   PRIMARY KEY (RESUME_ID)
 * );
 *
 * -- 文档3修改后:
 * CREATE TABLE test_root (
 *   ROOT_ID int IDENTITY(1,1) NOT NULL,
 *   NAME varchar(50),
 *   PASSWORD varchar(50),
 *   PHONE int,
 *   EMAIL varchar(333),
 *   GENDER varchar(50),
 *   PRIMARY KEY (ROOT_ID)
 * );
 *
 * -- 文档4修改后:
 * CREATE TABLE test_user (
 *   USER_ID int IDENTITY(1,1) NOT NULL,
 *   PASSWORD varchar(50),
 *   NAME varchar(1000),
 *   PHONE int,
 *   EMAIL varchar(333),
 *   GENDER varchar(50),
 *   PRIMARY KEY (USER_ID)
 * );
 *
 *
 */


/**
 * 暂时的测试类：Util【到时候同学要写的】:
 */
//package DAO_UTIL_SQL_SERVER_20252160A0925.DAO;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
/// **
// * 极简版数据库工具类：仅处理连接、SQL执行、结果集解析（按你的要求，只做核心事情）
// */
//public class TestUtil {
//    // SQL Server连接配置（替换为你的实际配置）
//    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=StudentPartTimeJobDB;encrypt=true;trustServerCertificate=true;";
//    private static final String USER = "sa";
//    private static final String PASSWORD = "123456";
//    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//
//    // 加载驱动（仅执行一次）
//    static {
//        try {
//            Class.forName(DRIVER);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException("驱动加载失败", e);
//        }
//    }
//
//    /**
//     * 执行SQL（查询返回结果集，增删改返回空列表）
//     * @param sql 要执行的SQL语句
//     * @return List<Map>：查询结果（key=列名，value=值）；增删改返回空列表
//     * @throws SQLException SQL执行异常
//     */
//    public static List<Map<String, Object>> executeSql(String sql) throws SQLException {
//        List<Map<String, Object>> result = new ArrayList<>();
//        Connection conn = null;
//        Statement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            // 1. 获取连接（Util的核心工作1）
//            conn = DriverManager.getConnection(URL, USER, PASSWORD);
//            // 2. 创建执行对象（Util的核心工作2）
//            stmt = conn.createStatement();
//
//            // 3. 执行SQL（Util的核心工作3）
//            if (sql.trim().toUpperCase().startsWith("SELECT")) {
//                // 查询：解析结果集
//                rs = stmt.executeQuery(sql);
//                ResultSetMetaData metaData = rs.getMetaData();
//                int columnCount = metaData.getColumnCount();
//                while (rs.next()) {
//                    Map<String, Object> row = new HashMap<>();
//                    for (int i = 1; i <= columnCount; i++) {
//                        row.put(metaData.getColumnName(i), rs.getObject(i));
//                    }
//                    result.add(row);
//                }
//            } else {
//                // 增删改：执行更新
//                stmt.executeUpdate(sql);
//            }
//        } finally {
//            // 4. 关闭资源（Util的核心工作4）
//            if (rs != null) rs.close();
//            if (stmt != null) stmt.close();
//            if (conn != null) conn.close();
//        }
//        return result;
//    }
//}