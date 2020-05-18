package com.dico.result;

import lombok.Getter;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public class ImageResult {

    @Getter
    private String imageName;

    @Getter
    private String imagePath;

    @Getter
    private String imageSuffix;

    protected ImageResult ImageResult(String imagePath, String imageName, String imageSuffix) {
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.imageSuffix = imageSuffix;
        return this;
    }

}
