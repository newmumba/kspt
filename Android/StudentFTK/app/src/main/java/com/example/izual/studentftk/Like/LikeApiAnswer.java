package com.example.izual.studentftk.Like;

public class LikeApiAnswer {
    public final int httpCode;
    public final String message;

    LikeApiAnswer(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }
}
