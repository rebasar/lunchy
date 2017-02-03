package net.rebworks.lunchy.domain.places;

import net.rebworks.lunchy.domain.Place;
import net.rebworks.lunchy.resources.LunchResource;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import static net.rebworks.lunchy.util.Util.toUri;

public class Barabicu implements Place {

    public static final String NAME = "barabicu";

    private static final TreeSet<String> ALIASES = new TreeSet<>(Arrays.asList("barabicu", "barran", "brb"));
    private final UriInfo uriInfo;

    @Inject
    public Barabicu(@Context final UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public String getName() {
        return "Barabicu";
    }

    @Override
    public SortedSet<String> getAliases() {
        return ALIASES;
    }

    @Override
    public URI getWebsite() {
        return toUri("http://barabicu.se");
    }

    @Override
    public URI getUri() {
        return uriInfo.getRequestUriBuilder()
                      .path(LunchResource.class, "getPlace")
                      .resolveTemplate("place", NAME)
                      .build();
    }

}
