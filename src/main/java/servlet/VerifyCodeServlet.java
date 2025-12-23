package servlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 验证码生成Servlet
 * 对应页面中的 verifyCodeServlet 路径
 */

public class VerifyCodeServlet extends HttpServlet {
    // 验证码图片的宽度
    private static final int WIDTH = 120;
    // 验证码图片的高度
    private static final int HEIGHT = 52;
    // 验证码字符个数
    private static final int CODE_COUNT = 4;
    // 干扰线数量
    private static final int LINE_COUNT = 5;
    // 随机数对象
    private static final Random RANDOM = new Random();

    // 验证码字符集（去掉易混淆的字符：0/O、1/I/l）
    private static final String CODE_CHARSET = "23456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 创建内存图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 2. 获取图片画笔
        Graphics g = image.getGraphics();

        // 3. 绘制图片背景
        drawBackground(g);
        // 4. 绘制验证码文本（返回生成的验证码字符串）
        String verifyCode = drawCodeText(g);
        // 5. 绘制干扰线
        drawInterferenceLine(g);
        // 6. 释放画笔资源
        g.dispose();

        // 7. 将验证码存入Session（供后续注册验证使用）
        HttpSession session = request.getSession();
        // 注意：存的时候建议统一大小写，方便后续验证
        session.setAttribute("verifyCode", verifyCode.toLowerCase());

        // 8. 设置响应头：禁止缓存（防止验证码图片被缓存）
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 9. 设置响应内容类型：图片/jpeg
        response.setContentType("image/jpeg");

        // 10. 将图片输出到浏览器
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // POST请求直接调用GET方法
        doGet(request, response);
    }

    /**
     * 绘制图片背景
     */
    private void drawBackground(Graphics g) {
        // 设置背景色（浅灰色）
        g.setColor(Color.WHITE);
        // 填充整个图片区域
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // 绘制图片边框（可选）
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
    }

    /**
     * 绘制验证码文本，并返回生成的验证码字符串
     */
    private String drawCodeText(Graphics g) {
        // 设置字体：加粗、宋体、字号20
        g.setFont(new Font("宋体", Font.BOLD, 20));
        // 存储生成的验证码
        StringBuilder codeBuilder = new StringBuilder();
        // 每个字符的宽度（平均分配）
        int charWidth = WIDTH / (CODE_COUNT + 1);

        for (int i = 0; i < CODE_COUNT; i++) {
            // 随机获取字符集索引
            int index = RANDOM.nextInt(CODE_CHARSET.length());
            // 获取字符
            char codeChar = CODE_CHARSET.charAt(index);
            // 添加到验证码字符串
            codeBuilder.append(codeChar);

            // 随机设置字符颜色（深色调，提高辨识度）
            g.setColor(new Color(RANDOM.nextInt(80), RANDOM.nextInt(80), RANDOM.nextInt(80)));
            // 随机设置字符偏移量（防止字符整齐排列，增加识别难度）
            int y = HEIGHT - RANDOM.nextInt(10);
            // 绘制字符
            g.drawString(String.valueOf(codeChar), (i + 1) * charWidth - RANDOM.nextInt(5), y);
        }
        return codeBuilder.toString();
    }

    /**
     * 绘制干扰线
     */
    private void drawInterferenceLine(Graphics g) {
        for (int i = 0; i < LINE_COUNT; i++) {
            // 随机设置干扰线颜色（浅灰色）
            g.setColor(new Color(RANDOM.nextInt(150), RANDOM.nextInt(150), RANDOM.nextInt(150)));
            // 随机获取起点和终点坐标
            int x1 = RANDOM.nextInt(WIDTH);
            int y1 = RANDOM.nextInt(HEIGHT);
            int x2 = RANDOM.nextInt(WIDTH);
            int y2 = RANDOM.nextInt(HEIGHT);
            // 绘制直线
            g.drawLine(x1, y1, x2, y2);
        }

        // 可选：添加干扰点
        for (int i = 0; i < 20; i++) {
            g.setColor(new Color(RANDOM.nextInt(200), RANDOM.nextInt(200), RANDOM.nextInt(200)));
            g.fillOval(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT), 2, 2);
        }
    }
}