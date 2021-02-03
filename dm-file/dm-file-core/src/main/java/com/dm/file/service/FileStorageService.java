package com.dm.file.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

public interface FileStorageService {

    /**
     * 用指定文件名保存一个文件
     *
     * @param filename 指定要保存的文件的文件名，文件存储服务要能根据该文件名获取文件
     * @param from     要存储的文件
     * @return true 保存成功<br>
     * false 保存失败
     */
    boolean save(MultipartFile from, String filename, String... parents);

    /**
     * 删除指定的文件
     *
     * @param filename 要删除的文件文件名
     * @return true 删除成功<br>
     * false 删除失败
     */
    boolean delete(String filename, String... parents);

    /**
     * 指定的文件是否存在
     *
     * @param filename 要查找的文件
     * @return 存在返回true, 不存在返回false
     */
    boolean exist(String filename, String... parents);

    /**
     * 根据文件名和前缀获取资源
     *
     * @param filename
     * @param parents
     * @return
     * @throws IOException
     */
    Resource getResource(String filename, String... parents);

//    /**
//     * 获取指定文件名的Resource
//     *
//     * @param filename 要获取资源的文件名
//     * @return
//     */
//    public Resource getResource(String filename);

    /**
     * 根据传入的开始个结束位置返回Resource,用于文件的分块传输
     *
     * @param filename
     * @param start
     * @param end
     * @return
     * @throws IOException
     */
    Resource getResource(String filename, Long start, Long end, String... parents);

    /**
     * 将所有的文件块组合保存到一个文件
     *
     * @param filename
     * @param from
     * @return true 保存成功<br>
     * false 保存失败
     * @throws IOException
     */
    boolean save(Path[] from, String filename, String... parents);

    OutputStream getOutputStream(String filename, String... parents) throws IOException;
}
