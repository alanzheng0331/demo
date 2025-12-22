function showUserLogin() {
    document.getElementById('userLoginForm').classList.remove('hidden');
    document.getElementById('adminLoginForm').classList.add('hidden');

    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelectorAll('.tab-btn')[0].classList.add('active');
}

function showAdminLogin() {
    document.getElementById('userLoginForm').classList.add('hidden');
    document.getElementById('adminLoginForm').classList.remove('hidden');

    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelectorAll('.tab-btn')[1].classList.add('active');
}

// 个人用户登录表单 - 带跳转
document.getElementById('userLoginForm').addEventListener('submit', function(e) {
    // 第一步：先阻止表单默认提交行为（关键）
    e.preventDefault();
    
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!username || !password) {
        alert('请填写完整的登录信息！');
        return false;
    }

    if (password.length < 6) {
        alert('密码长度至少6位！');
        return false;
    }

    // 验证通过：跳转到个人用户首页
    window.location.href = '../user/index.html';
});

// 企业（admin）登录表单 - 带跳转
document.getElementById('adminLoginForm').addEventListener('submit', function(e) {
    // 第一步：先阻止表单默认提交行为（关键）
    e.preventDefault();
    
    const username = document.getElementById('adminUsername').value.trim();
    const password = document.getElementById('adminPassword').value.trim();
    
    if (!username || !password) {
        alert('请填写完整的企业登录信息！');
        return false;
    }

    // 验证通过：跳转到企业首页
    window.location.href = '../company/companyIndex.html';
});