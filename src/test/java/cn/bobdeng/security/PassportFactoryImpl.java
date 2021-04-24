package cn.bobdeng.security;

import com.google.gson.Gson;

public class PassportFactoryImpl implements PassportFactory {

    @Override
    public Passport fromString(String value) {
        return new Gson().fromJson(value, TestPassport.class);
    }

    @Override
    public String toString(Passport passport) {
        return new Gson().toJson(passport);
    }
}
