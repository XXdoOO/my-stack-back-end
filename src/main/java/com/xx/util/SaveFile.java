package com.xx.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class SaveFile {
    public static boolean saveFile(MultipartFile file, String filename) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String locPath = "D:/fileUpload/";
        File temp = new File(locPath);
        if (!temp.exists()) {
            temp.mkdirs();
        }

        File localFile = new File(locPath + filename);
        try {
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
