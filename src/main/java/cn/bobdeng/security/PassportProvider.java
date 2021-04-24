package cn.bobdeng.security;

public interface PassportProvider {
    String fromCookie(String value);

    String toCookieValue(Passport passport);
}
