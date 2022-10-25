/**
 * 
 */
package com.nextlabs.destiny.plugin.engine;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nextlabs.destiny.plugin.engine.nodejs.NodejsScriptedEngine;
import com.nextlabs.destiny.plugin.model.ScriptEngineMetadata;

/**
 * @author kyu
 *
 */
public final class ScriptedEngineFactory {

    private static final Log log = LogFactory.getLog(ScriptedEngineFactory.class);

    private static final Map<ScriptEngineMetadata, ScriptedEngine> engineMap = new ConcurrentHashMap<>();

    private static final Map<String, ScriptedEngine> engineMapSource = new ConcurrentHashMap<>();

    /**
     * load ScriptedEngine implementation based on file extension name
     * 
     * @param f
     * @return
     */
    public static void loadScriptedEngine(File f) {
        if (f == null)
            throw new NullPointerException("File is required");
        if (f.isDirectory()) {
            log.error("File is a directory");
            return;
        }
        String fileName = f.getName();
        ScriptedEngine engine = null;
        int lastIndexOfDot = fileName.lastIndexOf(".");
        String extension = fileName.substring(lastIndexOfDot + 1).toLowerCase();
        if (log.isDebugEnabled()) {
            log.debug("File " + fileName + " has extension name " + extension);
        }

        long startTime = System.nanoTime();
        switch (extension) {
        case "js":
            try {
                engine = new NodejsScriptedEngine(f);
                if (log.isDebugEnabled()) {
                    log.debug("loaded NodejsScriptEngine from " + fileName);
                }
            } catch (Exception e) {
                log.error("error while creating NodejsScriptedEngine from file", e);
            }
            break;
        default:
            log.info("file [" + fileName + "] has no matched scriptEngine implementation yet");
            break;
        }
        if (null != engine) {
            long scriptLoaded = System.nanoTime();
            if (log.isDebugEnabled()) {
                log.debug("loading ScriptEngine takes " + (scriptLoaded - startTime) + " ns");
            }
            Collection<ScriptEngineMetadata> loadMetadata = engine.loadMetadata();
            if (loadMetadata != null && !loadMetadata.isEmpty()) {
                if (log.isDebugEnabled()) {
                    log.debug("loading metadata takes " + (System.nanoTime() - scriptLoaded) + " ns");
                }
                try {
                    engineMapSource.put(f.getCanonicalPath(), engine);
                } catch (IOException e) {
                    log.error("Error getting canonical path of file " + fileName, e);
                }
                for (ScriptEngineMetadata key : loadMetadata) {
                    engineMap.put(key, engine);
                }
            } else {
                log.info("no metadata loaded. will remove this engine");
                engine.remove();
            }
        }
    }

    public static ScriptedEngine findProperEngine(ScriptEngineMetadata key) {
        return engineMap.get(key);
    }

    /**
     * remove early loaded ScriptEngine when a file is removed
     * 
     * @param fileCanonicalPath
     */
    public static void delScriptedEngineOfFile(String fileCanonicalPath) {
        ScriptedEngine engine = engineMapSource.get(fileCanonicalPath);
        if (engine != null) {
            engine.remove();
        }
    }
}
