package org.superdata.medismart.utils.file;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.superdata.medismart.common.exception.FileException;
import org.superdata.medismart.config.MediSmartConfig;
import org.superdata.medismart.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUtils {
    static int DEFAULT_FILE_NAME_LENGTH = 100;

    static int DEFAULT_MAX_SIZE = 5 * 1024 * 1024;

    static String DEFAULT_BASE_DIR = MediSmartConfig.PROFILE;

    static String RESOURCE_PREFIX = "/profile";

    public static String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws FileException, IOException {
        // 1. 检查文件名长度
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > DEFAULT_FILE_NAME_LENGTH)
        {
            throw new FileException("File name length limit exceed, max is " + DEFAULT_FILE_NAME_LENGTH);
        }
        // 2. 检查文件格式
        assertAllowed(file, allowedExtension);
        // 3. 对文件名字进行加工编码，防止文件名乱码
        String fileName = extractFilename(file);
        // 4. 创建文件
        String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();
        file.transferTo(Paths.get(absPath));
        return getPathFileName(baseDir, fileName);
    }


    /**
     * 编码文件名
     */
    public static String extractFilename(MultipartFile file)
    {
        return StringUtils.format("{}/{}_{}.{}", DateUtil.format(DateUtil.date(), "yyyyMMdd"), FileNameUtil.getName(file.getOriginalFilename()), Seq.getId(Seq.uploadSeqType), FileNameUtil.extName(file.getOriginalFilename()));
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @throws FileException 如果超出最大大小
     */
    public static void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws FileException
    {
        //检查文件大小
        long size = file.getSize();
        if (size > DEFAULT_MAX_SIZE)
        {
            throw new FileException("File size limit exceed, max is " + DEFAULT_MAX_SIZE, HttpStatus.BAD_REQUEST.value());
        }
        //检查扩展名，允许的扩展名在MimeTypeUtils类中定义，有图片、视频、音频、flash
        //String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension))
        {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION)
            {
                throw new FileException(StringUtils.format("Invalid image extension {}", extension), HttpStatus.BAD_REQUEST.value());
            }
            else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION)
            {
                throw new FileException(StringUtils.format("Invalid flash extension {}", extension), HttpStatus.BAD_REQUEST.value());
            }
            else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION)
            {
                throw new FileException(StringUtils.format("Invalid media extension {}", extension), HttpStatus.BAD_REQUEST.value());
            }
            else if (allowedExtension == MimeTypeUtils.VIDEO_EXTENSION)
            {
                throw new FileException(StringUtils.format("Invalid video extension {}", extension), HttpStatus.BAD_REQUEST.value());
            }
            else
            {
                throw new FileException(StringUtils.format("Invalid file extension {}", extension), HttpStatus.BAD_REQUEST.value());
            }
        }
    }

    public static File getAbsoluteFile(String uploadDir, String fileName) throws IOException
    {
        File desc = new File(DEFAULT_BASE_DIR + uploadDir + File.separator + fileName);

        if (!desc.exists())
        {
            if (!desc.getParentFile().exists())
            {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    public static String getPathFileName(String uploadDir, String fileName) throws IOException
    {
        int dirLastIndex = DEFAULT_BASE_DIR.length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        return uploadDir + currentDir + "/" + fileName;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static String getExtension(MultipartFile file)
    {
        String extension = FileUtil.extName(Objects.requireNonNull(file.getOriginalFilename()));
        if (StringUtils.isEmpty(extension))
        {

            extension = org.superdata.medismart.utils.file.MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension 文件后缀
     * @param allowedExtension 允许的后缀
     * @return 校验结果
     */
    public static boolean isAllowedExtension(String extension, String[] allowedExtension)
    {
        for (String str : allowedExtension)
        {
            if (str.equalsIgnoreCase(extension))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 复制文件
     *
     * @param source 源文件
     * @param dest 目标文件
     */
    public static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

}
