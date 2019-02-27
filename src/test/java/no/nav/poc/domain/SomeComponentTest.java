package no.nav.poc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SomeComponentTest {

    @Autowired
    SomeComponent someComponent;

    @Test
    public void testMethodThatIsAnnotated() {
        assertThat(someComponent.thisMethodIsAnnotated())
            .containsExactly(
                entry("Component-Key", "Component-Value"),
                entry("Service-Key", "Service-Value")
            );
    }

    @Test
    public void testMethodThatIsNotAnnotated() {
        assertThat(someComponent.thisMethodIsNotAnnotated())
            .isNull();
    }

}
