package no.nav.poc.mdc;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
@RequiredArgsConstructor
public class MdcConfig {

    @SuppressWarnings({"EmptyMethod", "unused"})
    @Pointcut(value = "@annotation(MyCustomMdcAnnotation)")
    public void pointCutForMethodsAnnotatedWithMyCustomMdcAnnotation() {
    }

    @Around("pointCutForMethodsAnnotatedWithMyCustomMdcAnnotation()")
    public Object aroundMethodsAnnotatedWithMyCustomMdcAnnotation(ProceedingJoinPoint joinPoint)
        throws Throwable {

        if (joinPoint.getTarget() instanceof MdcContextProvider) {
            MdcContextProvider provider = (MdcContextProvider) joinPoint.getTarget();
            log.info("Using provider {}", provider.getClass().getName());
            provider
                .getMdcContextMap(joinPoint.getArgs())
                .forEach(MDC::put);
        } else {
            logIncorrectUsage(joinPoint.getTarget().getClass().getName());
        }

        return joinPoint.proceed();

    }

    @After("pointCutForMethodsAnnotatedWithMyCustomMdcAnnotation()")
    public void afterMethodsAnnotatedWithMyCustomMdcAnnotation(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        MyCustomMdcAnnotation annotation = signature.getMethod().getAnnotation(MyCustomMdcAnnotation.class);
        if (annotation.clear()) {

            if (joinPoint.getTarget() instanceof MdcContextProvider) {
                MdcContextProvider provider = (MdcContextProvider) joinPoint.getTarget();
                provider
                    .getMdcContextKeyset()
                    .forEach(MDC::remove);
                log.info("Removed MDC key(s) {} after method {}", provider.getMdcContextKeyset(), signature.getDeclaringTypeName() + "." + signature.getName());
            } else {
                logIncorrectUsage(joinPoint.getTarget().getClass().getName());
            }
        }

    }

    private static void logIncorrectUsage(String className) {
        log.warn("{} uses @{} but does not implement {}", className, MyCustomMdcAnnotation.class.getSimpleName(), MdcContextProvider.class.getSimpleName());
    }

}