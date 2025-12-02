package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密工具类：使用MD5+盐值加密
 */
public class PasswordUtil {
    // 盐值（固定盐，增强加密强度，实际项目可动态生成）
    private static final String SALT = "student_job_salt_2025";

    /**
     * 加密密码
     * @param password 明文密码
     * @return 加密后的16进制字符串
     */
    public static String encrypt(String password) {
        try {
            // 1. 创建MD5加密器
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 2. 拼接密码和盐值（防止彩虹表破解）
            String input = password + SALT;
            // 3. 对字节数组进行加密
            byte[] digest = md.digest(input.getBytes());
            // 4. 将加密结果转为16进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                // 确保每个字节转换为2位16进制（不足补0）
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 若JVM不支持MD5算法（极少出现），抛出运行时异常
            throw new RuntimeException("❌ 密码加密失败：" + e.getMessage());
        }
    }
}
