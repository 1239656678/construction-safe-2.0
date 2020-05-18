package com.dico.modules.controller;

import com.dico.qrcode.QRCodeAttribute;
import com.dico.result.ImageResult;
import com.dico.result.ResultData;
import com.dico.util.QRCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: GeneratorERCode
 * 创建时间: 2019/3/28
 */
@RestController
@RequestMapping("qrcode")
@Api(tags = "二维码生成", description = "二维码生成API")
public class GeneratorQRCode {

    @Value("${file-upload.url.qr-code}")
    private String imagePath;

    /**
     * 生成二维码的方法
     *
     * @author Gaodl
     * 方法名称: generatorTextImage
     * 参数： [content, viewMsg, width, height]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/3/28
     */
    @ResponseBody
    @GetMapping("/generatorTextImage")
    @ApiOperation(value = "生成带文字说明的二维码", notes = "生成带文字说明的二维码")
    public ResultData generatorTextImage(@RequestParam(defaultValue = "", value = "content") String content, @RequestParam(defaultValue = "", value = "viewMsg") String viewMsg, @RequestParam(defaultValue = "800", value = "width") int width, @RequestParam(defaultValue = "800", value = "height") int height) {
        QRCodeAttribute qrCodeAttribute = new QRCodeAttribute();
        qrCodeAttribute.setImageWidth(width);
        qrCodeAttribute.setImageHeight(height);
        qrCodeAttribute.setImageViewText(viewMsg);
        qrCodeAttribute.setContent(content);
        qrCodeAttribute.setImagePath(imagePath);
        QRCodeUtils qrCodeUtils = new QRCodeUtils(qrCodeAttribute);
        ImageResult imageResult = qrCodeUtils.ganertorTextImage();
        if (null == imageResult) {
            return ResultData.getFailResult("生成二维码失败");
        }
        return ResultData.getSuccessResult(imageResult);
    }

    /**
     * 生成二维码的方法
     *
     * @author Gaodl
     * 方法名称: generator
     * 参数： [content, viewMsg, width, height]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/3/28
     */
    @ResponseBody
    @GetMapping("/generator")
    @ApiOperation(value = "生成普通二维码", notes = "生成普通二维码")
    public ResultData generator(@RequestParam(defaultValue = "", value = "content") String content, @RequestParam(defaultValue = "800", value = "width") int width, @RequestParam(defaultValue = "800", value = "height") int height) {
        QRCodeAttribute qrCodeAttribute = new QRCodeAttribute();
        qrCodeAttribute.setImageWidth(width);
        qrCodeAttribute.setImageHeight(height);
        qrCodeAttribute.setContent(content);
        qrCodeAttribute.setImagePath(imagePath);
        QRCodeUtils qrCodeUtils = new QRCodeUtils(qrCodeAttribute);
        ImageResult imageResult = qrCodeUtils.ganertorTextImage();
        if (null == imageResult) {
            return ResultData.getFailResult("生成二维码失败");
        }
        return ResultData.getSuccessResult(imageResult);
    }
}
