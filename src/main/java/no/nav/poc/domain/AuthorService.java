package no.nav.poc.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.poc.mdc.MdcContext;
import no.nav.poc.mdc.MdcContextProvider;
import no.nav.poc.mdc.MyCustomMdcAnnotation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService implements MdcContextProvider {

    void thisMethodIsNotAnnotated(Author author) {
        log.info("Handling author {} without annotation", author);
    }

    void thismethodIsNotAnnotatedButWillUseMdcContext(Author author) {
        new MdcContext(Collections.singletonMap("author.name", author.getName()));
        log.info("Handling author {} with autocloseable (not actually closing)", author);
    }

    @MyCustomMdcAnnotation
    void thisMethodIsAnnotated(Author author) {
        log.info("Handling author {} with annotation", author);
    }

    @Override
    public Map<String, String> getMdcContextMap(Object[] args) {
        return Arrays
            .stream(args)
            .filter(Author.class::isInstance)
            .findFirst()
            .map(Author.class::cast)
            .map(author -> Collections.singletonMap("author.name", author.getName()))
            .orElse(Collections.emptyMap());

    }
}
