package no.nav.poc.mdc;

import java.util.Map;

public interface MdcContextProvider {

    Map<String, String> getMdcContextMap();

}
