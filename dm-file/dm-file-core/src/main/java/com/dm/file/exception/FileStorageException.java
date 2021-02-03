package com.dm.file.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "File write failure")
public class FileStorageException extends RuntimeException {

    private static final long serialVersionUID = 2347371832589053526L;

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException() {
        super("File write failure");
    }

}
