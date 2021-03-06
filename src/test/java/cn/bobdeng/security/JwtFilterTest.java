package cn.bobdeng.security;

import org.junit.Before;
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
    private SecurityFilter jwtFilter;
    private MockRequest request;
    private GsonPassportFactory passportFactory;
    private PassportThreadLocalImpl passportThreadLocal;
    private MockServletResponse response;
    private JwtPassportProvider passportProvider;
    private LoginStatusRepositoryImpl<Passport> passportLoginStatusRepository;

    @Before
    public void setup() {
        passportFactory = new GsonPassportFactory(TestPassport.class);
        passportThreadLocal = new PassportThreadLocalImpl();
        passportLoginStatusRepository = new LoginStatusRepositoryImpl<>();
        passportLoginStatusRepository.setLogout(false);
        passportProvider = new JwtPassportProvider("Bearer ", 60000, "123456");
        jwtFilter = new SecurityFilter(AUTHORIZATION_NAME, PASSPORT_NAME, passportFactory, passportProvider, passportThreadLocal, passportLoginStatusRepository);
        request = new MockRequest();
        response = new MockServletResponse();
    }

    @Test
    public void 当Cookie是空什么也不做() throws Exception {
        jwtFilter.doFilter(request, new MockServletResponse(), new MockFilterChain());
        assertThat(request.getAttribute(PASSPORT_NAME), nullValue());
    }

    @Test
    public void 当Cookie有值() throws Exception {
        Passport passport = new TestPassport(1, Arrays.asList("admin"));
        String cookieValue = passportProvider.to(passportFactory.toString(passport));
        request.setCookies(new Cookie[]{new Cookie(AUTHORIZATION_NAME, cookieValue)});
        jwtFilter.doFilter(request, response, new MockFilterChain());
        assertThat(request.getAttribute(PASSPORT_NAME), notNullValue());
        assertThat(request.getAttribute(PASSPORT_NAME), is(passport));
        assertThat(passportThreadLocal.getLogs(), is(Arrays.asList("set", "clear")));
        assertThat(response.getCookies().size(), is(1));
    }

    @Test
    public void 当Header有值() throws Exception {
        Passport passport = new TestPassport(1, Arrays.asList("admin"));
        String cookieValue = passportProvider.to(passportFactory.toString(passport));
        request.setHeader(AUTHORIZATION_NAME, cookieValue);
        jwtFilter.doFilter(request, new MockServletResponse(), new MockFilterChain());
        assertThat(request.getAttribute(PASSPORT_NAME), notNullValue());
        assertThat(request.getAttribute(PASSPORT_NAME), is(passport));
        assertThat(passportThreadLocal.getLogs(), is(Arrays.asList("set", "clear")));
    }

    @Test
    public void 当Header有值但已经登出() throws Exception {
        Passport passport = new TestPassport(1, Arrays.asList("admin"));
        passportLoginStatusRepository.setLogout(true);
        String cookieValue = passportProvider.to(passportFactory.toString(passport));
        request.setHeader(AUTHORIZATION_NAME, cookieValue);
        jwtFilter.doFilter(request, new MockServletResponse(), new MockFilterChain());
        assertThat(request.getAttribute(PASSPORT_NAME), nullValue());
    }


    @Test
    public void 当Header有值但不合法() throws Exception {
        request.setHeader(AUTHORIZATION_NAME, "illegal value");
        jwtFilter.doFilter(request, new MockServletResponse(), new MockFilterChain());
        assertThat(request.getAttribute(PASSPORT_NAME), nullValue());
        assertThat(passportThreadLocal.getLogs(), is(Arrays.asList("clear")));
    }
}
