package com.pine.pineapple.common.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传工具类
 */
public class UploadUtil {

    /**
     * 上传文件
     * @param file 上传的文件
     * @param uploadDir 上传目录
     * @param allowedTypes 允许的文件类型数组
     * @param maxSize 最大文件大小（字节）
     * @return 文件访问路径
     * @throws IOException IO异常
     * @throws IllegalArgumentException 参数异常
     */
    public static String uploadFile(MultipartFile file, String uploadDir,
                                    String[] allowedTypes, long maxSize) throws IOException {
        // 参数校验
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        if (uploadDir == null || uploadDir.trim().isEmpty()) {
            throw new IllegalArgumentException("上传目录不能为空");
        }

        // 文件类型校验
        String contentType = file.getContentType();
        if (!isAllowedType(contentType, allowedTypes)) {
            throw new IllegalArgumentException("不支持的文件类型: " + contentType);
        }

        // 文件大小校验
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("文件大小超出限制: " + maxSize + " 字节");
        }

        // 创建上传目录
        Path dirPath = Paths.get(uploadDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID() + fileExtension;

        // 保存文件
        Path filePath = dirPath.resolve(uniqueFilename);
        Files.write(filePath, file.getBytes());

        return "/uploads/" + uniqueFilename;
    }

    /**
     * 上传图片文件（专用方法）
     * @param file 图片文件
     * @param uploadDir 上传目录
     * @param maxSize 最大文件大小
     * @return 文件访问路径
     * @throws IOException IO异常
     */
    public static String uploadImage(MultipartFile file, String uploadDir, long maxSize) throws IOException {
        String[] imageTypes = {"image/jpeg", "image/png", "image/jpg", "image/gif"};
        return uploadFile(file, uploadDir, imageTypes, maxSize);
    }

    /**
     * 检查文件类型是否被允许
     * @param contentType 文件内容类型
     * @param allowedTypes 允许的类型数组
     * @return 是否允许
     */
    private static boolean isAllowedType(String contentType, String[] allowedTypes) {
        for (String type : allowedTypes) {
            if (type.equalsIgnoreCase(contentType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件扩展名
     * @param filename 文件名
     * @return 文件扩展名
     */
    private static String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
