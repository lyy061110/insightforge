package com.insightforge.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传控制器
 * 负责处理图片文件的上传请求，支持Markdown编辑器中的图片上传功能
 */
@RestController
public class ImageUploadController {

    /** 图片上传目录 */
    private static final String UPLOAD_DIR = "uploads/images/";

    /**
     * 处理图片上传请求
     * 支持Markdown编辑器中的图片上传，返回图片URL供前端使用
     * 
     * @param file 上传的图片文件
     * @return 包含上传结果的响应（成功时返回图片URL，失败时返回错误信息）
     */
    @PostMapping(value = "/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        // 校验：文件是否为空
        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "请选择要上传的图片");
            return ResponseEntity.badRequest().body(response);
        }

        // 校验：文件类型是否为图片
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            response.put("success", false);
            response.put("message", "只支持图片文件");
            return ResponseEntity.badRequest().body(response);
        }

        // 校验：文件大小不超过5MB
        if (file.getSize() > 5 * 1024 * 1024) {
            response.put("success", false);
            response.put("message", "图片大小不能超过5MB");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 创建上传目录（如果不存在）
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成唯一文件名：使用时间戳避免文件名冲突
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String filename = "img_" + timestamp + extension;

            // 保存文件到服务器
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // 返回图片访问URL
            String imageUrl = "/images/" + filename;
            response.put("success", true);
            response.put("message", "图片上传成功");
            response.put("url", imageUrl);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            // 上传失败，返回错误信息
            response.put("success", false);
            response.put("message", "图片上传失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
