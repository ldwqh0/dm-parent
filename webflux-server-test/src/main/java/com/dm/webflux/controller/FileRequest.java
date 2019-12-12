package com.dm.webflux.controller;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

public class FileRequest {

    private String name;

    private FilePart file;

    public FilePart getFile() {
        return file;
    }

    public void setFile(FilePart file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
