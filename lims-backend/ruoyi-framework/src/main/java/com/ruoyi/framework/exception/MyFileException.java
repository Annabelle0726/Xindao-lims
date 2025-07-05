package com.ruoyi.framework.exception;

public class MyFileException extends RuntimeException{
    public MyFileException() {
    }

    public MyFileException(String message) {
        super(message);
    }

    public MyFileException(Throwable cause) {
        super(cause);
    }

    public MyFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
