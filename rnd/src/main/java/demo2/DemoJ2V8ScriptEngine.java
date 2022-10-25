package demo2;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.utils.MemoryManager;

import io.apigee.trireme.core.NodeException;

public class DemoJ2V8ScriptEngine {

	static NodeJS nodeJS = null;
	static String pathToScripts;
	static long timeout = 1000; // default timeout is set to 1 sec
	// static MemoryManager scope = null;
	// static V8Object engine = createNodeJSEngine(scope,
	// "j2v8_request_return_2_attributes_0.js");

	public DemoJ2V8ScriptEngine(String pathToScripts) {
		DemoJ2V8ScriptEngine.pathToScripts = pathToScripts;
	}

	public DemoJ2V8ScriptEngine(String pathToScripts, long timeout) {
		DemoJ2V8ScriptEngine.pathToScripts = pathToScripts;
		DemoJ2V8ScriptEngine.timeout = timeout;
	}

	private static V8Object createNodeJSEngine(MemoryManager scope, String scriptName) {
		nodeJS = NodeJS.createNodeJS();
		scope = new MemoryManager(nodeJS.getRuntime());
		String scriptToExecute = pathToScripts + File.separator + scriptName+".js";
		V8Object plugin = nodeJS.require(new File(scriptToExecute));

		return plugin;
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
		MemoryManager scope = null;
		final JSONObject json = new JSONObject();
		V8Object engine = createNodeJSEngine(scope, scriptName);

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

		if (scope != null && !scope.isReleased()) {
			scope.release();
		}

		nodeJS = null; // reset to null otherwise will get exception
		timer.cancel();

		JSONObject response = json.has("response")? json.getJSONObject("response"): null;
		sb.append("Method execution and callback time: " + (System.nanoTime() - startTime) / 1000000 + " ms. \n");
		sb.append("Total time: " + (System.nanoTime() - totalTime1) / 1000000 + " ms. \n");
		sb.append("Response: " + response + " \n");
		sb.append("-------------------------------------------------------------------------------------\n");
		System.out.println(sb.toString());

		return response.toString();
	}

}
