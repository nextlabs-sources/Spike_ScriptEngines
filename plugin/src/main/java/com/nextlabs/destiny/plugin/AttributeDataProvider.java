package com.nextlabs.destiny.plugin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bluejungle.framework.expressions.EvalValue;
import com.bluejungle.framework.expressions.IEvalValue;
import com.bluejungle.framework.expressions.ValueType;
import com.bluejungle.pf.domain.destiny.serviceprovider.IHeartbeatServiceProvider;
import com.bluejungle.pf.domain.destiny.serviceprovider.IResourceAttributeProvider;
import com.bluejungle.pf.domain.destiny.serviceprovider.ISubjectAttributeProvider;
import com.bluejungle.pf.domain.destiny.serviceprovider.ServiceProviderException;
import com.bluejungle.pf.domain.destiny.subject.IDSubject;
import com.bluejungle.pf.domain.epicenter.resource.IResource;
import com.nextlabs.destiny.plugin.engine.ScriptedEngineFactory;
import com.nextlabs.pf.domain.destiny.serviceprovider.IConfigurableServiceProvider;

public class AttributeDataProvider implements IResourceAttributeProvider, ISubjectAttributeProvider, IHeartbeatServiceProvider, IConfigurableServiceProvider {

    private static final Log log = LogFactory.getLog(AttributeDataProvider.class);

    PIPDataProvider provider = new PIPDataProvider();

    private Properties properties;

    @Override
    public void init() throws Exception {
        log.info("initializing script engine plugin");
        provider.init(Boolean.valueOf(properties.getProperty("enablecache", "false")));
        if ("local".equalsIgnoreCase(properties.getProperty("script_mode"))) {
            String scriptFolder = properties.getProperty("script_folder");
            final File scriptDir = new File(scriptFolder);
            log.warn("plugin is running in local testing mode, would load script from fixed folder: " + scriptDir.getAbsolutePath());

            for (File file : scriptDir.listFiles()) {
                ScriptedEngineFactory.loadScriptedEngine(file);
            }

            Thread fileWatcher = new Thread() {
                public void run() {
                    try {
                        WatchService watcher = FileSystems.getDefault().newWatchService();
                        scriptDir.toPath().register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                                StandardWatchEventKinds.ENTRY_MODIFY);
                        log.info("watching file change in directory " + scriptDir);
                        while (true) {
                            try {
                                WatchKey key = watcher.take();
                                log.info("File change detected");
                                try {
                                    List<WatchEvent<?>> events = key.pollEvents();
                                    for (WatchEvent<?> e : events) {
                                        Kind<?> kind = e.kind();
                                        String path = ((Path) e.context()).toString();
                                        log.info("File " + path + " changed. Change type is " + kind);
                                        if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                                            ScriptedEngineFactory.delScriptedEngineOfFile(new File(scriptDir, path).getCanonicalPath());
                                        } else if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                                            ScriptedEngineFactory.loadScriptedEngine(new File(scriptDir, path));
                                        } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                                            ScriptedEngineFactory.delScriptedEngineOfFile(new File(scriptDir, path).getCanonicalPath());
                                            ScriptedEngineFactory.loadScriptedEngine(new File(scriptDir, path));
                                        }
                                    }
                                } catch (Exception e) {
                                    log.error("Exception while watching file", e);
                                }
                                key.reset();
                            } catch (InterruptedException x) {
                                log.error("Interrupted", x);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
            };
            fileWatcher.setName("SCRIPT-FILE-WATCHER");
            fileWatcher.start();
        }
    }

    @Override
    public IEvalValue getAttribute(IDSubject paramIDSubject, String paramString) throws ServiceProviderException {
        // TODO Auto-generated method stub
        return getAttribute(paramIDSubject.getUid(), "SUBJECT", "user", paramString);
    }

    @Override
    public IEvalValue getAttribute(IResource paramIResource, String paramString) throws ServiceProviderException {
        // TODO Auto-generated method stub
        // How to get policy model type?
        String policyModel = null;
        for (Entry<String, IEvalValue> entry : paramIResource.getEntrySet()) {
            if ("ce::destinytype".equals(entry.getKey()) && ValueType.STRING.equals(entry.getValue().getType())) {
                policyModel = (String) entry.getValue().getValue();
            }
        }
        if (policyModel == null) {
            log.error("policy model is not available");
            return null;
        }
        return getAttribute(String.valueOf(paramIResource.getIdentifier()), "RESOURCE", policyModel, paramString);
    }

    private IEvalValue getAttribute(String entityId, String entityType, String modelType, String attributeName) {
        log.debug("***************************************************************");
        if (log.isDebugEnabled()) {
            log.debug("get attribute [" + attributeName + "] for entity [" + entityId + " ] of type [" + entityType + "], model type [" + modelType + "]");
        }
        long startTime = System.nanoTime();
        String attributeVal = provider.getAttribute(entityId, entityType, modelType, attributeName);
        if (log.isDebugEnabled() && attributeVal == null) {
            log.debug("attribute not found");
        }
        if (log.isDebugEnabled()) {
            log.debug("it takes " + (System.nanoTime() - startTime) / 1000000 + " ms to get attribute");
            log.debug("***************************************************************");
        }
        return EvalValue.build(attributeVal);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bluejungle.framework.heartbeat.IHeartbeatListener#prepareRequest(java
     * .lang.String)
     */
    @Override
    public Serializable prepareRequest(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bluejungle.framework.heartbeat.IHeartbeatListener#processResponse(
     * java.lang.String, java.lang.String)
     */
    @Override
    public void processResponse(String arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nextlabs.pf.domain.destiny.serviceprovider.
     * IConfigurableServiceProvider#setProperties(java.util.Properties)
     */
    @Override
    public void setProperties(Properties paramProperties) {
        this.properties = paramProperties;
    }

}
