package com.dm.file.util;


import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Locale;

import static java.nio.file.StandardOpenOption.*;

public class DmFileUtils {

    /**
     * 获取一个文件的原始文件名称部分，不包括路径分隔符
     *
     * @param name 文件名称
     * @return 获取到的路径名称
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
     * @param filename 要获取扩展名的文件名
     * @return 获取到的扩展名
     */
    public static String getExt(String filename) {
        return StringUtils.substringAfterLast(filename, ".").toLowerCase(Locale.ROOT);
    }

    /**
     * 连接多个文件为一个文件
     *
     * @param dist    最终保存的目标文件
     * @param sources 文件源
     * @return 合并成功返还true, 合并失败返还false
     */
    public static boolean concatFile(@NotNull Path dist, Path... sources) {
        try (FileChannel out = FileChannel.open(dist, CREATE, APPEND, WRITE)) {
            for (Path path : sources) {
                try (FileChannel in = FileChannel.open(path, READ)) {
                    in.transferTo(0, in.size(), out);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
