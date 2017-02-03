package net.rebworks.lunchy.domain.services;

import net.rebworks.lunchy.annotations.Default;
import net.rebworks.lunchy.domain.Lunches;
import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.Place;
import net.rebworks.lunchy.domain.io.HttpClient;
import net.rebworks.lunchy.dto.Lunch;
import org.glassfish.hk2.api.IterableProvider;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Default
public class DefaultParserService implements ParserService {

    private final HttpClient httpClient;
    private final IterableProvider<Place> placeProvider;
    private final IterableProvider<Parser> parserProvider;

    @Inject
    public DefaultParserService(final HttpClient httpClient, final IterableProvider<Place> placeProvider, final IterableProvider<Parser> parserProvider) {
        this.httpClient = httpClient;
        this.placeProvider = placeProvider;
        this.parserProvider = parserProvider;
    }

    @Override
    public Lunches getLunches(final String name) {
        final Place place = placeProvider.named(name).get();
        final Parser parser = parserProvider.named(name).get();
        if (place == null || parser == null) {
            return Lunches.empty();
        }
        return httpClient.getPage(place)
                         .map(parser::parse)
                         .map(lunches -> toLunches(place, lunches))
                         .orElse(Lunches.empty());
    }

    private Lunches toLunches(final Place place, final List<Lunch> lunches) {
        return Lunches.builder().expires(place.getExpiry(LocalDateTime.now())).addAllLunches(lunches).build();
    }
}
