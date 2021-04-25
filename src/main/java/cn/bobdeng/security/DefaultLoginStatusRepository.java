package cn.bobdeng.security;

public class DefaultLoginStatusRepository<T extends Passport> implements LoginStatusRepository<T> {
    @Override
    public boolean isLogout(T passport) {
        return false;
    }
}
