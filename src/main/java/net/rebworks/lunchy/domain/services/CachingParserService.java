package net.rebworks.lunchy.domain.services;

import net.rebworks.lunchy.annotations.Cached;
import net.rebworks.lunchy.annotations.Default;
import net.rebworks.lunchy.domain.Lunches;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;

import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Cached
public class CachingParserService implements ParserService {

    private final ParserService delegate;
    private final Cache<String, Lunches> cache;

    // FIXME: Find a way to change the first parameter to ParserService.
    @Inject
    public CachingParserService(@Default final DefaultParserService delegate, final DefaultCacheManager cacheManager) {
        this.delegate = delegate;
        this.cache = getOrCreateCache(cacheManager);
    }

    private Cache<String, Lunches> getOrCreateCache(final DefaultCacheManager cacheManager) {
        if (cacheManager.cacheExists("lunches")) {
            return cacheManager.getCache("lunches");
        } else {
            return cacheManager.createCache("lunches", cacheManager.getDefaultCacheConfiguration());
        }
    }

    @Override
    public Lunches getLunches(final String name) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }
        final Lunches lunches = delegate.getLunches(name);
        if (lunches.exists()) {
            cache(name, lunches);
        }
        return lunches;
    }

    private void cache(final String name, final Lunches lunches) {
        final LocalDateTime now = LocalDateTime.now();
        final long expireMillis = Duration.between(now, lunches.getExpires()).toMillis();
        cache.put(name, lunches, expireMillis, TimeUnit.MILLISECONDS);
    }
}
