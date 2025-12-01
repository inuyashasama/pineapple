package com.pine.pineapple.controller;

import com.pine.pineapple.common.utils.Result;
import com.pine.pineapple.common.utils.UploadUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadActionController {

    @RequestMapping("/image")
    public Result<String> uploadAvatar(MultipartFile file) {
        try {
            long maxSize = 5 * 1024 * 1024; // 5MB
            String avatarUrl = UploadUtil.uploadImage(file, maxSize);
            return Result.ok("上传成功", avatarUrl);
        } catch (Exception e) {
            return Result.fail("上传失败: " + e.getMessage());
        }
    }


}
