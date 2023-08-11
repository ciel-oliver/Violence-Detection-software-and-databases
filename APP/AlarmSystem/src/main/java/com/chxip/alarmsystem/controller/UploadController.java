package com.chxip.alarmsystem.controller;


import com.chxip.alarmsystem.entity.Response;
import com.chxip.alarmsystem.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/upload")
public class UploadController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 上传图片
     * @return Response
     */
    @PostMapping("/uploadImage")
    @ResponseBody
    public Response uploadImage(@RequestPart("file")
                                  MultipartFile multipartFile) {
        //返回的信息
        Response response = new Response();

        if (!multipartFile.isEmpty()) {
            //获取文件名称
            String fileName = multipartFile.getOriginalFilename();
            //本地保存文件的路径
            File file = FileUtil.getNewFile(fileName);
            logger.info("保存文件的路径："+file.getPath());
            try {
                //保存文件
                multipartFile.transferTo(file);
                response.setData(FileUtil.getOpenPath() + file.getName());

            } catch (IOException e) {
                e.printStackTrace();
                response.setErrorId(1002);
                response.setData("上传图片失败");
            }

        } else {
            response.setErrorId(1002);
            response.setData("文件为空");
        }

        return response;
    }

    /**
     * 上传图片
     * @return Response
     */
    @PostMapping("/api/uploadImage")
    @ResponseBody
    public Response uploadImageApi(@RequestPart("file")
                                        MultipartFile multipartFile) {
        //返回的信息
        Response response = new Response();

        if (!multipartFile.isEmpty()) {
            //获取文件名称
            String fileName = multipartFile.getOriginalFilename();
            //本地保存文件的路径
            File file = FileUtil.getNewFile(fileName);
            logger.info("保存文件的路径："+file.getPath());
            try {
                //保存文件
                multipartFile.transferTo(file);
                response.setData(FileUtil.getOpenPath() + file.getName());

            } catch (IOException e) {
                e.printStackTrace();
                response.setErrorId(1002);
                response.setData("上传图片失败");
            }

        } else {
            response.setErrorId(1002);
            response.setData("文件为空");
        }

        return response;
    }
}
