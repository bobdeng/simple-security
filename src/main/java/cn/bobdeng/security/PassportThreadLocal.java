package cn.bobdeng.security;

import org.springframework.stereotype.Service;

@Service
public interface PassportThreadLocal<T extends Passport> {

    void set(T passport);

    T get();

    void clear();
}
