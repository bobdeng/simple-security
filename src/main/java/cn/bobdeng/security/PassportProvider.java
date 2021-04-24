package cn.bobdeng.security;

public interface PassportProvider {
    String from(String value);

    String to(String subject);
}
