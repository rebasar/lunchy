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

public class Vallagat implements Place {

    public static final String NAME = "Vallagat";
    private static final TreeSet<String> ALIASES = new TreeSet<>(Arrays.asList("Vallagat", "vallagat", "valagat"));
    private final UriInfo uriInfo;

    @Inject
    public Vallagat(@Context final UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public SortedSet<String> getAliases() {
        return ALIASES;
    }

    @Override
    public URI getWebsite() {
        return URI.create("http://www.vallagat.se/lunchmeny/");
    }

    @Override
    public URI getUri() {
        return LunchResource.getPlaceURI(uriInfo, NAME);
    }
}
