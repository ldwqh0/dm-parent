package com.dm.file.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;

public interface FileStorageService {

    /**
     * 将一个文件按指定的key存储
     *
     * @param key         指定的key
     * @param inputStream 要存储的文件
     * @return true 保存成功<br>
     *         false 保存失败
     * @throws Exception
     */
    public boolean save(String path, InputStream inputStream);

    /**
     * 保存一个文件，用指定文件名
     *
     * @param fileName
     * @param file     要存储的文件
     * @return true 保存成功<br>
     *         false 保存失败
     * @throws IOException
     */
    public boolean save(String path, File file);

    /**
     * 删除指定的文件
     *
     * @param path 要删除的文件的路径
     * @return true 删除成功<br>
     *         false 删除失败
     * @throws Exception
     */
    public boolean delete(String path);

    /**
     * 指定的文件是否存在
     *
     * @param path 要查找的文件
     * @return 存在返回true,不存在返回false
     * @throws Exception
     */
    public boolean exist(String path);

    /**
     * 获取指定的文件流
     *
     * @param path
     * @return
     * @throws Exception
     */
    public Resource getResource(String path);

    /**
     * 根据传入的开始个结束位置返回Resource,用于文件的分块传输
     * 
     * @param path
     * @param start
     * @param end
     * @return
     */
    public Resource getResource(String path, Long start, Long end);

    /**
     * 将所有的文件块组合保存到一个文件
     * 
     * @param path
     * @param src
     * @return true 保存成功<br>
     *         false 保存失败
     * @throws IOException
     */
    public boolean save(String path, File[] src);
}
