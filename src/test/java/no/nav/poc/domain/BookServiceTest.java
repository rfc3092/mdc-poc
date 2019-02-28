package no.nav.poc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BookServiceTest {

    private static final Book BOOK = new Book("Title", new Author("Author"));

    @Autowired
    BookService bookService;

    @Test
    public void testMethodThatIsNotAnnotated() {

        bookService.thisMethodIsNotAnnotated(BOOK);
        log.info("Calls completed");

        assertThat(MDC.getCopyOfContextMap())
            .isNull();
    }

    @Test
    public void testMethodThatIsNotAnnotatedButWillUseAutoCloseableMdcContext() {

        bookService.thisMethodIsNotAnnotatedButWillUseAutoCloseableMdcContext(BOOK);
        log.info("Calls completed");

        assertThat(MDC.getCopyOfContextMap())
            .containsOnly(entry("author.name", BOOK.getAuthor().getName()));

    }

    @Test
    public void testMethodThatIsAnnotated() {

        bookService.thisMethodIsAnnotated(BOOK);
        log.info("Calls completed");

        assertThat(MDC.getCopyOfContextMap())
            .containsOnly(
                entry("book.title", BOOK.getTitle()),
                entry("author.name", BOOK.getAuthor().getName())
            );
    }

    @Test
    public void testMethodThatIsAnnotatedAndWillClear() {

        bookService.thisMethodIsAnnotatedAndWillClear(BOOK);
        log.info("Calls completed");

        assertThat(MDC.getCopyOfContextMap())
            .containsOnly(entry("author.name", BOOK.getAuthor().getName()));

    }


}
