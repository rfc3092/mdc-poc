package no.nav.poc.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
class Book {

    private final String title;
    private final Author author;

}
