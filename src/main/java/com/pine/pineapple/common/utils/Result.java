package com.pine.pineapple.common.utils;

import lombok.Data;

@Data
public class Result<T> {
    private Integer success;
    private String message;
    private T data;



    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.success = 200;
        r.data = data;
        r.message = "ok";
        return r;
    }

    public static <T> Result<T> ok(String message, T data) {
        Result<T> r = ok(data);
        r.message = message;
        return r;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> r = new Result<>();
        r.success = 500;
        r.message = message;
        return r;
    }
}

