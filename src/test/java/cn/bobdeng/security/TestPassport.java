package cn.bobdeng.security;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode
public class TestPassport implements Passport {
    private int userId;
    private List<String> roles;

    public TestPassport(int userId, List<String> roles) {
        this.userId = userId;
        this.roles = roles;
    }
}
