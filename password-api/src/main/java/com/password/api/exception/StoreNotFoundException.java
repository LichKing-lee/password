package com.password.api.exception;

import com.password.exception.PasswordException;

public class StoreNotFoundException extends PasswordException {

    public StoreNotFoundException(Long storeId) {
        super("Store not found: id=" + storeId, "존재하지 않는 상점입니다.");
    }
}
