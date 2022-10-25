package j2v8;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.utils.MemoryManager;

import io.apigee.trireme.core.NodeException;

public class J2V8ScriptEngine {
	static final V8 v8 = V8.createV8Runtime("global");
	static NodeJS nodeJS = null;
	static String pathToScripts;
	static long timeout = 5000; // default timeout is set to 1 sec
	static ConcurrentHashMap<String, V8Object> scriptMap = new ConcurrentHashMap<>();

	public J2V8ScriptEngine(String pathToScripts) throws NodeException, InterruptedException, ExecutionException {
		J2V8ScriptEngine.pathToScripts = pathToScripts;
		System.out.println("Start loading scripts**************");
		loadScripts(pathToScripts);
		System.out.println("Loading finished");
	}

	private void loadScripts(String pathToScripts) throws NodeException, InterruptedException, ExecutionException {
		File file = new File(pathToScripts);
		File[] scripts = file.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("j2v8") && name.endsWith(".js");
			}
		});

//		for (int i = 0; i < scripts.length; i++){
//			createNodeJSEngine(scripts[i].getName());
//			System.out.println("Loading script "+scripts[i].getName());
//		}
	}

	public J2V8ScriptEngine(String pathToScripts, long timeout) throws NodeException, InterruptedException, ExecutionException {
		J2V8ScriptEngine.pathToScripts = pathToScripts;
		J2V8ScriptEngine.timeout = timeout;
		System.out.println("Start loading scripts**************");
		loadScripts(pathToScripts);
		System.out.println("Loading finished");
	}

	private V8Object createNodeJSEngine(String scriptName) {
		long startTime = System.nanoTime();
		nodeJS = NodeJS.createNodeJS();
		//scope = new MemoryManager(v8);

		System.out.println("Engine creation time: " + (System.nanoTime() - startTime) / 1000000 + " ms., Script Name :"
				+ scriptName);
		startTime = System.nanoTime();
		String scriptToExecute = pathToScripts + File.separator + scriptName +".js";
		V8Object plugin = nodeJS.require(new File(scriptToExecute));
		System.out.println("Script loading time: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
		
		scriptMap.put(scriptName, plugin);
		
		return plugin;
	}

	@SuppressWarnings("unused")
	public synchronized String getAttributeValue(String scriptName, String entityType, String resourceType, String attributeId) {
		MemoryManager scope = null;
		final JSONObject attribute = new JSONObject();

		V8Object plugin = createNodeJSEngine(scriptName);
//		V8Object plugin = scriptMap.get(scriptName+".js");
		//System.out.println("plugin is "+plugin);
		long startTime = System.nanoTime();
		V8Function attributeCallback = new V8Function(nodeJS.getRuntime(), new JavaCallback() {
			public Object invoke(V8Object receiver, V8Array parameters) {
				System.out.println("NODEJS IS :::::: sucess  :::" + parameters);
				boolean sucess = (Boolean) parameters.get(0);
				if (sucess) {
					attribute.put("response", new JSONObject(parameters.get(1).toString()));
					System.out.println("NODEJS IS :::::: sucess  :::");
				}
				return parameters;
			}
		});

		// input to getMetadata function
		V8Array attributePara = new V8Array(nodeJS.getRuntime());
		attributePara.push(entityType);
		attributePara.push(resourceType);
		attributePara.push(attributeId);
		attributePara.push(attributeCallback);

		plugin.executeFunction("getAttributeValue", attributePara);

		final AtomicBoolean isTimeout = new AtomicBoolean(false);
		
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				isTimeout.set(true);
			}
		}, timeout);
		
		while (nodeJS.isRunning() && !isTimeout.get()) {
			nodeJS.handleMessage();
		}

		if (scope != null && !scope.isReleased()) {
			scope.release();
		}

		nodeJS = null; // reset to null otherwise will get exception
		timer.cancel();
		JSONObject attributeResponse = (attribute.has("response"))? attribute.getJSONObject("response") : new JSONObject();
		System.out.println("Method execution and callback time: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
		System.out.println(attributeResponse);
		return attributeResponse.toString();
	}

	@SuppressWarnings("unused")
	public String getMetadata(String scriptName, String resourceType) {
		MemoryManager scope = null;
		final JSONObject metadata = new JSONObject();
		//V8Object plugin = createNodeJSEngine(scope, scriptName);
		V8Object plugin = scriptMap.get(scriptName+".js");
		
		long startTime = System.nanoTime();
		V8Function metadataCallback = new V8Function(nodeJS.getRuntime(), new JavaCallback() {
			public Object invoke(V8Object receiver, V8Array parameters) {
				boolean sucess = (Boolean) parameters.get(0);
				if (sucess) {
					metadata.put("response", new JSONObject(parameters.get(1).toString()));
				}
				return parameters;
			}
		});

		// input to getMetadata function
		V8Array metadataPara = new V8Array(nodeJS.getRuntime());
		metadataPara.push(resourceType);
		metadataPara.push(metadataCallback); // callback function
		plugin.executeFunction("getMetadata", metadataPara);

		final AtomicBoolean isTimeout = new AtomicBoolean(false);
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				isTimeout.set(true);
			}
		}, timeout);

		while (nodeJS.isRunning() && !isTimeout.get()) {
			nodeJS.handleMessage();
		}

		if (scope != null && !scope.isReleased()) {
			scope.release();
		}

		nodeJS = null;
		timer.cancel();

		JSONObject metadataResponse = metadata.getJSONObject("response");
		System.out.println("Method execution and callback time: " + (System.nanoTime() - startTime) / 1000000 + " ms.");

		return metadataResponse.toString();
	}
}
