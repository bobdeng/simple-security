package cn.bobdeng.security;

public class PassportFactoryImpl implements PassportFactory {
    @Override
    public Passport newPassport() {
        return new TestPassport();
    }
}
