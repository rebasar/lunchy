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

public class Aptigarden implements Place {

    public static final String NAME = "Aptigarden";
    private static final TreeSet<String> ALIASES = new TreeSet<>(Arrays.asList("Aptigarden", "aptigarden", "apg"));
    private final UriInfo uriInfo;

    @Inject
    public Aptigarden(@Context final UriInfo uriInfo) {
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
        return URI.create("http://aptitgarden.com/sv/index.php/lunchmeny-3");
    }

    @Override
    public URI getUri() {
        return LunchResource.getPlaceURI(uriInfo, NAME);
    }
}
