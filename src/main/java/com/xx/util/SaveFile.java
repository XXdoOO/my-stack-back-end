package com.xx.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class SaveFile {
    public static boolean saveFile(MultipartFile file, String locPath, String filename) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        java.io.File temp = new java.io.File(locPath);
        if (!temp.exists()) {
            temp.mkdirs();
        }

        java.io.File localFile = new java.io.File(locPath + filename);
        try {
            //把上传的文件保存至本地
            file.transferTo(localFile);

            System.out.println(locPath + filename);
            System.out.println(file.getOriginalFilename() + " 上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
