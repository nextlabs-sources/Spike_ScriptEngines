/**
 * 
 */
package com.nextlabs.destiny.plugin;

import java.util.HashMap;
import java.util.Map;

import com.nextlabs.destiny.plugin.model.EntityAttributeKey;

/**
 * @author kyu
 *
 */
public class EntityAttributeCacheProvider {
    private Map<EntityAttributeKey, String> entityAttributeValueCache = new HashMap<>();

    public String getCachedAttribute(EntityAttributeKey key) {
        return entityAttributeValueCache.get(key);
    }

    public void cacheAttribute(EntityAttributeKey key, String value) {
        entityAttributeValueCache.put(key, value);
    }
}
