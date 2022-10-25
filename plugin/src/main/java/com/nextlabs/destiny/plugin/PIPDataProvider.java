/**
 * 
 */
package com.nextlabs.destiny.plugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nextlabs.destiny.plugin.engine.ScriptedEngine;
import com.nextlabs.destiny.plugin.engine.ScriptedEngineFactory;
import com.nextlabs.destiny.plugin.model.EntityAttributeKey;
import com.nextlabs.destiny.plugin.model.ScriptEngineMetadata;

/**
 * @author kyu
 *
 */
public class PIPDataProvider {

    private static final Log log = LogFactory.getLog(PIPDataProvider.class);

    private boolean enableValueCache;

    private final EntityAttributeCacheProvider attrCacheProvider = new EntityAttributeCacheProvider();

    public void init(boolean enableValueCache) throws Exception {
        this.enableValueCache = enableValueCache;
    }

    public String getAttribute(String entityId, String entityType, String modelType, String attributeName) {
        EntityAttributeKey key = new EntityAttributeKey(entityType, modelType, attributeName, entityId);
        if (enableValueCache) {
            String cachedAttribute = this.attrCacheProvider.getCachedAttribute(key);
            if (cachedAttribute != null) {
                if (log.isDebugEnabled()) {
                    log.debug("cached attribute found for:" + key.toString());
                }
                return cachedAttribute;
            }
        }
        ScriptEngineMetadata metadata = new ScriptEngineMetadata(entityType, modelType, attributeName);
        ScriptedEngine engine = ScriptedEngineFactory.findProperEngine(metadata);
        if (engine == null) {
            log.info("no handler found for key: " + metadata);
            return null;
        }
        long startTime = System.nanoTime();
        String attributeValue = engine.getAttribute(entityId, entityType, modelType, attributeName);
        if (log.isDebugEnabled()) {
            log.debug("get attribute takes " + (System.nanoTime() - startTime) + " ns");
        }
        if (enableValueCache && attributeValue != null) {
            if (log.isDebugEnabled()) {
                log.debug("caching attribute found for:" + key.toString());
            }
            this.attrCacheProvider.cacheAttribute(key, attributeValue);
        }
        return attributeValue;
    }
}
