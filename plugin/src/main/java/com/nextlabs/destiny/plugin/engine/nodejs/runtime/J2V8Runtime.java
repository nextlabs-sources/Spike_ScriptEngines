/**
 * 
 */
package com.nextlabs.destiny.plugin.engine.nodejs.runtime;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.nextlabs.destiny.plugin.model.ScriptEngineMetadata;

/**
 * @author kyu
 *
 */
public class J2V8Runtime implements IRuntime {

    private static final Log log = LogFactory.getLog(J2V8Runtime.class);

    private V8ExecutorThread executor;

    // private MemoryManager memoryManager;

    public static J2V8Runtime getInstance(File file) throws InterruptedException, ExecutionException, IOException {
        return new J2V8Runtime(file);
    }

    private J2V8Runtime(File file) {
        super();
        // memoryManager = new MemoryManager(nodeJS.getRuntime());
        this.executor = new V8ExecutorThread(file);
        this.executor.setDaemon(true);
        this.executor.setName("ScriptRunner-" + file.getName());
        this.executor.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nextlabs.destiny.plugin.engine.nodejs.runtime.IRuntime#
     * getAttributeValue(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getAttributeValue(String entityType, String modelType, String attributeName, String entityId)
            throws InterruptedException, ExecutionException {
        // TODO Auto-generated method stub
        final StringBuilder respBuilder = new StringBuilder();
        final AtomicBoolean hasResult = new AtomicBoolean(false);
        JavaCallbackLite javaCallback = new JavaCallbackLite() {
            public void invoke(Object[] parameters) {
                if ((Boolean) parameters[0]) {
                    if (log.isDebugEnabled()) {
                        log.debug("loaded attribute value:" + parameters[1]);
                    }
                    respBuilder.append(parameters[1].toString());
                }
                hasResult.set(true);
            }
        };

        if (!this.executor.submitTask(new GetAttributeValueTask(entityType, modelType, attributeName, entityId, javaCallback))) {
            return null;
        }
        while (!hasResult.get())
            ; // infinite loop waiting for response

        if (respBuilder.length() > 0) {
            final JSONObject result = new JSONObject(respBuilder.toString());
            return String.valueOf(result.get("attributeValue"));
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.nextlabs.destiny.plugin.engine.nodejs.runtime.IRuntime#loadMetadata()
     */
    @Override
    public Collection<ScriptEngineMetadata> loadMetadata() {
        final AtomicBoolean hasResult = new AtomicBoolean(false);
        final StringBuilder respBuilder = new StringBuilder();
        JavaCallbackLite javaCallback = new JavaCallbackLite() {
            public void invoke(Object[] parameters) {
                if ((Boolean) parameters[0]) {
                    if (log.isDebugEnabled()) {
                        log.debug("loaded metadata:" + parameters[1]);
                    }
                    respBuilder.append(parameters[1].toString());
                }
                hasResult.set(true);
            }
        };
        Set<ScriptEngineMetadata> handleKey = new HashSet<>();
        if (!this.executor.submitTask(new LoadMetadataTask(javaCallback))) {
            return handleKey;
        }
        while (!hasResult.get())
            ; // infinite loop waiting for response

        if (respBuilder.length() > 0) {
            JSONArray metadata = new JSONArray(respBuilder.toString());
            Iterator<Object> iterator = metadata.iterator();
            while (iterator.hasNext()) {
                JSONObject obj = (JSONObject) iterator.next();
                String entityType = (String) obj.get("entityType");
                String modelType = (String) obj.get("modelType");
                JSONArray modelTypeAttr = (JSONArray) obj.get("attr");
                Iterator<Object> modelTypeAttrIterator = modelTypeAttr.iterator();
                while (modelTypeAttrIterator.hasNext()) {
                    JSONObject modelTypeAttrMeta = (JSONObject) modelTypeAttrIterator.next();
                    String attrName = (String) modelTypeAttrMeta.get("name");
                    // long ttl = 0;
                    // if (modelTypeAttrMeta.has("ttl")) {
                    // ttl = (long) modelTypeAttrMeta.get("ttl");
                    // }
                    ScriptEngineMetadata key = new ScriptEngineMetadata(entityType, modelType, attrName);
                    if (log.isDebugEnabled()) {
                        log.debug("key " + key + " would be handled by this engine: " + this);
                    }
                    handleKey.add(key);
                }
            }
        }
        return handleKey;
    }

    private interface JavaCallbackLite {
        void invoke(Object[] response);
    }

    private abstract class J2V8Task {

        /**
         * @return
         */
        public abstract JavaCallbackLite getJavaCallback();

        /**
         * @return
         */
        public abstract String getFunctionName();

    }

    private final class LoadMetadataTask extends J2V8Task {

        private JavaCallbackLite javaCallback;

        public LoadMetadataTask(JavaCallbackLite javaCallback) {
            super();
            this.javaCallback = javaCallback;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.nextlabs.destiny.plugin.engine.nodejs.runtime.J2V8Runtime.
         * J2V8Task#getJavaCallback()
         */
        @Override
        public JavaCallbackLite getJavaCallback() {
            return this.javaCallback;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.nextlabs.destiny.plugin.engine.nodejs.runtime.J2V8Runtime.
         * J2V8Task#getFunctionName()
         */
        @Override
        public String getFunctionName() {
            return "getMetadata";
        }

    }

    private final class GetAttributeValueTask extends J2V8Task {

        private JavaCallbackLite javaCallback;

        private String entityType;
        private String modelType;
        private String attributeName;
        private String entityId;

        public GetAttributeValueTask(String entityType, String modelType, String attributeName, String entityId, JavaCallbackLite javaCallback) {
            super();
            this.entityType = entityType;
            this.modelType = modelType;
            this.attributeName = attributeName;
            this.entityId = entityId;
            this.javaCallback = javaCallback;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.nextlabs.destiny.plugin.engine.nodejs.runtime.J2V8Runtime.
         * J2V8Task#getJavaCallback()
         */
        @Override
        public JavaCallbackLite getJavaCallback() {
            return this.javaCallback;
        }

        /**
         * @return the entityType
         */
        public String getEntityType() {
            return entityType;
        }

        /**
         * @return the modelType
         */
        public String getModelType() {
            return modelType;
        }

        /**
         * @return the attributeName
         */
        public String getAttributeName() {
            return attributeName;
        }

        /**
         * @return the entityId
         */
        public String getEntityId() {
            return entityId;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.nextlabs.destiny.plugin.engine.nodejs.runtime.J2V8Runtime.
         * J2V8Task#getFunctionName()
         */
        @Override
        public String getFunctionName() {
            return "getAttributeValue";
        }

    }

    private class V8ExecutorThread extends Thread {
        private V8Object engine;

        private final Queue<J2V8Task> taskQueue = new LinkedBlockingQueue<>();

        private volatile boolean shuttingDown = false;

        private NodeJS nodeJS = null;

        private File jsFile;

        /**
         * @param file
         */
        public V8ExecutorThread(File file) {
            // TODO Auto-generated constructor stub
            this.jsFile = file;

        }

        public boolean submitTask(J2V8Task task) {
            if (shuttingDown) {
                log.error("executor is already shutting down or shut down");
                return false;
            }
            boolean willNotify = taskQueue.isEmpty();
            taskQueue.add(task);
            if (willNotify) {
                synchronized (this) {
                    notify();
                }
            }
            return true;
        }

        public void shutdown() {
            this.shuttingDown = true;
        }

        public void run() {
            try {
                nodeJS = NodeJS.createNodeJS();
                this.engine = nodeJS.require(this.jsFile);
            } catch (Exception e) {
                log.error("Error with creating runtime, there might be another process running on the library", e);
                return;
            }

            while (true) {
                synchronized (this) {
                    while (taskQueue.isEmpty() && !shuttingDown) {
                        if (taskQueue.isEmpty() && !shuttingDown) {
                            try {
                                wait(10000);
                            } catch (InterruptedException e) {
                                log.error("Interrupted", e);
                            }
                        }
                    }
                    if ((taskQueue.isEmpty() || shuttingDown)) {
                        log.info("executor is shutting down");
                        return;
                    }
                    V8Function callback = null;
                    V8Array parameters = null;
                    final J2V8Task j2v8Task = taskQueue.poll();
                    final AtomicBoolean hasResult = new AtomicBoolean(false);
                    JavaCallback javaCallbackWrapper = new JavaCallback() {
                        public Object invoke(V8Object receiver, V8Array parameters) {
                            Object[] response = new Object[parameters.length()];
                            for (int i = 0; i < parameters.length(); i++) {
                                response[i] = parameters.get(i);
                            }
                            j2v8Task.getJavaCallback().invoke(response);
                            hasResult.set(true);
                            return parameters;
                        }
                    };
                    callback = new V8Function(nodeJS.getRuntime(), javaCallbackWrapper);
                    parameters = new V8Array(nodeJS.getRuntime());
                    if (j2v8Task instanceof GetAttributeValueTask) {
                        GetAttributeValueTask getAttributeValueTask = (GetAttributeValueTask) j2v8Task;
                        parameters.push(getAttributeValueTask.getEntityType());
                        parameters.push(getAttributeValueTask.getModelType());
                        parameters.push(getAttributeValueTask.getAttributeName());
                        parameters.push(getAttributeValueTask.getEntityId());
                    }
                    parameters.push(callback);

                    try {
                        engine.executeFunction(j2v8Task.getFunctionName(), parameters);
                    } catch (Exception e) {
                        hasResult.set(true);
                        j2v8Task.getJavaCallback().invoke(new Object[] { false });
                        log.error("Exception while executing script", e);
                    }

                    final AtomicBoolean isTimeout = new AtomicBoolean(false);
                    Timer timer = new Timer(true);
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isTimeout.set(true);
                        }
                    }, 10000);

                    while (!hasResult.get() && !isTimeout.get()) {
                        nodeJS.handleMessage();
                    }
                    timer.cancel();
                    callback.release();
                    parameters.release();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nextlabs.destiny.plugin.engine.nodejs.runtime.IRuntime#remove()
     */
    @Override
    public void remove() {
        this.executor.shutdown();
    }
}
