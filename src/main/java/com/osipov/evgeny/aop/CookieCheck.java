package com.osipov.evgeny.aop;

import com.osipov.evgeny.config.MappingConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class CookieCheck {

    public static final String COOKIE_KEY = "user_id";

    private Cookie getCookieFromRequestOrReturnNull() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        Cookie cookie = null;
        for (Cookie cookies : request.getCookies()) {
            if (cookies.getName().equals(COOKIE_KEY)) {
                cookie = cookies;
                break;
            }
        }
        return cookie;
    }

    @Around("com.osipov.evgeny.aop.PointcutContainer.userIsAlreadyRegisteredOrLoggedIn()")
    public String userIsAlreadyLoginCheck(ProceedingJoinPoint point) throws Throwable {
        System.out.println(">>> userIsAlreadyLoginCheck ----------------------");
        Cookie cookie = getCookieFromRequestOrReturnNull();
        if (cookie != null) {
            System.out.println("INFO --- [userIsAlreadyLoginCheck]  User with id = " + cookie.getValue()
                    + " trying auth again.");
            return MappingConfig.REDIRECT_HOME;
        } else {
            Object targetMethod = point.proceed();
            return targetMethod.toString();
        }
    }

    @Around("com.osipov.evgeny.aop.PointcutContainer.userIsNotLoggedIn()")
    public String userIsNotLoggedInCheck(ProceedingJoinPoint point) throws Throwable {
        Cookie cookie = getCookieFromRequestOrReturnNull();
        if (cookie != null) {
            Object targetMethod = point.proceed();
            return targetMethod.toString();
        } else {
            System.out.println("INFO --- [userIsNotLoggedInCheck]  Anonymous user trying enter.");
            return MappingConfig.REDIRECT_AUTH;
        }
    }
}
