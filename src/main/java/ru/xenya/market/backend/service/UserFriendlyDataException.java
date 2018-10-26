package ru.xenya.market.backend.service;

import org.springframework.dao.DataIntegrityViolationException;

public class UserFriendlyDataException extends DataIntegrityViolationException {
    public UserFriendlyDataException(String msg) {
        super(msg);
    }
}
