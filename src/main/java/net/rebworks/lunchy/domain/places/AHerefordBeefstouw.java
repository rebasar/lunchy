package net.rebworks.lunchy.domain.places;

import net.rebworks.lunchy.domain.Place;
import net.rebworks.lunchy.resources.LunchResource;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class AHerefordBeefstouw implements Place {

    private static final TreeSet<String> ALIASES = new TreeSet<>(Arrays.asList("ahb", "aherefordbeefstouw", "hereford", "beefstouw"));
    public static final String NAME = ALIASES.first();
    private final UriInfo uriInfo;

    @Inject
    public AHerefordBeefstouw(final UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public String getName() {
        return "A Hereford Beefstouw";
    }

    @Override
    public SortedSet<String> getAliases() {
        return ALIASES;
    }

    @Override
    public URI getWebsite() {
        return URI.create("http://a-h-b.se/goteborg-sverige/meny/lunch");
    }

    @Override
    public URI getUri() {
        return LunchResource.getPlaceURI(uriInfo, NAME);
    }
}
