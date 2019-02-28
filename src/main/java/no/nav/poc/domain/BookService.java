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
class BookService implements MdcContextProvider {

    private final AuthorService authorService;

    void thisMethodIsNotAnnotated(Book book) {
        log.info("Handling book {} without annotation", book);
        authorService.thisMethodIsNotAnnotated(book.getAuthor());
    }

    void thisMethodIsNotAnnotatedButWillUseAutoCloseableMdcContext(Book book) {
        log.info("Handling book {} with autocloseable", book);
        try (MdcContext ignored = new MdcContext(Collections.singletonMap("book.title", book.getTitle()))) {
            authorService.thismethodIsNotAnnotatedButWillUseMdcContext(book.getAuthor());
        }
    }

    @MyCustomMdcAnnotation
    void thisMethodIsAnnotated(Book book) {
        log.info("Handling book {} with annotation", book);
        authorService.thisMethodIsAnnotated(book.getAuthor());
    }

    @MyCustomMdcAnnotation(clear = true)
    void thisMethodIsAnnotatedAndWillClear(Book book) {
        log.info("Handling book {} with annotation (clearing)", book);
        authorService.thisMethodIsAnnotated(book.getAuthor());
    }

    @Override
    public Map<String, String> getMdcContextMap(Object[] args) {
        return Arrays
            .stream(args)
            .filter(Book.class::isInstance)
            .findFirst()
            .map(Book.class::cast)
            .map(book -> Collections.singletonMap("book.title", book.getTitle()))
            .orElse(Collections.emptyMap());
    }
}
