package com.funix.prj_321x.asm03.constant;

public class SecurityConstant {
    public static final long EXPIRATION_JWT_TIME = 432_000_000; // 5 days expressed in milliseconds
    public static final long EXPIRATION_RESET_PW_TIME = 86_400_000; // 1 day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String FUNIX_PRJ321_ASM03 = "FUNiX Prj321x 3.0VN Asm03";
    public static final String ADMINISTRATION = "Admin";
    public static final String AUTHORITY = "authority";
    public static final String FORBIDDEN_MESSAGE = "You need to login to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = { "/", "user/login", "user/register", "/user/reset-password", "/user/reset-password/**"};
    public static final String[] AUTH_WHITELIST = { "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui.html", "/webjars/**", "/v3/api-docs/**", "/api/public/**",
            "/api/public/authenticate", "/actuator/*", "/swagger-ui/**" };
}
