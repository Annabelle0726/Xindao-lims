package com.ruoyi.common.utils;

import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @Author zhuo
 * @Date 2025/5/7
 */
public class SignatureImageUtil {

    /**
     * 根据中文导出签名图片
     * @param text
     * @return
     * @throws IOException
     * @throws FontFormatException
     */
    public static InputStream saveImageToFile(String text){
        // 获取字体库
        Font font = null;
        try (InputStream is = new ClassPathResource("/ttf/signature.ttf").getInputStream()) {
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(50f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        int width = 100; // 图片的宽度
        int height = 50; // 图片的高度

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setFont(font);

        // 设置文字颜色为黑色
        g2d.setColor(Color.black);

        // 设置文字在图片中的位置
        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(text, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;
        g2d.drawString(text, (int) x, (int) baseY);
        g2d.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ByteArrayInputStream(os.toByteArray());
    }

}
