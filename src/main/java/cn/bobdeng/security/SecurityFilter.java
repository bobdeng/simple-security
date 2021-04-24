package cn.bobdeng.security;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class SecurityFilter implements Filter {
    private String passportKey;
    private String passportAttributeName;
    private PassportFactory passportFactory;
    private JwtPassportProvider passportProvider;

    public SecurityFilter(String passportKey,
                          String passportAttributeName,
                          PassportFactory passportFactory,
                          JwtPassportProvider passportProvider) {
        this.passportKey = passportKey;
        this.passportAttributeName = passportAttributeName;
        this.passportFactory = passportFactory;
        this.passportProvider = passportProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        getPassportString(request)
                .ifPresent(passportString -> {
                    try {
                        String value = passportProvider.from(passportString);
                        request.setAttribute(passportAttributeName, passportFactory.fromString(value));
                    } catch (Exception e) {
                        log.warn(e.getMessage());
                    }
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
        return Optional.ofNullable(request.getHeader(passportKey));

    }

    private Optional<String> getPassportFromCookie(HttpServletRequest request) {
        return Stream.of(request.getCookies())
                .filter(cookie -> cookie.getName().equals(passportKey))
                .findFirst()
                .map(Cookie::getValue);
    }
}
