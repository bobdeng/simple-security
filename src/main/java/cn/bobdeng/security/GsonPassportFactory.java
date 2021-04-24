package cn.bobdeng.security;

import com.google.gson.Gson;

public class GsonPassportFactory implements PassportFactory {
    private Class<? extends Passport> passportClass;

    public GsonPassportFactory(Class<? extends Passport> passportClass) {
        this.passportClass = passportClass;
    }

    @Override
    public Passport fromString(String value) {
        return new Gson().fromJson(value, passportClass);
    }

    @Override
    public String toString(Passport passport) {
        return new Gson().toJson(passport);
    }
}
