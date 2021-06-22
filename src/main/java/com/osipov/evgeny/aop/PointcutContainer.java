package com.osipov.evgeny.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PointcutContainer {
    @Pointcut("execution(public String com.osipov.evgeny.controller.AccountController.*(..))")
    public void allAccountControllerMethods() {
    }

    @Pointcut("execution(* com.osipov.evgeny.controller.SimulationController.*(..))")
    public void allSimulationControllerMethods() {
    }

    @Pointcut("execution(* com.osipov.evgeny.controller.AccountController.authorization(..))" +
            "|| execution(* com.osipov.evgeny.controller.AccountController.registration(..))" +
            "|| execution(* com.osipov.evgeny.controller.WebController.authorization(..))" +
            "|| execution(* com.osipov.evgeny.controller.WebController.registration(..))")
    public void userIsAlreadyRegisteredOrLoggedIn() {
    }

    @Pointcut("!execution(* com.osipov.evgeny.controller.AccountController.authorization(..))" +
            "&& !execution(* com.osipov.evgeny.controller.AccountController.registration(..))" +
            "&& !execution(* com.osipov.evgeny.controller.WebController.authorization(..))" +
            "&& !execution(* com.osipov.evgeny.controller.WebController.registration(..))" +
            "&& (execution(* com.osipov.evgeny.controller.WebController.*(..)) " +
            "    || execution(* com.osipov.evgeny.controller.AccountController.*(..))" +
            "    || execution(* com.osipov.evgeny.controller.SimulationController.*(..)))")
    public void userIsNotLoggedIn() {
    }

}
