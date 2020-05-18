package com.dico.util;

import com.dico.qrcode.QRCodeAttribute;
import com.dico.result.ImageResult;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.lang.StringUtils;
import sun.font.FontDesignMetrics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public class QRCodeUtils extends ImageResult {

    private static final int QRCOLOR = 0xFF000000; // 默认是黑色
    private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色

    private QRCodeAttribute qrCodeAttribute;

    /**
     * 重写构造方法
     *
     * @param qrCodeAttribute
     */
    public QRCodeUtils(QRCodeAttribute qrCodeAttribute) {
        this.qrCodeAttribute = qrCodeAttribute;
    }

    /**
     * 生成图片的方法
     *
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public ImageResult generatorImage() throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrCodeAttribute.getContent(), qrCodeAttribute.getBarcodeFormat(), qrCodeAttribute.getImageWidth(), qrCodeAttribute.getImageHeight(), QRCodeAttribute.hints);
        File filePath = new File(qrCodeAttribute.getImagePath());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        Path file = new File(qrCodeAttribute.getImagePath() + File.separator + qrCodeAttribute.getImageName() + "." + qrCodeAttribute.getSuffix()).toPath();
        MatrixToImageWriter.writeToPath(bitMatrix, qrCodeAttribute.getSuffix(), file);
        return super.ImageResult(qrCodeAttribute.getImagePath(), qrCodeAttribute.getImageName(), qrCodeAttribute.getSuffix());
    }

    /**
     * 生成带说明文字的二维码图片
     *
     * @author Gaodl
     * 方法名称: drawLogoQRCode
     * 参数： []
     * 返回值： Map
     * 创建时间: 2019/3/27
     */
    public ImageResult ganertorTextImage() {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix bm = multiFormatWriter.encode(qrCodeAttribute.getContent(), qrCodeAttribute.getBarcodeFormat(), qrCodeAttribute.getImageWidth(), qrCodeAttribute.getImageHeight(), QRCodeAttribute.hints);
            BufferedImage image = new BufferedImage(qrCodeAttribute.getImageWidth(), qrCodeAttribute.getImageHeight(), BufferedImage.TYPE_INT_RGB);
            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
            for (int x = 0; x < qrCodeAttribute.getImageWidth(); x++) {
                for (int y = 0; y < qrCodeAttribute.getImageHeight(); y++) {
                    image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
                }
            }
            String imageViewText = qrCodeAttribute.getImageViewText();
            // 自定义文本描述
            if (StringUtils.isNotEmpty(imageViewText)) {
                // 新的图片，把带logo的二维码下面加上文字
                BufferedImage outImage = new BufferedImage(qrCodeAttribute.getImageWidth(), qrCodeAttribute.getImageHeight() + 40, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D outg = outImage.createGraphics();
                // 画二维码到新的面板
                outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                outg.setBackground(Color.WHITE);
                // 画文字到新的面板
                outg.setColor(Color.BLACK);
                outg.setFont(new Font("楷体", Font.BOLD, 30)); // 字体、字型、字号
                int strWidth = outg.getFontMetrics().stringWidth(imageViewText);
                if (strWidth > qrCodeAttribute.getImageWidth() - 1) {
                    //每一行字符串宽度
                    System.out.println("每行字符宽度:" + qrCodeAttribute.getImageWidth());
                    //获取字符高度
                    int strHeight = getStringHeight(outg);
                    //字符串总个数
                    System.out.println("字符串总个数:" + imageViewText.length());
                    int rowstrnum = getRowStrNum(imageViewText.length(), qrCodeAttribute.getImageWidth(), strWidth);
                    int rows = getRows(strWidth, qrCodeAttribute.getImageWidth());
                    String[] imageViewTexts = new String[rows];
                    for (int i = 0; i < rows; i++) {
                        if (i == rows - 1) {
                            imageViewTexts[i] = imageViewText.substring((i * rowstrnum) - 1);
                            continue;
                        }
                        if (i == 0) {
                            imageViewTexts[i] = imageViewText.substring(i * rowstrnum, (rowstrnum * (i + 1) - 1));
                            continue;
                        }
                        imageViewTexts[i] = imageViewText.substring((i * rowstrnum) - 1, (rowstrnum * (i + 1) - 1));
                    }
                    BufferedImage newOutImage = new BufferedImage(qrCodeAttribute.getImageWidth(), qrCodeAttribute.getImageHeight() + (40 * rows), BufferedImage.TYPE_4BYTE_ABGR);
                    Graphics2D newOutg = newOutImage.createGraphics();
                    // 画二维码到新的面板
                    newOutg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                    newOutg.setBackground(Color.WHITE);
                    // 画文字到新的面板
                    newOutg.setColor(Color.BLACK);
                    newOutg.setFont(new Font("楷体", Font.BOLD, 30)); // 字体、字型、字号
                    for (int i = 0; i < imageViewTexts.length; i++) {
                        newOutg.drawString(imageViewTexts[i], (newOutImage.getWidth() - getStringLength(newOutg, imageViewTexts[i])) / 2, image.getHeight() + strHeight * (i + 1)); // 画文字
                    }
                    newOutg.dispose();
                    newOutImage.flush();
                    outImage = newOutImage;
                } else {
                    outg.drawString(imageViewText, (outImage.getWidth() - strWidth) / 2, image.getHeight() + (outImage.getHeight() - image.getHeight()) / 2 + 12); // 画文字
                }
                outg.dispose();
                outImage.flush();
                image = outImage;
            }
            image.flush();
            File filePath = new File(qrCodeAttribute.getImagePath());
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            String imagePath = qrCodeAttribute.getImagePath() + File.separator + qrCodeAttribute.getImageName() + "." + qrCodeAttribute.getSuffix();
            String imageSuffix = qrCodeAttribute.getSuffix();
            ImageIO.write(image, imageSuffix, new File(imagePath)); // TODO
            return super.ImageResult(imagePath, qrCodeAttribute.getImageName(), imageSuffix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符总宽度
     *
     * @author Gaodl
     * 方法名称: getStringLength
     * 参数： [g, str]
     * 返回值： int
     * 创建时间: 2019/3/27
     */
    private int getStringLength(Graphics g, String str) {
        char[] strcha = str.toCharArray();
        int strWidth = g.getFontMetrics().charsWidth(strcha, 0, str.length());
        System.out.println("字符总宽度:" + strWidth);
        return strWidth;
    }

    /**
     * 计算每行的字符数
     *
     * @author Gaodl
     * 方法名称: getRowStrNum
     * 参数： [strnum, rowWidth, strWidth]
     * 返回值： int
     * 创建时间: 2019/3/27
     */
    private int getRowStrNum(int strnum, int rowWidth, int strWidth) {
        int rowstrnum = 0;
        rowstrnum = (rowWidth * strnum) / strWidth;
        System.out.println("每行的字符数:" + rowstrnum);
        return rowstrnum;
    }

    /**
     * 计算总行数
     *
     * @author Gaodl
     * 方法名称: getRows
     * 参数： [strWidth, rowWidth]
     * 返回值： int
     * 创建时间: 2019/3/27
     */
    private int getRows(int strWidth, int rowWidth) {
        int rows = 0;
        if (strWidth % rowWidth > 0) {
            rows = strWidth / rowWidth + 1;
        } else {
            rows = strWidth / rowWidth;
        }
        System.out.println("行数:" + rows);
        return rows;
    }

    /**
     * 计算字符高度
     *
     * @author Gaodl
     * 方法名称: getStringHeight
     * 参数： [g]
     * 返回值： int
     * 创建时间: 2019/3/27
     */
    private int getStringHeight(Graphics g) {
        int height = g.getFontMetrics().getHeight();
        System.out.println("字符高度:" + height);
        return height;
    }

}
