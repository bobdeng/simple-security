package cn.bobdeng.security;

import com.google.gson.Gson;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class TestPassport implements Passport {
    private int userId;
    private List<String> roles;

    public TestPassport(int userId, List<String> roles) {
        this.userId = userId;
        this.roles = roles;
    }

    @Override
    public void fromCookie(String value) {
        TestPassport testPassport = new Gson().fromJson(value, TestPassport.class);
        this.userId = testPassport.userId;
        this.roles = testPassport.roles;
    }

    @Override
    public String toStringValue() {
        return new Gson().toJson(this);
    }
}
