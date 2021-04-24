package cn.bobdeng.security;

public interface PassportFactory {
    Passport newPassport();

    Passport fromString(String value);
}
