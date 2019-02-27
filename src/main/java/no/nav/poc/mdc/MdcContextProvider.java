package no.nav.poc.mdc;

import java.util.Map;
import java.util.Set;

public interface MdcContextProvider {

    Set<String> getMdcContextKeyset();
    Map<String, String> getMdcContextMap(Object[] args);

}
