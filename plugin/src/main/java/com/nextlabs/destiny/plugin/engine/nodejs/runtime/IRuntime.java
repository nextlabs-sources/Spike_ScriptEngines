/**
 * 
 */
package com.nextlabs.destiny.plugin.engine.nodejs.runtime;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import com.nextlabs.destiny.plugin.model.ScriptEngineMetadata;

/**
 * @author kyu
 *
 */
public interface IRuntime {

    /**
     * @param entityType
     * @param modelType
     * @param attributeName
     * @param entityId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NodeException
     */
    String getAttributeValue(String entityType, String modelType, String attributeName, String entityId) throws InterruptedException, ExecutionException;

    Collection<ScriptEngineMetadata> loadMetadata();

    /**
     * 
     */
    void remove();

}
