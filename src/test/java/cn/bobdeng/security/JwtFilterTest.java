package cn.bobdeng.security;

import org.junit.Test;

import javax.servlet.MockFilterChain;
import javax.servlet.MockRequest;
import javax.servlet.MockServletResponse;
import javax.servlet.http.Cookie;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JwtFilterTest {

    public static final String PASSPORT_NAME = "passport";
    public static final String AUTHORIZATION_NAME = "Authorization";

    @Test
    public void 当Cookie是空什么也不做() throws Exception {
        SecurityFilter jwtFilter = new SecurityFilter(AUTHORIZATION_NAME, PASSPORT_NAME,new PassportFactoryImpl());
    }

    @Test
    public void 当Cookie有值() throws Exception {
        Passport passport = new TestPassport(1, Arrays.asList("admin"));
        JwtPassportProvider jwtPassportProvider = new JwtPassportProvider();
        String cookieValue = jwtPassportProvider.toCookieValue(passport);
        SecurityFilter jwtFilter = new SecurityFilter(AUTHORIZATION_NAME, PASSPORT_NAME,new PassportFactoryImpl());
        MockRequest request = new MockRequest();
        request.setCookies(new Cookie[]{new Cookie(AUTHORIZATION_NAME, cookieValue)});
        jwtFilter.doFilter(request, new MockServletResponse(), new MockFilterChain());
        assertThat(request.getAttribute(PASSPORT_NAME), notNullValue());
    }
}
