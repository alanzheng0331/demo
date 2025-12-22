package dao.model;

import java.util.List;
import java.util.Map;

public interface TestInterface_DAO_SQL_PUBLIC {

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
}
