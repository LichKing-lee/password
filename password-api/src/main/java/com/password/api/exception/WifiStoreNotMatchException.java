package com.password.api.exception;

import com.password.exception.PasswordException;

public class WifiStoreNotMatchException extends PasswordException {

    public WifiStoreNotMatchException(Long wifiId, Long storeId) {
        super("Wifi store not match: wifiId=" + wifiId + ", storeId=" + storeId, "해당 상점의 와이파이가 아닙니다.");
    }
}
