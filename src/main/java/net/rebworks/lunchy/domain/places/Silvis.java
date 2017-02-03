package net.rebworks.lunchy.domain.places;

import net.rebworks.lunchy.domain.Place;
import net.rebworks.lunchy.resources.LunchResource;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import static net.rebworks.lunchy.util.Util.toUri;

public class Silvis implements Place {

    private static final TreeSet<String> ALIASES = new TreeSet<>(Collections.singletonList("silvis"));
    public static final String NAME = ALIASES.first();
    private final UriInfo uriInfo;

    @Inject
    public Silvis(@Context final UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public String getName() {
        return "Silvis";
    }

    @Override
    public SortedSet<String> getAliases() {
        return ALIASES;
    }

    @Override
    public URI getWebsite() {
        return toUri("http://www.restaurangguiden.com/sv/silvis/lunch");
    }

    @Override
    public URI getUri() {
        return uriInfo.getRequestUriBuilder()
                      .path(LunchResource.class, "getPlace")
                      .resolveTemplate("place", NAME).build();
    }

}
