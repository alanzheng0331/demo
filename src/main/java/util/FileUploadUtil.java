package util;


import constant.UserConstants;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传工具类（头像/简历）
 */
public class FileUploadUtil {


    /**
     * 解析上传请求（含文件+普通参数）
     */
    public static Map<String, Object> parseUpload(HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // 设置临时目录
        String tempPath = request.getServletContext().getRealPath("/WEB-INF/temp");
        File tempDir = new File(tempPath);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        factory.setRepository(tempDir);

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(UserConstants.MAX_FILE_SIZE);
        upload.setHeaderEncoding("UTF-8");

        // 解析请求
        List<FileItem> items = upload.parseRequest(request);
        for (FileItem item : items) {
            if (item.isFormField()) {
                // 普通表单字段
                String fieldName = item.getFieldName();
                String value = item.getString("UTF-8");
                result.put(fieldName, value);
            } else {
                // 文件字段
                String fieldName = item.getFieldName();
                String fileName = item.getName();
                if (fileName == null || fileName.isEmpty()) {
                    continue;
                }

                // 验证文件类型
                String ext = getFileExtension(fileName);
                if (!isAllowedType(fieldName, ext)) {
                    throw new IOException("不支持的文件类型：" + ext);
                }

                // 生成唯一文件名
                String newFileName = UUID.randomUUID().toString() + "." + ext;
                // 获取上传目录
                String uploadPath = request.getServletContext().getRealPath("/WEB-INF/upload");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // 保存文件
                File file = new File(uploadDir, newFileName);
                item.write(file);

                // 存储文件信息
                Map<String, String> fileInfo = new HashMap<>();
                fileInfo.put("originalName", fileName);
                fileInfo.put("newName", newFileName);
                fileInfo.put("path", file.getAbsolutePath());
                fileInfo.put("url", "/part-time-job-web/upload/" + newFileName);
                result.put(fieldName, fileInfo);

                // 清理临时文件
                item.delete();
            }
        }
        return result;
    }

    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * 验证文件类型
     */
    private static boolean isAllowedType(String fieldName, String ext) {
        if ("avatar".equals(fieldName)) {
            // 头像验证
            for (String type : UserConstants.ALLOWED_AVATAR_TYPES) {
                if (type.equals(ext)) {
                    return true;
                }
            }
        } else if ("resumeFile".equals(fieldName)) {
            // 简历验证
            for (String type : UserConstants.ALLOWED_RESUME_TYPES) {
                if (type.equals(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }
}
