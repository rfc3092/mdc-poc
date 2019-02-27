package no.nav.poc.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @MyCustomMdcAnnotation
    void thisMethodIsAnnotated(Author author) {
        log.info("Handling author {} with annotation", author);
    }

    @Override
    public Set<String> getMdcContextKeyset() {
        return Collections.singleton("author.name");
    }

    @Override
    public Map<String, String> getMdcContextMap(Object[] args) {

        String authorName = Arrays
            .stream(args)
            .filter(Author.class::isInstance)
            .findFirst()
            .map(Author.class::cast)
            .map(Author::getName)
            .orElse(null);
        return Collections.singletonMap("author.name", authorName);

    }
}
