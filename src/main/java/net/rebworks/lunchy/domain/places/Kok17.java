package net.rebworks.lunchy.domain.places;

import net.rebworks.lunchy.domain.Place;
import net.rebworks.lunchy.resources.LunchResource;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class Kok17 implements Place {

    private static final TreeSet<String> ALIASES = new TreeSet<>(Arrays.asList("kök17", "kok17", "sjutton"));
    public static final String NAME = ALIASES.first();

    private final UriInfo uriInfo;

    @Inject
    public Kok17(final UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public String getName() {
        return "Kök 17";
    }

    @Override
    public SortedSet<String> getAliases() {
        return ALIASES;
    }

    @Override
    public URI getWebsite() {
        return URI.create("http://kok17.se/meny/");
    }

    @Override
    public URI getUri() {
        return LunchResource.getPlaceURI(uriInfo, NAME);
    }
}
