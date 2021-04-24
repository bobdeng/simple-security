package cn.bobdeng.security;

public interface PassportFactory {

    Passport fromString(String value);

    String toString(Passport passport);
}
