package com.portfolio.recipebook.util;

public class UserNicknameAlreadyExistException extends RuntimeException {
    public UserNicknameAlreadyExistException(String nickname) {
        super("User nickname: " + nickname + " already exist.");
    }
}
