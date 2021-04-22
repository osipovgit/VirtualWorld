package com.osipov_evgeny.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PointcutContainer {
    @Pointcut("execution(public String com.osipov_evgeny.controller.AccountController.*(..))")
    public void allAccountControllerMethods(){}

    @Pointcut("execution(public void com.osipov_evgeny.controller.AccountController.logout(..))")
    public void logoutAccountController(){}

    @Pointcut("execution(* com.osipov_evgeny.controller.SimulationController.*(..))")
    public void allSimulationControllerMethods(){}
}
