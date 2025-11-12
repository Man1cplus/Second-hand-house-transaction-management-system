package org.secondhand.secondhandhousebackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    /**
     * 上传文件
     * @param file 上传的文件
     * @return 返回文件的URI
     */
    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return Result.fail("文件不能为空");
            }

            // 检查文件大小（限制为5MB）
            if (file.getSize() > 5 * 1024 * 1024) {
                return Result.fail("文件大小不能超过5MB");
            }

            // 获取文件原始名称
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return Result.fail("文件名不能为空");
            }

            // 获取文件扩展名
            String extension = "";
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex > 0) {
                extension = originalFilename.substring(lastDotIndex);
            }

            // 生成唯一文件名
            String uniqueFileName = UUID.randomUUID().toString() + extension;

            // 创建上传目录（如果不存在）
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 构建文件URI
            String fileUri = "/files/download/" + uniqueFileName;

            // 返回结果
            Map<String, Object> data = new HashMap<>();
            data.put("uri", fileUri);
            data.put("filename", uniqueFileName);
            data.put("originalFilename", originalFilename);
            data.put("size", file.getSize());

            return Result.ok(data);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传合同文件（仅支持doc、pdf、txt格式）
     * @param file 上传的合同文件
     * @return 返回文件的URI
     */
    @PostMapping("/upload/contract")
    public Result uploadContractFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return Result.fail("文件不能为空");
            }

            // 检查文件大小（限制为10MB，合同文件可能较大）
            if (file.getSize() > 10 * 1024 * 1024) {
                return Result.fail("合同文件大小不能超过10MB");
            }

            // 获取文件原始名称
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return Result.fail("文件名不能为空");
            }

            // 获取文件扩展名
            String extension = "";
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex > 0) {
                extension = originalFilename.substring(lastDotIndex).toLowerCase();
            }

            // 验证文件格式（仅允许doc、docx、pdf、txt）
            if (!extension.equals(".doc") && !extension.equals(".docx") && 
                !extension.equals(".pdf") && !extension.equals(".txt")) {
                return Result.fail("合同文件格式不支持，仅支持doc、docx、pdf、txt格式");
            }

            // 生成唯一文件名
            String uniqueFileName = "contract_" + UUID.randomUUID().toString() + extension;

            // 创建上传目录（如果不存在）
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 构建文件URI
            String fileUri = "/files/download/" + uniqueFileName;

            // 返回结果
            Map<String, Object> data = new HashMap<>();
            data.put("uri", fileUri);
            data.put("filename", uniqueFileName);
            data.put("originalFilename", originalFilename);
            data.put("size", file.getSize());

            return Result.ok(data);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("合同文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 下载文件
     * @param filename 文件名
     * @return 文件资源
     */
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new FileSystemResource(filePath.toFile());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // 确定文件的内容类型
            String contentType = null;
            try {
                contentType = Files.probeContentType(filePath);
            } catch (IOException e) {
                // 如果无法确定内容类型，使用默认值
            }
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

