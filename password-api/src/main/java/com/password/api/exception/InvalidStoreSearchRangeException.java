package com.password.api.exception;

import com.password.exception.PasswordException;

public class InvalidStoreSearchRangeException extends PasswordException {

    public InvalidStoreSearchRangeException(String detail) {
        super("Invalid store search range: " + detail, "잘못된 검색 범위입니다.");
    }
}
