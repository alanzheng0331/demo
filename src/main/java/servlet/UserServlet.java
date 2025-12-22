package servlet;

import com.google.code.kaptcha.Constants;
import dao.CollectDao;
import dao.ResumeDao;
import dao.UserDao;
import entity.Collect;
import entity.Resume;
import entity.User;
import constant.UserConstants;
import util.FileUploadUtil;
import util.JsonUtil;
import util.MD5Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 用户模块核心控制器
 * 覆盖：注册/登录/个人信息修改/头像上传/密码修改/简历管理/收藏管理
 */
public class UserServlet extends HttpServlet {
    private UserDao userDao = new UserDao();
    private ResumeDao resumeDao = new ResumeDao();
    private CollectDao collectDao = new CollectDao();
    private static final String SALT = "job_platform_2025"; // 密码加密盐值

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String path = request.getPathInfo();

        try {
            switch (path) {
                case "/register": // 注册
                    register(request, out);
                    break;
                case "/login": // 登录
                    login(request, out);
                    break;
                case "/logout": // 退出登录
                    logout(request, out);
                    break;
                case "/updateInfo": // 修改基本信息
                    updateInfo(request, out);
                    break;
                case "/uploadAvatar": // 上传头像
                    uploadAvatar(request, out);
                    break;
                case "/updatePassword": // 修改密码
                    updatePassword(request, out);
                    break;
                case "/updatePhone": // 更换手机号
                    updatePhone(request, out);
                    break;
                case "/addResume": // 创建简历
                    addResume(request, out);
                    break;
                case "/updateResume": // 编辑简历
                    updateResume(request, out);
                    break;
                case "/deleteResume": // 删除简历
                    deleteResume(request, out);
                    break;
                case "/collectJob": // 收藏兼职
                    collectJob(request, out);
                    break;
                case "/cancelCollect": // 取消收藏
                    cancelCollect(request, out);
                    break;
                default:
                    out.write("{\"code\":404,\"msg\":\"接口不存在\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"code\":500,\"msg\":\"服务器异常：" + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String path = request.getPathInfo();

        try {
            switch (path) {
                case "/getUserInfo": // 获取个人信息
                    getUserInfo(request, out);
                    break;
                case "/getResumeList": // 获取简历列表
                    getResumeList(request, out);
                    break;
                case "/getResumeDetail": // 获取简历详情
                    getResumeDetail(request, out);
                    break;
                case "/getCollectList": // 获取收藏列表
                    getCollectList(request, out);
                    break;
                case "/checkCollect": // 检查是否收藏
                    checkCollect(request, out);
                    break;
                default:
                    out.write("{\"code\":404,\"msg\":\"接口不存在\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"code\":500,\"msg\":\"服务器异常\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    // 1. 注册功能
    private void register(HttpServletRequest request, PrintWriter out) throws SQLException {
        // 获取参数
        String role = request.getParameter("role");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPwd = request.getParameter("confirmPwd");
        String code = request.getParameter("code");
        String skillTags = request.getParameter("skillTags");
        String expectedSalary = request.getParameter("expectedSalary");

        // 参数校验
        if (role == null || !UserConstants.ROLE_JOB_SEEKER.equals(role) && !UserConstants.ROLE_ADMIN.equals(role)) {
            out.write("{\"code\":400,\"msg\":\"角色只能是求职者或管理员\"}");
            return;
        }
        if (name == null || name.isEmpty() || phone == null || phone.isEmpty() || password == null || password.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"姓名、手机号、密码不能为空\"}");
            return;
        }
        if (!password.equals(confirmPwd)) {
            out.write("{\"code\":400,\"msg\":\"两次密码输入不一致\"}");
            return;
        }
        if (phone.length() != 11) {
            out.write("{\"code\":400,\"msg\":\"手机号格式错误\"}");
            return;
        }

        // 验证码校验
        HttpSession session = request.getSession();
        String sessionCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (sessionCode == null || !sessionCode.equalsIgnoreCase(code)) {
            out.write("{\"code\":400,\"msg\":\"验证码错误或已过期\"}");
            return;
        }

        // 手机号重复校验
        if (userDao.existsByPhone(phone)) {
            out.write("{\"code\":400,\"msg\":\"该手机号已注册\"}");
            return;
        }

        // 密码加密
        String encryptPwd = MD5Util.md5(password + SALT);

        // 保存用户
        User user = new User();
        user.setRole(role);
        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(encryptPwd);
        user.setSkillTags(skillTags);
        user.setExpectedSalary(expectedSalary);
        user.setStatus(UserConstants.USER_STATUS_NORMAL);

        User User = new User();
        int result = userDao.register(User);
        if (result > 0) {
            session.removeAttribute(Constants.KAPTCHA_SESSION_KEY); // 清空验证码
            out.write("{\"code\":200,\"msg\":\"注册成功，请登录\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"注册失败，请重试\"}");
        }
    }

    // 2. 登录功能
    private void login(HttpServletRequest request, PrintWriter out) throws SQLException {
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        // 参数校验
        if (phone == null || phone.isEmpty() || password == null || password.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"手机号或密码不能为空\"}");
            return;
        }

        // 查询用户
        User user = userDao.findByPhone(phone);
        if (user == null) {
            out.write("{\"code\":400,\"msg\":\"用户不存在\"}");
            return;
        }

        // 校验密码
        String encryptPwd = MD5Util.md5(password + SALT);
        if (!encryptPwd.equals(user.getPassword())) {
            out.write("{\"code\":400,\"msg\":\"密码错误\"}");
            return;
        }

        // 校验用户状态
        if (UserConstants.USER_STATUS_DISABLED == user.getStatus()) {
            out.write("{\"code\":403,\"msg\":\"账号已禁用，请联系管理员\"}");
            return;
        }

        // 登录成功，存入Session
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", user);
        session.setMaxInactiveInterval(86400); // 24小时有效期

        // 返回用户信息（隐藏密码）
        user.setPassword(null);
        out.write("{\"code\":200,\"msg\":\"登录成功\",\"data\":" + JsonUtil.toJson(user) + "}");
    }

    // 3. 退出登录
    private void logout(HttpServletRequest request, PrintWriter out) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("loginUser");
            session.invalidate(); // 销毁Session
        }
        out.write("{\"code\":200,\"msg\":\"退出登录成功\"}");
    }

    // 4. 获取个人信息
    private void getUserInfo(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        // 查询最新用户信息
        User user = userDao.findById(loginUser.getId());
        if (user == null) {
            out.write("{\"code\":400,\"msg\":\"用户不存在\"}");
            return;
        }
        user.setPassword(null); // 隐藏密码
        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":" + JsonUtil.toJson(user) + "}");
    }

    // 5. 修改基本信息
    private void updateInfo(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        // 获取参数
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String skillTags = request.getParameter("skillTags");
        String expectedSalary = request.getParameter("expectedSalary");

        // 参数校验
        if (name == null || name.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"姓名不能为空\"}");
            return;
        }

        // 封装用户信息
        User user = new User();
        user.setId(loginUser.getId());
        user.setName(name);
        user.setEmail(email);
        user.setSkillTags(skillTags);
        user.setExpectedSalary(expectedSalary);

        // 更新信息
        int result = userDao.updateUser(user);
        if (result > 0) {
            // 更新Session中的用户信息
            loginUser.setName(name);
            loginUser.setEmail(email);
            loginUser.setSkillTags(skillTags);
            loginUser.setExpectedSalary(expectedSalary);
            session.setAttribute("loginUser", loginUser);

            out.write("{\"code\":200,\"msg\":\"信息修改成功\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"信息修改失败\"}");
        }
    }

    // 6. 上传头像
    private void uploadAvatar(HttpServletRequest request, PrintWriter out) {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        try {
            // 解析上传文件
            Map<String, Object> uploadResult = FileUploadUtil.parseUpload(request);
            Map<String, String> avatarInfo = (Map<String, String>) uploadResult.get("avatar");
            if (avatarInfo == null) {
                out.write("{\"code\":400,\"msg\":\"请选择要上传的头像文件\"}");
                return;
            }

            // 更新头像
            User user = new User();
            user.setId(loginUser.getId());
            user.setAvatar(avatarInfo.get("url"));
            int result = userDao.updateAvatar(user);

            if (result > 0) {
                // 更新Session中的头像信息
                loginUser.setAvatar(avatarInfo.get("url"));
                session.setAttribute("loginUser", loginUser);

                out.write("{\"code\":200,\"msg\":\"头像上传成功\",\"data\":{\"avatarUrl\":\"" + avatarInfo.get("url") + "\"}}");
            } else {
                // 上传失败，删除文件
                FileUploadUtil.deleteFile(avatarInfo.get("path"));
                out.write("{\"code\":500,\"msg\":\"头像上传失败\"}");
            }
        } catch (Exception e) {
            out.write("{\"code\":500,\"msg\":\"上传失败：" + e.getMessage() + "\"}");
        }
    }

    // 7. 修改密码
    private void updatePassword(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        // 获取参数
        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        String confirmPwd = request.getParameter("confirmPwd");

        // 参数校验
        if (oldPwd == null || oldPwd.isEmpty() || newPwd == null || newPwd.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"原密码和新密码不能为空\"}");
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            out.write("{\"code\":400,\"msg\":\"两次新密码输入不一致\"}");
            return;
        }

        // 校验原密码
        String encryptOldPwd = MD5Util.md5(oldPwd + SALT);
        if (!encryptOldPwd.equals(loginUser.getPassword())) {
            out.write("{\"code\":400,\"msg\":\"原密码错误\"}");
            return;
        }

        // 加密新密码并更新
        String encryptNewPwd = MD5Util.md5(newPwd + SALT);
        int result = userDao.updatePassword(loginUser.getId(), encryptNewPwd);

        if (result > 0) {
            // 更新Session中的密码
            loginUser.setPassword(encryptNewPwd);
            session.setAttribute("loginUser", loginUser);

            out.write("{\"code\":200,\"msg\":\"密码修改成功，请重新登录\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"密码修改失败\"}");
        }
    }

    // 8. 更换手机号
    private void updatePhone(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        // 获取参数
        String newPhone = request.getParameter("newPhone");
        String code = request.getParameter("code");

        // 参数校验
        if (newPhone == null || newPhone.length() != 11) {
            out.write("{\"code\":400,\"msg\":\"手机号格式错误\"}");
            return;
        }
        if (newPhone.equals(loginUser.getPhone())) {
            out.write("{\"code\":400,\"msg\":\"新手机号不能与原手机号相同\"}");
            return;
        }
        if (userDao.existsByPhone(newPhone)) {
            out.write("{\"code\":400,\"msg\":\"新手机号已被注册\"}");
            return;
        }

        // 验证码校验（此处简化，实际需对接短信验证码）
        // TODO: 对接短信验证码接口
        if (code == null || !"123456".equals(code)) { // 测试用固定验证码
            out.write("{\"code\":400,\"msg\":\"验证码错误\"}");
            return;
        }

        // 更新手机号
        int result = userDao.updatePhone(loginUser.getId(), newPhone);
        if (result > 0) {
            // 更新Session中的手机号
            loginUser.setPhone(newPhone);
            session.setAttribute("loginUser", loginUser);

            out.write("{\"code\":200,\"msg\":\"手机号更换成功\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"手机号更换失败\"}");
        }
    }

    // 9. 创建简历
    private void addResume(HttpServletRequest request, PrintWriter out) {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        try {
            // 解析上传请求（含文件+表单参数）
            Map<String, Object> uploadResult = FileUploadUtil.parseUpload(request);

            // 获取表单参数
            String resumeName = (String) uploadResult.get("resumeName");
            String education = (String) uploadResult.get("education");
            String workExperience = (String) uploadResult.get("workExperience");
            String skillTags = (String) uploadResult.get("skillTags");
            String expectedSalary = (String) uploadResult.get("expectedSalary");
            String isDefault = (String) uploadResult.get("isDefault"); // 0/1
            Map<String, String> resumeFile = (Map<String, String>) uploadResult.get("resumeFile");

            // 参数校验
            if (resumeName == null || resumeName.isEmpty()) {
                out.write("{\"code\":400,\"msg\":\"简历名称不能为空\"}");
                return;
            }
            if (resumeFile == null) {
                out.write("{\"code\":400,\"msg\":\"请上传简历文件\"}");
                return;
            }

            // 封装简历信息
            Resume resume = new Resume();
            resume.setUserId(loginUser.getId());
            resume.setResumeName(resumeName);
            resume.setFilePath(resumeFile.get("path"));
            resume.setFileName(resumeFile.get("originalName"));
            resume.setEducation(education);
            resume.setWorkExperience(workExperience);
            resume.setSkillTags(skillTags);
            resume.setExpectedSalary(expectedSalary);
            resume.setIsDefault("1".equals(isDefault) ? UserConstants.RESUME_DEFAULT_YES : UserConstants.RESUME_DEFAULT_NO);

            // 如果设置为默认简历，取消其他简历的默认状态
            if (resume.getIsDefault() == UserConstants.RESUME_DEFAULT_YES) {
                // 先查询用户现有简历
                List<Resume> resumeList = resumeDao.findResumeList(loginUser.getId());
                if (resumeList != null && !resumeList.isEmpty()) {
                    for (Resume r : resumeList) {
                        if (r.getIsDefault() == UserConstants.RESUME_DEFAULT_YES) {
                            resumeDao.cancelDefault(loginUser.getId(), r.getId());
                            break;
                        }
                    }
                }
            }

            // 添加简历
            int result = resumeDao.addResume(resume);
            if (result > 0) {
                out.write("{\"code\":200,\"msg\":\"简历创建成功\"}");
            } else {
                // 创建失败，删除上传的文件
                FileUploadUtil.deleteFile(resumeFile.get("path"));
                out.write("{\"code\":500,\"msg\":\"简历创建失败\"}");
            }
        } catch (Exception e) {
            out.write("{\"code\":500,\"msg\":\"创建失败：" + e.getMessage() + "\"}");
        }
    }

    // 10. 获取简历列表
    private void getResumeList(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        List<Resume> resumeList = resumeDao.findResumeList(loginUser.getId());
        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":" + JsonUtil.toJson(resumeList) + "}");
    }

    // 11. 删除简历
    private void deleteResume(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        String resumeId = request.getParameter("resumeId");
        if (resumeId == null || resumeId.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"简历ID不能为空\"}");
            return;
        }

        // 查询简历信息
        Resume resume = resumeDao.findById(Long.parseLong(resumeId));
        if (resume == null || !resume.getUserId().equals(loginUser.getId())) {
            out.write("{\"code\":400,\"msg\":\"简历不存在或无权限删除\"}");
            return;
        }

        // 删除简历
        int result = resumeDao.deleteResume(Long.parseLong(resumeId), loginUser.getId());
        if (result > 0) {
            // 删除成功，同时删除文件
            FileUploadUtil.deleteFile(resume.getFilePath());
            out.write("{\"code\":200,\"msg\":\"简历删除成功\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"简历删除失败\"}");
        }
    }

    // 12. 收藏兼职
    private void collectJob(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        String jobId = request.getParameter("jobId");
        if (jobId == null || jobId.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"兼职ID不能为空\"}");
            return;
        }

        // 检查是否已收藏
        if (collectDao.isCollected(loginUser.getId(), Long.parseLong(jobId))) {
            out.write("{\"code\":400,\"msg\":\"已收藏该兼职，无需重复收藏\"}");
            return;
        }

        // 添加收藏
        int result = collectDao.addCollect(loginUser.getId(), Long.parseLong(jobId));
        if (result > 0) {
            out.write("{\"code\":200,\"msg\":\"收藏成功\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"收藏失败\"}");
        }
    }

    // 13. 取消收藏
    private void cancelCollect(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        String jobId = request.getParameter("jobId");
        if (jobId == null || jobId.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"兼职ID不能为空\"}");
            return;
        }

        // 检查是否收藏
        if (!collectDao.isCollected(loginUser.getId(), Long.parseLong(jobId))) {
            out.write("{\"code\":400,\"msg\":\"未收藏该兼职，无需取消\"}");
            return;
        }

        // 取消收藏
        int result = collectDao.deleteCollect(loginUser.getId(), Long.parseLong(jobId));
        if (result > 0) {
            out.write("{\"code\":200,\"msg\":\"取消收藏成功\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"取消收藏失败\"}");
        }
    }

    // 14. 获取收藏列表
    private void getCollectList(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        List<Collect> collectList = collectDao.findCollectList(loginUser.getId());
        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":" + JsonUtil.toJson(collectList) + "}");
    }

    // 其他方法（getResumeDetail、checkCollect）逻辑类似，已包含参数校验和结果返回
    private void getResumeDetail(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        String resumeId = request.getParameter("resumeId");
        if (resumeId == null || resumeId.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"简历ID不能为空\"}");
            return;
        }

        Resume resume = resumeDao.findById(Long.parseLong(resumeId));
        if (resume == null || !resume.getUserId().equals(loginUser.getId())) {
            out.write("{\"code\":400,\"msg\":\"简历不存在或无权限查看\"}");
            return;
        }

        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":" + JsonUtil.toJson(resume) + "}");
    }

    private void checkCollect(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        String jobId = request.getParameter("jobId");
        if (jobId == null || jobId.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"兼职ID不能为空\"}");
            return;
        }

        boolean isCollected = collectDao.isCollected(loginUser.getId(), Long.parseLong(jobId));
        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":{\"isCollected\":" + isCollected + "}}");
    }

    private void updateResume(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        String resumeId = request.getParameter("resumeId");
        String resumeName = request.getParameter("resumeName");
        String education = request.getParameter("education");
        String workExperience = request.getParameter("workExperience");
        String skillTags = request.getParameter("skillTags");
        String expectedSalary = request.getParameter("expectedSalary");
        String isDefault = request.getParameter("isDefault");

        if (resumeId == null || resumeId.isEmpty() || resumeName == null || resumeName.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"简历ID和简历名称不能为空\"}");
            return;
        }

        Resume resume = resumeDao.findById(Long.parseLong(resumeId));
        if (resume == null || !resume.getUserId().equals(loginUser.getId())) {
            out.write("{\"code\":400,\"msg\":\"简历不存在或无权限修改\"}");
            return;
        }

        // 更新简历信息
        resume.setResumeName(resumeName);
        resume.setEducation(education);
        resume.setWorkExperience(workExperience);
        resume.setSkillTags(skillTags);
        resume.setExpectedSalary(expectedSalary);
        resume.setIsDefault("1".equals(isDefault) ? UserConstants.RESUME_DEFAULT_YES : UserConstants.RESUME_DEFAULT_NO);

        // 如果设置为默认简历，取消其他简历的默认状态
        if (resume.getIsDefault() == UserConstants.RESUME_DEFAULT_YES) {
            resumeDao.cancelDefault(loginUser.getId(), resume.getId());
        }

        int result = resumeDao.updateResume(resume);
        if (result > 0) {
            out.write("{\"code\":200,\"msg\":\"简历修改成功\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"简历修改失败\"}");
        }
    }
}