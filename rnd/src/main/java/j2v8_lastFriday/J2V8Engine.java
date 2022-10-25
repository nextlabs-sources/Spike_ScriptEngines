package j2v8_lastFriday;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.utils.MemoryManager;

public class J2V8Engine {

	static NodeJS nodeJS = null;
	static String pathToScripts;
	static long timeout = 1000; // default timeout is set to 1 sec

	public J2V8Engine(String pathToScripts) {
		J2V8Engine.pathToScripts = pathToScripts;
	}

	public J2V8Engine(String pathToScripts, long timeout) {
		J2V8Engine.pathToScripts = pathToScripts;
		J2V8Engine.timeout = timeout;
	}

	private V8Object createNodeJSEngine(MemoryManager scope, String scriptName) {
		nodeJS = NodeJS.createNodeJS();

		scope = new MemoryManager(nodeJS.getRuntime());
		String scriptToExecute = pathToScripts + File.separator + scriptName + ".js";
		V8Object plugin = nodeJS.require(new File(scriptToExecute));

		return plugin;
	}

	public String getAttributeValue(String scriptName, String entityType, String resourceType, String attributeId) {
		MemoryManager scope = null;
		final JSONObject attribute = new JSONObject();
		V8Object plugin = createNodeJSEngine(scope, scriptName);

		V8Function attributeCallback = new V8Function(nodeJS.getRuntime(), new JavaCallback() {
			public Object invoke(V8Object receiver, V8Array parameters) {
				boolean sucess = (Boolean) parameters.get(0);
				if (sucess) {
					attribute.put("response", new JSONObject(parameters.get(1).toString()));
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

		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				nodeJS = null;
			}
		}, timeout);

		try {
			while (nodeJS.isRunning()) {
				nodeJS.handleMessage();
			}
		} catch (NullPointerException e) {
			return null;

		} finally {
			if (scope != null && !scope.isReleased()) {
				scope.release();
			}
		}

		timer.cancel();
		nodeJS = null; // reset to null otherwise will get exception
		JSONObject attributeResponse = attribute.getJSONObject("response");
		return attributeResponse.toString();
	}

	public String getMetadata(String scriptName, String resourceType) {
		MemoryManager scope = null;
		final JSONObject metadata = new JSONObject();
		V8Object plugin = createNodeJSEngine(scope, scriptName);

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

		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				nodeJS = null;
			}
		}, timeout);

		try {
			while (nodeJS.isRunning()) {
				nodeJS.handleMessage();
			}
		} catch (NullPointerException e) {
			return null;

		} finally {
			if (scope != null && !scope.isReleased()) {
				scope.release();
			}
		}
		
		timer.cancel();
		nodeJS = null;
		JSONObject metadataResponse = metadata.getJSONObject("response");
		return metadataResponse.toString();
	}
}
