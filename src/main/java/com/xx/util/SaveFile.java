package com.xx.util;

import com.xx.config.SystemConfig;
import com.xx.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SaveFile {
    private static final String localPath = SystemConfig.getLocalPath();

    private static final Long userId = SessionUtil.getUser().getId();

    public static String uploadBlogImage(MultipartFile image, Long blogId) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        String filePath = "/" + userId + "/" + blogId + "/" +
                System.currentTimeMillis() + "__" + image.getOriginalFilename();
        File localFile = new File(localPath + filePath);
        try {
            image.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filePath + "?timestamp=" + System.currentTimeMillis();
    }

    public static String saveCover(MultipartFile image, long blogId) {
        if (image == null || image.isEmpty()) {
            return null;
        }

        String filePath = "/" + userId + "/" + blogId + "/cover" + image.getOriginalFilename().
                substring(image.getOriginalFilename().lastIndexOf("."));
        File localFile = new File(localPath + filePath);
        try {
            image.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filePath + "?timestamp=" + System.currentTimeMillis();
    }

    public static String saveAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String filePath = "/" + userId + "/avatar" + file.getOriginalFilename().
                substring(file.getOriginalFilename().lastIndexOf("."));
        File localFile = new File(localPath + filePath);
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filePath + "?timestamp=" + System.currentTimeMillis();
    }
}
