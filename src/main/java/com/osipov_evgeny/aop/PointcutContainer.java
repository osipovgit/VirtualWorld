package com.osipov_evgeny.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PointcutContainer {
    @Pointcut("execution(public String com.osipov_evgeny.controller.AccountController.*(..))")
    public void allAccountControllerMethods() {
    }

    @Pointcut("execution(* com.osipov_evgeny.controller.SimulationController.*(..))")
    public void allSimulationControllerMethods() {
    }

    @Pointcut("execution(* com.osipov_evgeny.controller.AccountController.authorization(..))" +
            "|| execution(* com.osipov_evgeny.controller.AccountController.registration(..))" +
            "|| execution(* com.osipov_evgeny.controller.WebController.authorization(..))" +
            "|| execution(* com.osipov_evgeny.controller.WebController.registration(..))")
    public void userIsAlreadyRegisteredOrLoggedIn() {
    }

    @Pointcut("!execution(* com.osipov_evgeny.controller.AccountController.authorization(..))" +
            "&& !execution(* com.osipov_evgeny.controller.AccountController.registration(..))" +
            "&& !execution(* com.osipov_evgeny.controller.WebController.authorization(..))" +
            "&& !execution(* com.osipov_evgeny.controller.WebController.registration(..))" +
            "&& (execution(* com.osipov_evgeny.controller.WebController.*(..)) " +
            "    || execution(* com.osipov_evgeny.controller.AccountController.*(..))" +
            "    || execution(* com.osipov_evgeny.controller.SimulationController.*(..)))")
    public void userIsNotLoggedIn() {
    }

}
