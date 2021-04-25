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
public class SecurityFilter implements Filter {
    private String passportKey;
    private String passportAttributeName;
    private PassportFactory passportFactory;
    private JwtPassportProvider passportProvider;
    private PassportThreadLocal passportThreadLocal;

    public SecurityFilter(String passportKey,
                          String passportAttributeName,
                          PassportFactory passportFactory,
                          JwtPassportProvider passportProvider,
                          PassportThreadLocal passportThreadLocal) {
        this.passportKey = passportKey;
        this.passportAttributeName = passportAttributeName;
        this.passportFactory = passportFactory;
        this.passportProvider = passportProvider;
        this.passportThreadLocal = passportThreadLocal;
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
            Passport passport = passportFactory.fromString(value);
            passportThreadLocal.set(passport);
            writeNewPassportToResponse(response, passport);
            request.setAttribute(passportAttributeName, passport);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private void writeNewPassportToResponse(HttpServletResponse response, Passport passport) {
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
