package cn.bobdeng.security;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class SecurityFilter implements Filter {
    private String cookieName;
    private String passportName;
    private PassportFactory passportFactory;
    private JwtPassportProvider passportProvider;

    public SecurityFilter(String cookieName,
                          String passportName,
                          PassportFactory passportFactory,
                          JwtPassportProvider passportProvider) {
        this.cookieName = cookieName;
        this.passportName = passportName;
        this.passportFactory = passportFactory;
        this.passportProvider = passportProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        getPassportString(request)
                .ifPresent(passportString -> {
                    String value = passportProvider.from(passportString);
                    request.setAttribute(passportName, passportFactory.fromString(value));
                });

        chain.doFilter(servletRequest, response);
    }

    private Optional<String> getPassportString(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Optional<String> passportFromCookie = getPassportFromCookie(request);
            if (passportFromCookie.isPresent()) {
                return passportFromCookie;
            }
        }
        return Optional.ofNullable(request.getHeader(cookieName));

    }

    private Optional<String> getPassportFromCookie(HttpServletRequest request) {
        return Stream.of(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue);
    }
}
