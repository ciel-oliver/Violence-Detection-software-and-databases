package com.chxip.alarmsystem.config;

import com.chxip.alarmsystem.utils.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 文件外部访问路径配置
 */
@Configuration
public class UploadFilePathConfig implements WebMvcConfigurer {

    @Value("${file.openPath}")
    private String openPath;

    @Value("${file.uploadFolder}")
    private String uploadFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        FileUtil.setUploadFolder(uploadFolder);
        FileUtil.setOpenPath(openPath.replace("**", "").replace("/","")+"/");

        registry.addResourceHandler(openPath).addResourceLocations("file:///" + uploadFolder);

        File file = new File(uploadFolder);
        System.out.println("文件夹：");
        System.out.println("文件夹：" + file.getPath());
        System.out.println("图片文件路径：" + "file:///" + uploadFolder);

    }
}
