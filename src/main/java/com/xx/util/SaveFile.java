package com.xx.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SaveFile {
    @Value("${file.local-path}")
    private static String localPath;

    public static boolean uploadBlogImages(List<MultipartFile> files, Long blogId) {
        if (files == null || files.isEmpty()) {
            return false;
        }
        boolean result = true;
        for (MultipartFile file : files) {
            File temp = new File(localPath);
            if (!temp.exists()) {
                temp.mkdirs();
            }
            File localFile = new File(localPath + blogId);
            try {
                file.transferTo(localFile);
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
                break;
            }
        }
        return result;
    }

    public static boolean saveFile(MultipartFile file, long blogId) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String filename = "cover/" + blogId + ".jpg";
        File temp = new File(localPath);
        if (!temp.exists()) {
            temp.mkdirs();
        }

        File localFile = new File(localPath + filename);
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean saveAvatar(MultipartFile file, long userId) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String filename = "avatar/user-" + userId + ".jpg";
        File temp = new File(localPath);
        if (!temp.exists()) {
            temp.mkdirs();
        }

        File localFile = new File(localPath + filename);
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
