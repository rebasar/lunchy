package net.rebworks.lunchy.injection;

import net.rebworks.lunchy.annotations.Cached;
import net.rebworks.lunchy.annotations.Default;
import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.Place;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.domain.io.HttpClient;
import net.rebworks.lunchy.domain.parsers.util.SwedishTitleDescriptionSplitter;
import net.rebworks.lunchy.domain.places.*;
import net.rebworks.lunchy.domain.services.CachingParserService;
import net.rebworks.lunchy.domain.services.DefaultParserService;
import net.rebworks.lunchy.domain.services.ParserService;
import net.rebworks.lunchy.injection.factories.CacheFactory;
import net.rebworks.lunchy.injection.factories.DateCalculatorFactory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.infinispan.manager.DefaultCacheManager;

public class LunchyBinder extends AbstractBinder {

    private final HttpClient httpClient;

    public LunchyBinder(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    protected void configure() {
        bind(httpClient).to(HttpClient.class);
        bind(new SwedishTitleDescriptionSplitter()).to(SwedishTitleDescriptionSplitter.class);
        bindFactories();
        bindParserService();
        bindPlaces();
        bindParsers();
    }

    private void bindFactories() {
        bindFactory(new DateCalculatorFactory()).to(DateCalculator.class);
        bindFactory(new CacheFactory()).to(DefaultCacheManager.class);
    }

    private void bindParserService() {
        bind(CachingParserService.class).qualifiedBy(CachingParserService.class.getAnnotation(Cached.class))
                                        .to(ParserService.class);
        bind(DefaultParserService.class).qualifiedBy(DefaultParserService.class.getAnnotation(Default.class))
                                        .to(ParserService.class);
        bind(DefaultParserService.class).to(DefaultParserService.class);
    }

    private void bindParsers() {
        bind(net.rebworks.lunchy.domain.parsers.Barabicu.class).named(Barabicu.NAME).to(Parser.class);
        bind(net.rebworks.lunchy.domain.parsers.TacosAndTequila.class).named(TacosAndTequila.NAME).to(Parser.class);
        bind(net.rebworks.lunchy.domain.parsers.Silvis.class).named(Silvis.NAME).to(Parser.class);
        bind(net.rebworks.lunchy.domain.parsers.BangkokKitchen.class).named(BangkokKitchen.NAME).to(Parser.class);
        bind(net.rebworks.lunchy.domain.parsers.Kok17.class).named(Kok17.NAME).to(Parser.class);
        bind(net.rebworks.lunchy.domain.parsers.AHerefordBeefstouw.class).named(AHerefordBeefstouw.NAME)
                                                                         .to(Parser.class);
        bind(net.rebworks.lunchy.domain.parsers.Hildas.class).named(Hildas.NAME).to(Parser.class);
        bind(net.rebworks.lunchy.domain.parsers.Aptigarden.class).named(Aptigarden.NAME).to(Parser.class);
        bind(net.rebworks.lunchy.domain.parsers.Vallagat.class).named(Vallagat.NAME).to(Parser.class);
    }

    private void bindPlaces() {
        bind(Barabicu.class).named(Barabicu.NAME).to(Place.class);
        bind(TacosAndTequila.class).named(TacosAndTequila.NAME).to(Place.class);
        bind(Silvis.class).named(Silvis.NAME).to(Place.class);
        bind(BangkokKitchen.class).named(BangkokKitchen.NAME).to(Place.class);
        bind(Kok17.class).named(Kok17.NAME).to(Place.class);
        bind(AHerefordBeefstouw.class).named(AHerefordBeefstouw.NAME).to(Place.class);
        bind(Hildas.class).named(Hildas.NAME).to(Place.class);
        bind(Aptigarden.class).named(Aptigarden.NAME).to(Place.class);
        bind(Vallagat.class).named(Vallagat.NAME).to(Place.class);
    }
}
