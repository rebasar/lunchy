package net.rebworks.lunchy.resources;

import net.rebworks.lunchy.annotations.Cached;
import net.rebworks.lunchy.domain.Place;
import net.rebworks.lunchy.domain.services.ParserService;
import net.rebworks.lunchy.dto.Lunch;
import org.glassfish.hk2.api.IterableProvider;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class LunchResource {

    private final ParserService parserService;
    private final IterableProvider<Place> provider;

    @Inject
    public LunchResource(@Cached final ParserService parserService, final IterableProvider<Place> placeProvider) {
        this.parserService = parserService;
        this.provider = placeProvider;
    }

    @GET
    public List<Place> getPlaces() {
        return StreamSupport.stream(Spliterators.spliterator(provider.iterator(),
                                                             provider.getSize(),
                                                             Spliterator.SIZED), false).collect(toList());
    }

    @GET
    @Path("{place}")
    public Optional<Lunch> getPlace(@PathParam("place") final String place) {
        return parserService.getLunches(place).forDate(LocalDate.now());
    }

    public static URI getPlaceURI(final UriInfo uriInfo, final String place) {
        return uriInfo.getRequestUriBuilder()
                      .path(LunchResource.class, "getPlace")
                      .resolveTemplate("place", place)
                      .build();
    }
}
