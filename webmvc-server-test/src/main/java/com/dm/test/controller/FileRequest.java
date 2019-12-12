package com.dm.test.controller;

import org.springframework.web.multipart.MultipartFile;

public class FileRequest {
    
    private String name;

    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

}
