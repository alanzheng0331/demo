package util;

/**
 * 统一API响应结果类
 */
public class Result {
    private int code; // 响应码：200成功，500失败
    private String msg; // 响应提示信息
    private Object data; // 响应数据（登录成功可传null，后续扩展可传用户/企业信息）

    // 私有构造，通过静态方法创建
    private Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功响应（无数据）
    public static Result success(String msg) {
        return new Result(200, msg, null);
    }

    // 成功响应（带数据，后续扩展用）
    public static Result success(String msg, Object data) {
        return new Result(200, msg, data);
    }

    // 失败响应
    public static Result error(String msg) {
        return new Result(500, msg, null);
    }

    // ------------------- Getter方法（Gson需要反射获取属性，必须提供） -------------------
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}