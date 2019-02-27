package no.nav.poc.domain;

import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import no.nav.poc.mdc.MdcContextProvider;
import no.nav.poc.mdc.MyCustomMdcAnnotation;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class SomeComponent implements MdcContextProvider {

    private final SomeService service;

    Map<String, String> thisMethodIsNotAnnotated() {
        return service.thisMethodIsNotAnnotated();
    }

    @MyCustomMdcAnnotation
    Map<String, String> thisMethodIsAnnotated() {
        return service.thisMethodIsAnnotated();
    }

    @Override
    public Map<String, String> getMdcContextMap() {
        return Collections.singletonMap("Component-Key", "Component-Value");
    }

}
