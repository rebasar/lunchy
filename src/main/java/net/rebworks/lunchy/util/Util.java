package net.rebworks.lunchy.util;

import java.net.URI;
import java.net.URISyntaxException;

public class Util {

    public static URI toUri(final String uri) {
        try {
            return new URI(uri);
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
