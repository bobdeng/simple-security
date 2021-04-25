package cn.bobdeng.security;

public class PassportThreadLocalEmptyImpl<T extends Passport> implements PassportThreadLocal<T> {
    @Override
    public void set(T passport) {

    }

    @Override
    public T get() {
        return null;
    }

    @Override
    public void clear() {

    }
}
