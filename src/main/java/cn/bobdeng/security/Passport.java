package cn.bobdeng.security;

public interface Passport {
    void fromCookie(String value);

    String toStringValue();
}
