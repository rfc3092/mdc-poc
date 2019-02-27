package no.nav.poc.domain;

import no.nav.poc.mdc.MyCustomMdcAnnotation;
import org.springframework.stereotype.Service;

@Service
class ExpectedWarningService {

    @MyCustomMdcAnnotation
    void thisWillCauseAWarning() {
    }

}
