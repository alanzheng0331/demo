package ALL_20252160A0925.D3_DAO.MODEL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 插入数据的数据源工具类
 * 规则：
 * 1. 移除自增主键字段（数据库自动生成，插入时无需传递）；
 * 2. 仅保留Map的Key（表业务字段名，严格对应数据库）；
 * 3. Value设为空值（数值类型null/字符类型空字符串）；
 * 4. 不使用所有字段（可选部分字段）。
 * DAO层可调用此类方法获取空值数据模板，后续由Servlet赋值后执行INSERT
 */
public class TestClass_DAO_SQL_PUBLIC {

    // ====================== 1. 表：test（ID：主键+自增，移除；业务字段：N1/N2/N3/N4/N5） ======================
    /**
     * 获取test表的空值数据模板（移除自增主键ID，仅保留业务字段）
     * 数据库字段：ID(int, 主键+自增，自动生成)、N1(nchar190)、N2(nchar190)、N3(nchar109)、N4(nchar109)、N5(nchar10)
     * @return test表的空值数据列表
     */
    public static List<Map<String, Object>> getTestInsertTemplate() {
        List<Map<String, Object>> dataList = new ArrayList<>();

        // 第一条数据模板：选N1、N4、N5（演示选部分字段）
        Map<String, Object> data1 = new HashMap<>();
        data1.put("N1", "");    // N1（nchar190）：空字符串
        data1.put("N4", "");    // N4（nchar109）：空字符串
        data1.put("N5", "");    // N5（nchar10）：空字符串
        dataList.add(data1);

        // 第二条数据模板：仅选N2、N3（演示“不使用所有字段”）
        Map<String, Object> data2 = new HashMap<>();
        data2.put("N2", "");    // N2（nchar190）：空字符串
        data2.put("N3", "");    // N3（nchar109）：空字符串
        dataList.add(data2);

        return dataList;
    }

    // ====================== 2. 表：test_company（COMPANY_ID：主键+自增，移除；业务字段：ROOT_ID/Company_name/Company_address/TXT） ======================
    /**
     * 获取test_company表的空值数据模板（移除自增主键COMPANY_ID，仅保留业务字段）
     * 数据库字段：COMPANY_ID(int, 主键+自增，自动生成)、ROOT_ID(int)、Company_name(varchar999)、Company_address(varchar999)、TXT(varchar-1)
     * @return test_company表的空值数据列表
     */
    public static List<Map<String, Object>> getTestCompanyInsertTemplate() {
        List<Map<String, Object>> dataList = new ArrayList<>();

        // 数据模板：包含部分业务字段（ROOT_ID+Company_name+Company_address+TXT）
        Map<String, Object> data1 = new HashMap<>();
        data1.put("ROOT_ID", null);          // ROOT_ID（int）：null
        data1.put("Company_name", "");       // Company_name（varchar999）：空字符串
        data1.put("Company_address", "");    // Company_address（varchar999）：空字符串
        data1.put("TXT", "");                // TXT（varchar-1）：空字符串
        dataList.add(data1);

        return dataList;
    }

    // ====================== 3. 表：test_resume（RESUME_ID：主键+自增，移除；业务字段：COMPANY_ID/TXT） ======================
    /**
     * 获取test_resume表的空值数据模板（移除自增主键RESUME_ID，仅保留业务字段）
     * 数据库字段：RESUME_ID(int, 主键+自增，自动生成)、COMPANY_ID(int)、TXT(varchar-1)
     * @return test_resume表的空值数据列表
     */
    public static List<Map<String, Object>> getTestResumeInsertTemplate() {
        List<Map<String, Object>> dataList = new ArrayList<>();

        Map<String, Object> data1 = new HashMap<>();
        data1.put("COMPANY_ID", null);       // COMPANY_ID（int）：null
        data1.put("TXT", "");                // TXT（varchar-1）：空字符串
        dataList.add(data1);

        return dataList;
    }

    // ====================== 4. 表：test_root（ROOT_ID：主键+自增，移除；业务字段：NAME/PASSWORD/PHONE/EMAIL/GENDER） ======================
    /**
     * 获取test_root表的空值数据模板（移除自增主键ROOT_ID，仅保留业务字段）
     * 数据库字段：ROOT_ID(int, 主键+自增，自动生成)、NAME(varchar50)、PASSWORD(varchar50)、PHONE(varchar20)、EMAIL(varchar333)、GENDER(varchar50)
     * @return test_root表的空值数据列表
     */
    public static List<Map<String, Object>> getTestRootInsertTemplate() {
        List<Map<String, Object>> dataList = new ArrayList<>();

        Map<String, Object> data1 = new HashMap<>();
        data1.put("NAME", "");               // NAME（varchar50）：空字符串
        data1.put("PASSWORD", "");           // PASSWORD（varchar50）：空字符串
        data1.put("PHONE", "");              // PHONE（varchar20）：空字符串
        data1.put("EMAIL", "");              // EMAIL（varchar333）：空字符串
        data1.put("GENDER", "");             // GENDER（varchar50）：空字符串
        dataList.add(data1);

        return dataList;
    }

    // ====================== 5. 表：test_user（USER_ID：主键+自增，移除；业务字段：PASSWORD/NAME/PHONE/EMAIL/GENDER） ======================
    /**
     * 获取test_user表的空值数据模板（移除自增主键USER_ID，仅保留业务字段）
     * 数据库字段：USER_ID(int, 主键+自增，自动生成)、PASSWORD(varchar50)、NAME(varchar1000)、PHONE(varchar20)、EMAIL(varchar333)、GENDER(varchar50)
     * @return test_user表的空值数据列表
     */
    public static List<Map<String, Object>> getTestUserInsertTemplate() {
        List<Map<String, Object>> dataList = new ArrayList<>();

        Map<String, Object> data1 = new HashMap<>();
        data1.put("PASSWORD", "");           // PASSWORD（varchar50）：空字符串
        data1.put("NAME", "");               // NAME（varchar1000）：空字符串
        data1.put("PHONE", "");              // PHONE（varchar20）：空字符串
        data1.put("EMAIL", "");              // EMAIL（varchar333）：空字符串
        data1.put("GENDER", "");             // GENDER（varchar50）：空字符串
        dataList.add(data1);

        return dataList;
    }
}