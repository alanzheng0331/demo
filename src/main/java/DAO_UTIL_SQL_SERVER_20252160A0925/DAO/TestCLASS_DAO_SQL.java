package DAO_UTIL_SQL_SERVER_20252160A0925.DAO;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class TestCLASS_DAO_SQL implements TestInterface_DAO_SQL {

    //
    @Override
    public List<Map<String, Object>> TestInterface_DAO_SQL_PUBLIC(String sql) throws Exception {
        //如果你们用的是 sqlserver
        //首先要 下载  sqljdbc_13.2.1.0_chs.zip 的文件 如果是Maven 要下载对应的依赖

        //<dependency>
        //    <groupId>com.microsoft.sqlserver</groupId>
        //    <artifactId>mssql-jdbc</artifactId>
        //    <version>12.4.0.jre11</version>   <!-- 或 12.4.0.jre8 如果你坚持 JDK 8 -->
        //</dependency>

        //然后 把你的用户名 改好 权限打开 没有权限 经不去
        //推荐网站https://blog.csdn.net/dongmuxg/article/details/118963669


        /**
         * 这些 其实因该是在 Util中 写的
         */
        String url =
                "jdbc:sqlserver://localhost:1433;databaseName=TestSQL;"//databaseName=TestSQL 一定要存在
                        + "encrypt=true;trustServerCertificate=true;"; // 开发/内网快速通过
        String user = "sa";          // 1. 换成你的账号
        String pwd  = "Sa123456";      // 2. 换成你的密码

        /* 3. 自动关闭资源写法（try-with-resources） JAVA11+ */
        try (Connection conn = DriverManager.getConnection(url, user, pwd);
             Statement st  = conn.createStatement();
             ResultSet  rs  = st.executeQuery("select top 5 id from TEST_TABLE")) {
            //TEST_TABLE换成你们自己的字段
            while (rs.next()) {
                System.out.printf("id=%d",
                        rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // 这里再报错就是账号/表/网络问题
        }

        /**
         * 当然 有些要返回值 还是要接受
         * List<Map<String, Object>> 【数据表数据】/执行成功【true】/报错ERROR
         */

        //Util 的用法：
        /**
         *  // 获取数据库连接
         *             conn = DBUtil.getConnection();
         *             // 预编译SQL
         *             ps = conn.prepareStatement(sql);
         *             // 执行查询，得到结果集
         *             rs = ps.executeQuery();
         */
        return null;
    }

    /**
     *下面的所有方法 都遵循  组合成SQL语句——>发送给 TestInterface_DAO_SQL_PUBLIC(String sql) ——>有什么数据放回就返回
     */

    //增
    // ADD/insert
    /**
     *输入: List Map->
     *输出：true[成功]/抛出异常【失败】
     *内容：输入-》insert into XXX（Map_key） values Map_value ->Util
     */
    public String TestInterface_DAO_SQL_INSERT(List<Map<String, Object>> list) throws Exception{
        String sql="";
        this.TestInterface_DAO_SQL_PUBLIC(sql);
        return null;
    }

    //删
    // DELETE/DROP
    /**
     *输入: 用户 ID号->
     *输出：true[成功]/抛出异常【失败】
     *内容：输入-》delete from XXX where XXXid=我的ID ->Util
     */
    public String TestInterface_DAO_SQL_DELETE(Map<String, Object> MY_ID) throws Exception{
        String sql="";
        this.TestInterface_DAO_SQL_PUBLIC(sql);
        return null;
    }

    //改
    // UPDATE/CHANGE
    /**
     *输入: List Map->
     *输出：true[成功]/抛出异常【失败】
     *内容：输入-》UPDATE XXX SET MAP_key=MAP_value,………… WHERE  XXXID=MYid  ->Util
     */
    public String TestInterface_DAO_SQL_UPDATE(List<Map<String, Object>> list) throws Exception{
        String sql="";
        this.TestInterface_DAO_SQL_PUBLIC(sql);
        return null;
    }

    //查
    // SELECT
    /**
     * 1.全部查询
     *输入:
     *输出：List Map/抛出异常【失败】
     *内容：输入-》SELECT *FROM XXXXX ->Util ->返回 ——》return
     */
    public List<Map<String,Object>> TestInterface_DAO_SQL_SELECT_1() throws Exception{
        String sql="";
        this.TestInterface_DAO_SQL_PUBLIC(sql);
        return null;
    }
    /**
     * 2.ID条件查询
     *输入: 用户 ID号->
     *输出：List Map/抛出异常【失败】
     *内容：输入-》SELECT *FROM XXXXX where XXXid=我的ID ->Util ->返回 ——》return
     */
    public List<Map<String,Object>> TestInterface_DAO_SQL_SELECT_2(Map<String, Object> MY_ID) throws Exception{
        String sql="";
        this.TestInterface_DAO_SQL_PUBLIC(sql);
        return null;
    }
    /**
     * 3.普通条件查询
     *输入: Map
     *输出：List Map/抛出异常【失败】
     *内容：输入-》SELECT *FROM XXXXX where Map_key=Map_value ->Util ->返回 ——》return
     */
    public List<Map<String,Object>> TestInterface_DAO_SQL_SELECT_3(Map<String, Object> MY_WAY) throws Exception{
        String sql="";
        this.TestInterface_DAO_SQL_PUBLIC(sql);
        return null;
    }
}
