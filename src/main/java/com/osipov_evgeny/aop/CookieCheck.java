package com.osipov_evgeny.aop;

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

    private Cookie getCookieFromRequestOrReturnNull() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        Cookie cookie = null;
        for (Cookie cookies : request.getCookies()) {
            if (cookies.getName().equals("user_id")) {
                cookie = cookies;
                break;
            }
        }
        return cookie;
    }

    @Around("PointcutContainer.userIsAlreadyRegisteredOrLoggedIn()")
    public String userIsAlreadyLoginCheck(ProceedingJoinPoint point) throws Throwable {
        System.out.println(">>> userIsAlreadyLoginCheck ----------------------");
        Cookie cookie = getCookieFromRequestOrReturnNull();
        if (cookie != null) {
            System.out.println("INFO --- [userIsAlreadyLoginCheck]  User with id = " + cookie.getValue()
                    + " trying auth again.");
            return "redirect:/";
        } else {
            Object targetMethod = point.proceed();
            return targetMethod.toString();
        }
    }

    @Around("PointcutContainer.userIsNotLoggedIn()")
    public String userIsNotLoggedInCheck(ProceedingJoinPoint point) throws Throwable {
        Cookie cookie = getCookieFromRequestOrReturnNull();
        if (cookie != null) {
            Object targetMethod = point.proceed();
            return targetMethod.toString();
        } else {
            System.out.println("INFO --- [userIsNotLoggedInCheck]  Anonymous user trying enter.");
            return "redirect:/auth";
        }
    }
}
