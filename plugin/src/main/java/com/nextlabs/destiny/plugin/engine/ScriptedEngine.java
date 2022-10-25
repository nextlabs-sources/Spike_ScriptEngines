package com.nextlabs.destiny.plugin.engine;

import java.util.Collection;

import com.nextlabs.destiny.plugin.model.ScriptEngineMetadata;

/**
 * @author kyu
 *
 */
public interface ScriptedEngine {
    void init() throws Exception;

    Collection<ScriptEngineMetadata> loadMetadata();

    String getAttribute(String entityId, String entityType, String modelType, String attributeName);

    /**
     * do cleaning work here 
     */
    void remove();
}
