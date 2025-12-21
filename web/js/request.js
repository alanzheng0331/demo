/**
 * 通用请求工具：处理所有前后端接口交互
 * @param {string} url - 接口路径（如：/api/user/login）
 * @param {string} method - 请求方法（GET/POST/PUT/DELETE）
 * @param {object} data - 请求参数（POST/PUT时传递）
 * @returns {Promise} - 后端返回的数据
 */
async function request(url, method = 'GET', data = {}) {
    // 基础配置
    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json', // 告诉后端数据格式为JSON
        },
    };

    // 携带登录令牌（登录后存储在localStorage）
    const token = localStorage.getItem('token');
    if (token) {
        options.headers.Authorization = `Bearer ${token}`; // 令牌格式需与后端一致
    }

    // POST/PUT请求需要携带数据（GET请求通过URL参数传递）
    if (method !== 'GET') {
        options.body = JSON.stringify(data);
    }

    try {
        // 发送请求（注意：实际项目需替换为后端服务器地址，如：http://localhost:8080 + url）
        const baseUrl = ''; // 本地调试可留空，部署时填写后端域名
        const response = await fetch(baseUrl + url, options);
        const result = await response.json(); // 解析后端返回的JSON

        // 处理HTTP错误状态（如404、500）
        if (!response.ok) {
            // 401：未登录或令牌失效，强制跳转登录页
            if (response.status === 401) {
                alert('登录已过期，请重新登录');
                localStorage.removeItem('token'); // 清除无效令牌
                window.location.href = 'login.html';
                return;
            }
            // 其他错误（如参数错误）
            throw new Error(result.message || `请求失败（状态码：${response.status}）`);
        }

        // 处理后端自定义错误（如业务逻辑错误）
        if (!result.success) {
            throw new Error(result.message || '操作失败');
        }

        return result.data; // 返回接口数据（假设后端格式为 {success: true, data: ...}）

    } catch (error) {
        // 网络错误或解析错误（如断网、后端返回非JSON格式）
        alert(`操作失败：${error.message}`);
        throw error; // 允许页面进一步处理
    }
}