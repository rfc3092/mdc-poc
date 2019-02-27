package no.nav.poc.domain;

import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import no.nav.poc.mdc.MdcContextProvider;
import no.nav.poc.mdc.MyCustomMdcAnnotation;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SomeService implements MdcContextProvider {

    private final ExpectedWarningService expectedWarningService;
    private final ContextClearingService contextClearingService;

    Map<String, String> thisMethodIsNotAnnotated() {
        return MDC.getCopyOfContextMap();
    }

    @MyCustomMdcAnnotation
    Map<String, String> thisMethodIsAnnotated() {

        expectedWarningService.thisWillCauseAWarning();
        contextClearingService.thisWillClearItsMdcContext();
        return MDC.getCopyOfContextMap();

    }

    @Override
    public Map<String, String> getMdcContextMap() {
        return Collections.singletonMap("Service-Key", "Service-Value");
    }
}
