package ru.otus.services;

public class UserAuthServiceImpl implements UserAuthService {

    private final String user = "admin";
    private final String pwd = "pwd";

    @Override
    public boolean authenticate(String login, String password) {
        return login.equals(user) && password.equals(pwd);
    }

}
