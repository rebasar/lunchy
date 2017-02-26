package net.rebworks.lunchy.injection;

import net.rebworks.lunchy.annotations.Cached;
import net.rebworks.lunchy.annotations.Default;
import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.Place;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.domain.io.HttpClient;
import net.rebworks.lunchy.domain.places.BangkokKitchen;
import net.rebworks.lunchy.domain.places.Barabicu;
import net.rebworks.lunchy.domain.places.BynsBistro;
import net.rebworks.lunchy.domain.places.Silvis;
import net.rebworks.lunchy.domain.places.TacosAndTequila;
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
        bind(net.rebworks.lunchy.domain.parsers.BynsBistro.class).named(BynsBistro.NAME).to(Parser.class);
        bind(net.rebworks.lunchy.domain.parsers.BangkokKitchen.class).named(BangkokKitchen.NAME).to(Parser.class);
    }

    private void bindPlaces() {
        bind(Barabicu.class).named(Barabicu.NAME).to(Place.class);
        bind(TacosAndTequila.class).named(TacosAndTequila.NAME).to(Place.class);
        bind(Silvis.class).named(Silvis.NAME).to(Place.class);
        bind(BynsBistro.class).named(BynsBistro.NAME).to(Place.class);
        bind(BangkokKitchen.class).named(BangkokKitchen.NAME).to(Place.class);
    }
}
