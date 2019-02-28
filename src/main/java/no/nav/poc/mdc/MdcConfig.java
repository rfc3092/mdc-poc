package no.nav.poc.mdc;


import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class MdcConfig {

    @SuppressWarnings({"EmptyMethod", "unused"})
    @Pointcut("@annotation(annotation)")
    public void annotatedWithMyCustomMdcAnnotation(MyCustomMdcAnnotation annotation) {
    }

    @SuppressWarnings({"EmptyMethod", "unused"})
    @Pointcut("within(MdcContextProvider+) && target(provider)")
    public void implementsMdcContextProvider(MdcContextProvider provider) {
    }

    @Around(
        value = "annotatedWithMyCustomMdcAnnotation(annotation) && implementsMdcContextProvider(provider)",
        argNames = "joinPoint,annotation,provider"
    )
    public Object setMdcContextFromProvider(
        ProceedingJoinPoint joinPoint,
        MyCustomMdcAnnotation annotation,
        MdcContextProvider provider
    )
        throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Map<String, String> mdcContextMap = provider.getMdcContextMap(joinPoint.getArgs());
        putIntoMdcContext(mdcContextMap, getMethodName(signature));
        Object proceed = joinPoint.proceed();
        if (annotation.clear()) {
            removeFromMdcContext(mdcContextMap.keySet(), getMethodName(signature));
        }
        return proceed;

    }

    private static void putIntoMdcContext(Map<String, String> mdcContextMap, String methodName) {
        mdcContextMap.forEach(MDC::put);
        log.info("Set MDC to {} before method {}", mdcContextMap, methodName);
    }

    private static void removeFromMdcContext(Set<String> mdcContextKeySet, String methodName) {
        mdcContextKeySet.forEach(MDC::remove);
        log.info("Removed MDC key(s) {} after method {}", mdcContextKeySet, methodName);
    }

    private static String getMethodName(MethodSignature signature) {
        return signature.getDeclaringTypeName() + "." + signature.getName();
    }

}