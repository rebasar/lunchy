package net.rebworks.lunchy.domain.io;

import net.rebworks.lunchy.domain.Place;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import java.util.Optional;

public class HttpClient {
    private final Client client;

    public HttpClient(final Client client) {
        this.client = client;
    }

    public Optional<String> getPage(final Place place) {
        try {
            return Optional.ofNullable(client.target(place.getWebsite()).request().get(String.class));
        } catch (final ClientErrorException ignored) {
            return Optional.empty();
        }
    }
}
