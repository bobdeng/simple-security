package cn.bobdeng.security;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class PassportThreadLocalImpl<T extends Passport> implements PassportThreadLocal<T> {
    private List<String> logs=new ArrayList<>();
    @Override
    public void set(T passport) {
        logs.add("set");
    }

    @Override
    public T get() {
        return null;
    }

    @Override
    public void clear() {
        logs.add("clear");
    }
}
