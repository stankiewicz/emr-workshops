package pl.pwlsltsk.workshop.util;

import java.util.UUID;

/**
 * @author pwlsltsk
 */
public final class Names {

    private Names() {}

    public static String generateClusterName() {

        return String.format("cluster-%s", String.valueOf(UUID.randomUUID()));
    }
}
