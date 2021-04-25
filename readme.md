# Introduction
  This project is used to obtain user passport from http cookie or header, and put in request's attribute. the user information is encoded by jwt or other.

# Guide
- Implement PassportFactory
    
    Sample: use json to store passport
  ```
    public class PassportFactoryImpl implements PassportFactory {

    @Override
    public Passport fromString(String value) {
        return new Gson().fromJson(value, TestPassport.class);
    }

    @Override
    public String toString(Passport passport) {
        return new Gson().toJson(passport);
    }
   }
  ```
  
- Implement PassportThreadLocal
  
  To store passport to a ThreadLocal and ensure to clear ThreadLocal when http call is over.  

- Config filter

    ```
    @Bean
    public FilterRegistrationBean<Filter> loginFilterRegistration() {
        SecurityFilter securityFilter = new SecurityFilter(cookieName,passportName,passportFactory,new JwtPassportProvider());
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(securityFilter);
        registration.addUrlPatterns("/*");
        registration.setName("securityFilter");
        registration.setOrder(1);
        return registration;
    }
    ```
