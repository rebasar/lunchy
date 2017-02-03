package net.rebworks.lunchy.domain.places;

import net.rebworks.lunchy.domain.Place;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.resources.LunchResource;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import static net.rebworks.lunchy.util.Util.toUri;

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
        return toUri("https://www.tacosandtequila.se/");
    }

    @Override
    public URI getUri() {
        return uriInfo.getRequestUriBuilder()
                      .path(LunchResource.class, "getPlace")
                      .resolveTemplate("place", NAME)
                      .build();
    }

    @Override
    public LocalDateTime getExpiry(final LocalDateTime dateTime) {
        return new DateCalculator(dateTime.toLocalDate()).getEndOfWeek().plusDays(1).atStartOfDay();
    }
}
