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
class BookService implements MdcContextProvider {

    private final AuthorService authorService;

    void thisMethodIsNotAnnotated(Book book) {
        log.info("Handling book {} without annotation", book);
        authorService.thisMethodIsNotAnnotated(book.getAuthor());
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
    public Set<String> getMdcContextKeyset() {
        return Collections.singleton("book.title");
    }

    @Override
    public Map<String, String> getMdcContextMap(Object[] args) {

        String bookTitle = Arrays
            .stream(args)
            .filter(Book.class::isInstance)
            .findFirst()
            .map(Book.class::cast)
            .map(Book::getTitle)
            .orElse(null);
        return Collections.singletonMap("book.title", bookTitle);

    }
}
