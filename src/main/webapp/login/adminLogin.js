// 管理员登录表单验证（带跳转功能）
document.getElementById('adminLoginForm').addEventListener('submit', function(e) {
    // 第一步：先阻止表单默认提交行为（避免刷新页面，确保自定义跳转生效）
    e.preventDefault();
    
    const adminAccount = document.getElementById('adminAccount').value.trim();
    const adminPwd = document.getElementById('adminPwd').value.trim();

    // 验证账号和密码是否为空
    if (!adminAccount || !adminPwd) {
        alert('请填写完整的管理员登录信息！');
        return false;
    }

    // 可选：管理员密码长度要求更高（比如至少8位）
    if (adminPwd.length < 8) {
        alert('管理员密码长度至少8位！');
        return false;
    }

    // 所有验证通过，跳转到管理员的企业审核页面
    window.location.href = '../admin/companyReview.html';
});