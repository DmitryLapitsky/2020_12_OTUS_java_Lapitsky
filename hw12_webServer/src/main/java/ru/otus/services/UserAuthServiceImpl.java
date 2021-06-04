package ru.otus.services;

public class UserAuthServiceImpl implements UserAuthService {

    @Override
    public boolean authenticate(String login, String password) {
        String user = "admin";
        String pwd = "pwd";
        return login.equals(user) && password.equals(pwd);
    }

}
