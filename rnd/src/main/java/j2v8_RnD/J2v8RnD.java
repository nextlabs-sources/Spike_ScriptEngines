package j2v8_RnD;

import java.io.File;
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

public class J2v8RnD {

//	 static NodeJS nodeJS = NodeJS.createNodeJS();
	static String pathToScripts;
	static long timeout = 1000; // default timeout is set to 1 sec
	//static V8 v8 = V8.createV8Runtime("global");
	//static MemoryManager scope = new MemoryManager(v8);
	static ConcurrentHashMap<String, V8Object> scriptMap = new ConcurrentHashMap<String, V8Object>();
	static ConcurrentHashMap<String, NodeJS> runtimeMap = new ConcurrentHashMap<String, NodeJS>();

	static ConcurrentHashMap<String, V8Object> scriptMap2 = new ConcurrentHashMap<String, V8Object>();
	static ConcurrentHashMap<String, NodeJS> runtimeMap2 = new ConcurrentHashMap<String, NodeJS>();
	// static MemoryManager scope = null;
	// static V8Object engine = createNodeJSEngine(scope,
	// "j2v8_request_return_2_attributes_0.js");

	public J2v8RnD(String pathToScripts) {
		J2v8RnD.pathToScripts = pathToScripts;
	}

	public J2v8RnD(String pathToScripts, long timeout) {
		J2v8RnD.pathToScripts = pathToScripts;
		J2v8RnD.timeout = timeout;
		createNodeJSEngine("j2v8_request_return_30_attributes_0");
		createNodeJSEngine("j2v8_request_return_30_attributes_1");
		createNodeJSEngine("j2v8_request_return_30_attributes_2");
		
//		plugin0 = createNodeJSEngine("j2v8_request_return_30_attributes_0");
//		plugin1 = createNodeJSEngine("j2v8_request_return_30_attributes_1");
//		plugin2 = createNodeJSEngine("j2v8_request_return_30_attributes_2");
//		V8Object plugin3 = createNodeJSEngine("j2v8_request_return_30_attributes_0");
//		V8Object plugin4 = createNodeJSEngine("j2v8_request_return_30_attributes_1");
//		V8Object plugin5 = createNodeJSEngine("j2v8_request_return_30_attributes_2");
		
//		scriptMap2.put("j2v8_request_return_30_attributes_0", createNodeJSEngine("j2v8_request_return_30_attributes_0"));
//		scriptMap2.put("j2v8_request_return_30_attributes_1", createNodeJSEngine("j2v8_request_return_30_attributes_1"));
//		scriptMap2.put("j2v8_request_return_30_attributes_2", createNodeJSEngine("j2v8_request_return_30_attributes_2"));
	}

	private static void createNodeJSEngine(String scriptName) {
		long startTime = System.nanoTime();
		NodeJS nodeJS = NodeJS.createNodeJS();
		runtimeMap.put(scriptName, nodeJS);
		//nodeJS = NodeJS.createNodeJS();
		NodeJS nodeJS2 = NodeJS.createNodeJS();
		
		System.out.println("Node JS creation time: " + (System.nanoTime() - startTime));
		startTime = System.nanoTime();
//		scope = new MemoryManager(nodeJS.getRuntime());
		String scriptToExecute = pathToScripts + File.separator + scriptName + ".js";
		//scriptMap.put(scriptName, plugin);
		System.out.println("script loading time :" + (System.nanoTime() - startTime));
		scriptMap.put(scriptName, nodeJS.require(new File(scriptToExecute)));
		scriptMap2.put(scriptName, nodeJS2.require(new File(scriptToExecute)));
		runtimeMap.put(scriptName, nodeJS);
		runtimeMap2.put(scriptName, nodeJS2);
	}

	public String getMetadata(String scriptName, String resourceType)
			throws InterruptedException, NodeException, ExecutionException {
		return getResponse("getMetadata", scriptName, resourceType);
	}

	public String getAttributeValue(String scriptName, String entityType, String resourceType, String attributeId)
			throws InterruptedException, NodeException, ExecutionException {
		return getResponse("getAttributeValue", scriptName, entityType, resourceType, attributeId);
	}

	@SuppressWarnings("unused")
	private String getResponse(String... args) {
		long totalTime1 = System.nanoTime();
		long startTime = System.nanoTime();
		StringBuffer sb = new StringBuffer();
		String methodName = args[0];
		String scriptName = args[1];
		final JSONObject json = new JSONObject();

		
		V8Object engine = null;
		NodeJS nodeJS = null;

		System.out.println("Method name is :::::::::::"+methodName);
		if (methodName.equals("getMetadata")) {
			engine = scriptMap.get(scriptName);
//			createNodeJSEngine(scriptName);
			nodeJS = runtimeMap.get(scriptName);
			System.out.println("inside get metadata");
		} else if (methodName.equals("getAttributeValue")) {
			engine = scriptMap2.get(scriptName);
			nodeJS = runtimeMap2.get(scriptName);
			System.out.println("inside get attribute value");

		}
		
		// V8Object engine = createNodeJSEngine(scriptName);

		sb.append("Current method executing: " + methodName + "\n");
		sb.append("Time for engine creation and script loading: " + ((System.nanoTime() - startTime) / 1000000)
				+ " ms. \n");
		startTime = System.nanoTime();

		V8Function callback = new V8Function(nodeJS.getRuntime(), new JavaCallback() {
			public Object invoke(V8Object receiver, V8Array parameters) {
				boolean sucess = (Boolean) parameters.get(0);
				if (sucess) {
					json.put("response", new JSONObject(parameters.get(1).toString()));
				}
				return parameters;
			}
		});

		V8Array parameters = new V8Array(nodeJS.getRuntime());
		if (methodName.equals("getMetadata")) {
			// System.out.println("here...........");
			String resourceType = args[2];
			parameters.push(resourceType);
			parameters.push(callback);
		} else if (methodName.equals("getAttributeValue")) {
			String entityType = args[2];
			String resourceType = args[3];
			String attributeId = args[4];
			parameters.push(entityType);
			parameters.push(resourceType);
			parameters.push(attributeId);
			parameters.push(callback);
		} else {
			System.out.println("No method with name " + methodName + " is difined");
			return null;
		}

		engine.executeFunction(methodName, parameters);

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

		//if (scope != null && !scope.isReleased()) {
			MemoryManager scope = new MemoryManager(nodeJS.getRuntime());
			scope.release();
		//}

//		nodeJS = null; // reset to null otherwise will get exception
		// nodeJS.getRuntime().terminateExecution();
		timer.cancel();

		JSONObject response = json.has("response") ? json.getJSONObject("response") : null;
		sb.append("Method execution and callback time: " + (System.nanoTime() - startTime) / 1000000 + " ms. \n");
		sb.append("Total time: " + (System.nanoTime() - totalTime1) / 1000000 + " ms. \n");
		sb.append("Response: " + response + " \n");
		sb.append("-------------------------------------------------------------------------------------\n");
		System.out.println(sb.toString());

		return response.toString();
	}

}
