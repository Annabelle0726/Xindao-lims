package com.ruoyi.common.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author zhuo
 * @Date 2024/9/28
 */
public class DateImageUtil {

    /**
     * 生成 当前日期的画布
     * @return
     */
    public static InputStream createDateImage(LocalDateTime date) {
        int width = 80;
        int height = 20;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        if (date == null) {
            date = LocalDateTime.now();
        }
        String s = date.format(formatter);

        Font font = new Font("Serif", Font.BOLD, 10);
        // 创建一个画布（背景透明）
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // 获取画布的画笔
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        // 开始绘图
        g2.setComposite(AlphaComposite.Src); // 确保绘图时是透明背景
        g2.setBackground(new Color(0, 0, 0, 0)); // 背景色为透明
        g2.clearRect(0, 0, width, height);
        g2.setPaint(new Color(0, 0, 0)); // 设置绘制颜色
        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(s, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;
        g2.drawString(s, (int) x, (int) baseY);
        g2.dispose(); // 释放画笔资源
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(bi, "png", os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(os.toByteArray());
    }
}
