package com.pine.pineapple.controller;

import com.pine.pineapple.common.utils.Result;
import com.pine.pineapple.common.utils.UploadUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadActionController {

    @RequestMapping("/avatar")
    public Result<?> uploadAvatar(MultipartFile file) {
        try {
            String uploadDir = "uploads/avatars";
            long maxSize = 5 * 1024 * 1024; // 5MB
            String avatarUrl = UploadUtil.uploadImage(file, uploadDir, maxSize);
            return Result.ok("上传成功", avatarUrl);
        } catch (Exception e) {
            return Result.fail("上传失败: " + e.getMessage());
        }
    }


}
