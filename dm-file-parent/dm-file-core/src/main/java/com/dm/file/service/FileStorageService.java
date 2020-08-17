package com.dm.file.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;

public interface FileStorageService {

    /**
     * 用指定文件名保存一个文件
     *
     * @param filename    指定要保存的文件的文件名，文件存储服务要能根据该文件名获取文件
     * @param inputStream 要存储的文件数据流
     * @return true 保存成功<br>
     *         false 保存失败
     */
    public boolean save(String filename, InputStream inputStream);

    /**
     * 用指定文件名保存一个文件
     *
     * @param filename 指定要保存的文件的文件名，文件存储服务要能根据该文件名获取文件
     * @param file     要存储的文件
     * @return true 保存成功<br>
     *         false 保存失败
     */
    public boolean save(String filename, File file);

    /**
     * 删除指定的文件
     *
     * @param filename 要删除的文件文件名
     * @return true 删除成功<br>
     *         false 删除失败
     */
    public boolean delete(String filename);

    /**
     * 指定的文件是否存在
     *
     * @param filename 要查找的文件
     * @return 存在返回true,不存在返回false
     */
    public boolean exist(String filename);

    /**
     * 获取指定文件名的Resource
     *
     * @param filename 要获取资源的文件名
     * @return
     */
    public Resource getResource(String filename);

    /**
     * 根据传入的开始个结束位置返回Resource,用于文件的分块传输
     * 
     * @param filename
     * @param start
     * @param end
     * @return
     */
    public Resource getResource(String filename, Long start, Long end);

    /**
     * 将所有的文件块组合保存到一个文件
     * 
     * @param filename
     * @param src
     * @return true 保存成功<br>
     *         false 保存失败
     * @throws IOException
     */
    public boolean save(String filename, File[] src);
}
