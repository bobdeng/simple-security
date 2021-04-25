package cn.bobdeng.security;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class SecurityFilter<T extends Passport> implements Filter {
    private final String passportKey;
    private final String passportAttributeName;
    private final PassportFactory<T> passportFactory;
    private final JwtPassportProvider passportProvider;
    private final PassportThreadLocal<T> passportThreadLocal;
    private final LoginStatusRepository<T> loginStatusRepository;

    public SecurityFilter(String passportKey,
                          String passportAttributeName,
                          PassportFactory<T> passportFactory,
                          JwtPassportProvider passportProvider,
                          PassportThreadLocal<T> passportThreadLocal) {
        this.passportKey = passportKey;
        this.passportAttributeName = passportAttributeName;
        this.passportFactory = passportFactory;
        this.passportProvider = passportProvider;
        this.passportThreadLocal = passportThreadLocal;
        this.loginStatusRepository = new DefaultLoginStatusRepository();
    }

    public SecurityFilter(String passportKey,
                          String passportAttributeName,
                          PassportFactory<T> passportFactory,
                          JwtPassportProvider passportProvider,
                          PassportThreadLocal<T> passportThreadLocal,
                          LoginStatusRepository<T> loginStatusRepository) {
        this.passportKey = passportKey;
        this.passportAttributeName = passportAttributeName;
        this.passportFactory = passportFactory;
        this.passportProvider = passportProvider;
        this.passportThreadLocal = passportThreadLocal;

        this.loginStatusRepository = loginStatusRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        transferPassport(request, (HttpServletResponse) response);
        try {
            chain.doFilter(servletRequest, response);
        } finally {
            passportThreadLocal.clear();
        }
    }

    private void transferPassport(HttpServletRequest request, HttpServletResponse response) {
        getPassportString(request)
                .ifPresent(passportString -> parsePassport(request, passportString, response));
    }

    private void parsePassport(HttpServletRequest request, String passportString, HttpServletResponse response) {
        try {
            String value = passportProvider.from(passportString);
            T passport = passportFactory.fromString(value);
            if (loginStatusRepository.isLogout(passport)) {
                return;
            }
            passportThreadLocal.set(passport);
            writeNewPassportToResponse(response, passport);
            request.setAttribute(passportAttributeName, passport);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private void writeNewPassportToResponse(HttpServletResponse response, T passport) {
        Cookie cookie = new Cookie(passportKey, passportProvider.to(passportFactory.toString(passport)));
        cookie.setMaxAge((int) passportProvider.getExpireAfter() / 1000);
        response.addCookie(cookie);
    }

    private Optional<String> getPassportString(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Optional<String> passportFromCookie = getPassportFromCookie(request);
            if (passportFromCookie.isPresent()) {
                return passportFromCookie;
            }
        }
        return Optional.ofNullable(request.getHeader(passportKey));

    }

    private Optional<String> getPassportFromCookie(HttpServletRequest request) {
        return Stream.of(request.getCookies())
                .filter(cookie -> cookie.getName().equals(passportKey))
                .findFirst()
                .map(Cookie::getValue);
    }
}
