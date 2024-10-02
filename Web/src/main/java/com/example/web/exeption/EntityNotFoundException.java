package com.example.web.exeption;

public class EntityNotFoundException  extends RuntimeException{
    // Конструктор без параметров
    public EntityNotFoundException() {
        super();
    }

    // Конструктор с сообщением
    public EntityNotFoundException(String message) {
        super(message);
    }

    // Конструктор с сообщением и причиной (другим исключением)
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Конструктор с причиной (другим исключением)
    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
