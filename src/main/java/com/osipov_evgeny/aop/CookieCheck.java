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
    // TODO: метод проверки авторизованности пользователя (возможно ли откидывать на регистрацию, если cookie пустует?)

    private Cookie getCookieFromRequestOrReturnNull(){
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

    @Around("PointcutContainer.allAccountControllerMethods() " +
            "&& !PointcutContainer.logoutAccountController()")
    public Object userIsAlreadyLoginCheck(ProceedingJoinPoint point) throws Throwable {
        System.out.println("_acc----------------------_");
        Cookie cookie = getCookieFromRequestOrReturnNull();
        if (cookie != null) {
            System.out.println(cookie.getName());
            System.out.println(cookie.getValue());
        } else {
            System.out.println("Nope");
        }
        System.out.println("_------_");
        Object targetMethod = point.proceed();
        System.out.println("_----------------------_");
        return targetMethod;
    }

    @Around("PointcutContainer.allSimulationControllerMethods() || PointcutContainer.logoutAccountController()")
    public Object simulationControllerCheck(ProceedingJoinPoint point) throws Throwable {
        System.out.println("_----------------------_");
        Cookie cookie = getCookieFromRequestOrReturnNull();
        if (cookie != null) {
            System.out.println(cookie.getName());
            System.out.println(cookie.getValue());
        } else {
            System.out.println("Nope");
        }
        System.out.println("_------_");
        Object targetMethod = point.proceed();
        System.out.println("_----------------------_");
        return targetMethod;
    }
}
