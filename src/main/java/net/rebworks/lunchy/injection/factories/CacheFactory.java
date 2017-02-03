package net.rebworks.lunchy.injection.factories;

import org.glassfish.hk2.api.Factory;
import org.infinispan.manager.DefaultCacheManager;

public class CacheFactory implements Factory<DefaultCacheManager> {

    private final DefaultCacheManager defaultCacheManager = new DefaultCacheManager();

    @Override
    public DefaultCacheManager provide() {
        return defaultCacheManager;
    }

    @Override
    public void dispose(final DefaultCacheManager cacheManager) {
        cacheManager.stop();
    }
}
