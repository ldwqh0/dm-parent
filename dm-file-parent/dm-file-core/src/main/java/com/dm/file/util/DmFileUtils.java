package com.dm.file.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

public class DmFileUtils {

    /**
     * 获取一个文件的原始文件名
     *
     * @param name
     * @return
     */
    public static String getOriginalFilename(String name) {
        if (StringUtils.isNotBlank(name)) {
            String[] names = name.split("\\\\|\\/");
            return names[names.length - 1];
        }
        return "";
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExt(String filename) {
        return StringUtils.substringAfterLast(filename, ".").toLowerCase(Locale.ROOT);
    }

    /**
     * 将所有的片段文件组合到目标文件
     *
     * @param dist 目标文件
     * @param src  源文件的列表
     * @throws IOException
     */
    public static void concatFile(File dist, File[] src) throws IOException {
        try (OutputStream oStream = new FileOutputStream(dist)) {
            for (File file : src) {
                FileUtils.copyFile(file, oStream);
            }
        }
    }
}
