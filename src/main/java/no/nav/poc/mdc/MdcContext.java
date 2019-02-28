package no.nav.poc.mdc;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.MDC;

/**
 * Alternative.
 */
public class MdcContext implements AutoCloseable {

    private final Set<String> keys = new HashSet<>();

    public MdcContext(Map<String, String> contextMap) {
        contextMap.forEach((k, v) -> {
            MDC.put(k, v);
            keys.add(k);
        });
    }

    @Override
    public void close() {
        keys.forEach(MDC::remove);
        keys.clear();
    }
}
