/**
 * 
 */
package com.nextlabs.destiny.plugin.engine.nodejs;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nextlabs.destiny.plugin.engine.ScriptedEngine;
import com.nextlabs.destiny.plugin.engine.nodejs.runtime.IRuntime;
import com.nextlabs.destiny.plugin.engine.nodejs.runtime.J2V8Runtime;
import com.nextlabs.destiny.plugin.model.ScriptEngineMetadata;

/**
 * @author kyu
 *
 */
public class NodejsScriptedEngine implements ScriptedEngine {

    private static final Log log = LogFactory.getLog(NodejsScriptedEngine.class);

    private IRuntime runtime;
    // private TriremeReference runtime;

    public NodejsScriptedEngine(File file) throws Exception {
        super();
        this.runtime = createNodeScript(file);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nextlabs.destiny.plugin.engine.ScriptedEngine#init()
     */
    @Override
    public void init() throws Exception {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.nextlabs.destiny.plugin.engine.ScriptedEngine#getAttribute(java.lang.
     * String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String getAttribute(String entityId, String entityType, String modelType, String attributeName) {
        String cachedAttribute = getCachedAttribute(entityType, modelType, attributeName, entityId);
        if (cachedAttribute == null) {
            try {
                String attributeValue = runtime.getAttributeValue(entityType, modelType, attributeName, entityId);
                return attributeValue;
            } catch (Exception e) {
                log.error("Exception while getting NodeJs runtime", e);
                return null;
            }
        } else {
            return cachedAttribute;
        }
    }

    private String getCachedAttribute(String entityType, String modelType, String attributeName, String entityId) {
        // TODO get attribute from some cache
        return null;
    }

    /**
     * @param scriptToExecute
     * @return
     * @throws NodeException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private IRuntime createNodeScript(File file) throws Exception {
        try {
            return J2V8Runtime.getInstance(file);
        } catch (IOException e) {
            log.error("IOException", e);
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nextlabs.destiny.plugin.engine.ScriptedEngine#loadMetadata()
     */
    @Override
    public Collection<ScriptEngineMetadata> loadMetadata() {
        return runtime.loadMetadata();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nextlabs.destiny.plugin.engine.ScriptedEngine#remove()
     */
    @Override
    public void remove() {
        runtime.remove();
    }

}
