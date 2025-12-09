package DAO_FOR_MYSQL_20252160A0925;

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
