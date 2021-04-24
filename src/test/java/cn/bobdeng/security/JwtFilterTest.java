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
        SecurityFilter jwtFilter = new SecurityFilter(AUTHORIZATION_NAME, PASSPORT_NAME, new PassportFactoryImpl(), new JwtPassportProvider());
        MockRequest request = new MockRequest();
        jwtFilter.doFilter(request, new MockServletResponse(), new MockFilterChain());
        assertThat(request.getAttribute(PASSPORT_NAME), nullValue());
    }

    @Test
    public void 当Cookie有值() throws Exception {
        Passport passport = new TestPassport(1, Arrays.asList("admin"));
        JwtPassportProvider jwtPassportProvider = new JwtPassportProvider();
        PassportFactoryImpl passportFactory = new PassportFactoryImpl();
        String cookieValue = jwtPassportProvider.to(passportFactory.toString(passport));
        SecurityFilter jwtFilter = new SecurityFilter(AUTHORIZATION_NAME, PASSPORT_NAME, passportFactory, new JwtPassportProvider());
        MockRequest request = new MockRequest();
        request.setCookies(new Cookie[]{new Cookie(AUTHORIZATION_NAME, cookieValue)});
        jwtFilter.doFilter(request, new MockServletResponse(), new MockFilterChain());
        assertThat(request.getAttribute(PASSPORT_NAME), notNullValue());
        assertThat(request.getAttribute(PASSPORT_NAME), is(passport));
    }
}
