package no.nav.poc.domain;

import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import no.nav.poc.mdc.MdcContextProvider;
import no.nav.poc.mdc.MyCustomMdcAnnotation;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContextClearingService implements MdcContextProvider {

    @MyCustomMdcAnnotation(clear = true)
    void thisWillClearItsMdcContext() {
        log.info("Should clear MDC context");
    }

    @Override
    public Map<String, String> getMdcContextMap() {
        return Collections.singletonMap("Cleared-Key", "Cleared-Value");
    }
}
