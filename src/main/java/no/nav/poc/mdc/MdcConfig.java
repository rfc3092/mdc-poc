package no.nav.poc.mdc;


import java.util.HashSet;
import java.util.Set;
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
                .getMdcContextMap()
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
        if (annotation != null && annotation.clear()) {

            if (joinPoint.getTarget() instanceof MdcContextProvider) {
                MdcContextProvider provider = (MdcContextProvider) joinPoint.getTarget();
                Set<String> removed = new HashSet<>();
                provider
                    .getMdcContextMap()
                    .forEach((k, v) -> {
                        MDC.remove(k);
                        removed.add(k);
                    });
                log.info("Removed MDC key(s) {} after method {}", removed, signature.getDeclaringTypeName() + "." + signature.getName());
            } else {
                logIncorrectUsage(joinPoint.getTarget().getClass().getName());
            }
        }

    }

    private static void logIncorrectUsage(String className) {
        log.warn("{} uses @{} but does not implement {}", className, MyCustomMdcAnnotation.class.getSimpleName(), MdcContextProvider.class.getSimpleName());
    }

}