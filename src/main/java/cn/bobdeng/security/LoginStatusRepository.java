package cn.bobdeng.security;

public interface LoginStatusRepository<T extends Passport> {
    boolean isLogout(T passport);
}
