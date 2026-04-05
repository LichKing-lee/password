package com.password.api.exception;

import com.password.exception.PasswordException;

public class WifiNotFoundException extends PasswordException {

    public WifiNotFoundException(Long wifiId) {
        super("Wifi not found: id=" + wifiId, "존재하지 않는 와이파이입니다.");
    }
}
