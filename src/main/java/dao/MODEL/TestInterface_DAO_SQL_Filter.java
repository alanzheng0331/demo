package dao.model;

import java.util.List;
import java.util.Map;

public interface TestInterface_DAO_SQL_Filter {

    /**
     * 本文章用于过滤 那些 不符合规矩的 数据
     *
     */
    /**
     *输入： List<Map<String,Object>>  的KEY Value  [格式 Map_key="Map_value" , Map_key="Map_value"....]
     *输出：合格的数据/【返回 】
     *      1.没有 where 1=1 或 ‘2’=‘2’
     *      2.不为空
     *      3.key 找的到   需要 Util 但是Util 编写 不归我管
     *      4.不能让 特殊字符占据
     *
     * 内容： 1.是否为空 -> 是否会出现 1=1 或 ‘2’=‘2’ ->不能让 特殊字符占据 ->输出
     */
    public List<Map<String,Object>> TestInterface_DAO_SQL_Filter(List<Map<String,Object>> MY_SELECT) throws Exception;
}
