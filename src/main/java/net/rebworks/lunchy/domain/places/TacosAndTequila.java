package net.rebworks.lunchy.domain.places;

import net.rebworks.lunchy.domain.Place;
import net.rebworks.lunchy.resources.LunchResource;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class TacosAndTequila implements Place {

    private static final TreeSet<String> ALIASES = new TreeSet<>(Arrays.asList("tnt",
                                                                               "tacos",
                                                                               "tacosntequila",
                                                                               "tacosandtequila"));
    public static final String NAME = ALIASES.first();
    private final UriInfo uriInfo;

    @Inject
    public TacosAndTequila(final UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public String getName() {
        return "Tacos & Tequila";
    }

    @Override
    public SortedSet<String> getAliases() {
        return ALIASES;
    }

    @Override
    public URI getWebsite() {
        return URI.create("https://www.tacosandtequila.se/");
    }

    @Override
    public URI getUri() {
        return LunchResource.getPlaceURI(uriInfo, NAME);
    }

    @Override
    public LocalDateTime getExpiry(final LocalDateTime dateTime) {
        // Tacos seems be updating their menu on Monday morning
        return Place.super.getExpiry(dateTime).plusHours(8);
    }
}
