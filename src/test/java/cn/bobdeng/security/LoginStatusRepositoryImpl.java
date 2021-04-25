package cn.bobdeng.security;

import lombok.Setter;

public class LoginStatusRepositoryImpl<T extends Passport> implements LoginStatusRepository<TestPassport> {
    @Setter
    private boolean logout;
    @Override
    public boolean isLogout(TestPassport passport) {
        return logout;
    }
}
