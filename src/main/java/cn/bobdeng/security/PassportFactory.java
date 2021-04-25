package cn.bobdeng.security;

public interface PassportFactory<T extends Passport> {

    T fromString(String value);

    String toString(T passport);
}
