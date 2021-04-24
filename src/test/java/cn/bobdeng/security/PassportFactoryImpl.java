package cn.bobdeng.security;

import com.google.gson.Gson;

public class PassportFactoryImpl implements PassportFactory {
    @Override
    public Passport newPassport() {
        return new TestPassport();
    }

    @Override
    public Passport fromString(String value) {
        return new Gson().fromJson(value, TestPassport.class);
    }
}
