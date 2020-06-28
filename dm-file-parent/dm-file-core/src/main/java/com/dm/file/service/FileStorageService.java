package com.dm.file.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

//import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * 将一个文件按指定的key存储
     *
     * @param key         指定的key
     * @param inputStream 要存储的文件
     * @throws Exception
     */
    public boolean save(String path, InputStream inputStream) throws IOException;

    /**
     * 保存一个文件，用指定文件名
     *
     * @param fileName
     * @param file
     * @return
     * @throws IOException
     */
    public boolean save(String path, File file) throws IOException;

    /**
     * 删除指定的文件
     *
     * @param path 要删除的文件的路径
     * @return
     * @throws Exception
     */
    public boolean delete(String path) throws IOException;

    /**
     * 指定的文件是否存在
     *
     * @param path 要查找的文件
     * @return 存在返回true,不存在返回false
     * @throws Exception
     */
    public boolean exist(String path) throws Exception;


    /**
     * 获取指定的文件流
     *
     * @param path
     * @return
     * @throws Exception
     */
    public InputStream getInputStream(String path) throws Exception;

    public boolean save(String path, File[] src) throws IOException;
}
