package com.dico.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: QRCodeAttribute
 * 创建时间: $date$
 */
@NoArgsConstructor
public class QRCodeAttribute {

    public static HashMap hints = new HashMap();

    static {
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
    }

    /**
     * 图片名称
     */
    @Setter
    @Getter
    private String imageName = UUID.randomUUID().toString();

    /**
     * 图片展示文本
     */
    @Setter
    @Getter
    private String imageViewText = "";

    /**
     * 图片保存路径
     */
    @Setter
    @Getter
    private String imagePath = "";

    /**
     * 图片内容
     */
    @Setter
    @Getter
    private String content = "";

    /**
     * 图片类型
     */
    @Setter
    @Getter
    private BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;

    @Setter
    @Getter
    private String suffix = "png";

    /**
     * 图片高度
     */
    @Setter
    @Getter
    private int imageWidth = 100;

    /**
     * 图片高度
     */
    @Setter
    @Getter
    private int imageHeight = 100;

    @Override
    public String toString() {
        return "UUID--->" + this.getImageName() + ",PATH--->" + this.getImagePath() + ",CONTENT--->" + this.getContent() + ",VIEWTEXT--->" + this.getImageViewText();
    }
}
