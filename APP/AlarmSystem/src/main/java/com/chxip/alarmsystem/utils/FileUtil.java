package com.chxip.alarmsystem.utils;

import java.io.File;
import java.util.Random;

/**
 * 文件工具类
 */
public class FileUtil {


    /**
     * 文件保存路径
     */
    private static String uploadFolder;

    /**
     * 文件对外暴露的访问路径
     */
    private static String openPath;

    /**
     * 获取保存文件的根路径
     *
     * @return
     */
    public static String getUploadFolder() {

        return uploadFolder;
    }

    /**
     * 文件对外暴露的访问路径
     */
    public static String getOpenPath() {

        return openPath;
    }

    public static void setUploadFolder(String uploadFolder) {
        FileUtil.uploadFolder = uploadFolder;
    }

    public static void setOpenPath(String openPath) {
        FileUtil.openPath = openPath;
    }


    /**
     * 根据传入的URL获取获取对应的File
     * 如果相关File已经存在会在名称后随机加3位数
     *
     * @param filePath 默认的文件路径
     * @return
     */
    public static File getNewFile(String filePath) {
        String hzm = "";
        if (filePath.endsWith("jpg")) {
            hzm = ".jpg";
            filePath.replace(".jpg", "");
        } else if (filePath.endsWith("png")) {
            hzm = ".png";
            filePath.replace(".png", "");
        }
        //文件存在
        Random rand = new Random();
        int randNumber = rand.nextInt(8999) + 1000;
        filePath = FileUtil.getUploadFolder()+"/" + DateUtil.getNowDateStrOne() + randNumber + hzm;
        File file = new File(filePath);
        return file;
    }
}
