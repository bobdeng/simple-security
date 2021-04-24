package cn.bobdeng.security;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Stream;

public class SecurityFilter implements Filter {
    private String cookieName;
    private String passportName;
    private PassportFactory passportFactory;
    private JwtPassportProvider passportProvider;

    public SecurityFilter(@Value("${security.jwt.cookie.name}") String cookieName,
                          @Value("${security.jwt.request.name}") String passportName,
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
        if (request.getCookies() != null) {
            Stream.of(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .findFirst()
                    .ifPresent(cookie -> {
                        String value = passportProvider.from(cookie.getValue());
                        request.setAttribute(passportName, passportFactory.fromString(value));
                    });
        }
    }
}
