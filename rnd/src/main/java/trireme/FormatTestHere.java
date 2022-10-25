package trireme;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptFuture;
import io.apigee.trireme.core.internal.ScriptRunner;

public class FormatTestHere {
	String pathToScripts;
	static NodeEnvironment environment;
	ConcurrentHashMap<String, TriremeReference> scriptMap;

	public FormatTestHere(String pathToScripts) {
		environment = new NodeEnvironment();
		this.pathToScripts = pathToScripts;
		scriptMap = new ConcurrentHashMap<>();
		
	}

	public String getMetadata(String scriptName, String resourceType)
			throws InterruptedException, NodeException, ExecutionException {
		return getResponse("getMetadata", scriptName, resourceType);
	}

	public String getAttributeValue(String scriptName, String entityType, String resourceType, String attributeId)
			throws InterruptedException, NodeException, ExecutionException {
		return getResponse("getAttributeValue", scriptName, entityType, resourceType, attributeId);
	}

	private String getResponse(String... args) throws InterruptedException, NodeException, ExecutionException {
		String methodName = args[0];
		String scriptName = args[1];
		Object[] parameters;

		final JSONObject result = new JSONObject();
		long startTime = System.nanoTime();
		TriremeReference reference = createNodeScript(scriptName);
		System.out.println("Load script & create engine: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
		startTime = System.nanoTime();
		ScriptRunner runtime = reference.getRuntime();
		Scriptable moduleResult = reference.getModuleResult();

		final AtomicBoolean hasResult = new AtomicBoolean(false);

		Function callback = new org.mozilla.javascript.BaseFunction() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
				result.put("response", args[1].toString());
				hasResult.set(true);
				return args[0];
			}
		};

		if (methodName.equals("getMetadata")) {
			String resourceType = args[2];
			parameters = new Object[] { resourceType, callback };

		} else if (methodName.equals("getAttributeValue")) {
			String entityType = args[2];
			String resourceType = args[3];
			String attributeId = args[4];
			parameters = new Object[] { entityType, resourceType, attributeId, callback };
		} else {
			System.out.println("No method with name " + methodName + " is difined");
			return null;

		}

		Function getResponse = (Function) moduleResult.get(methodName, moduleResult);
		Context context = environment.getContextFactory().enterContext();
		context.putThreadLocal(ScriptRunner.RUNNER, runtime);
		getResponse.call(context, runtime.getScriptScope(), moduleResult, parameters);
		while (!hasResult.get()) {
			Thread.sleep(10);
		}
		String response = result.has("response") ? result.getString("response") : null;
		System.out.println("Method execution and callback time: " + (System.nanoTime() - startTime) / 1000000 + " ms.");

		return response;
	}

	private TriremeReference createNodeScript(String scriptName)
			throws NodeException, InterruptedException, ExecutionException {
		if (scriptMap.get(scriptName) != null) {
			return scriptMap.get(scriptName);
		} else {
			synchronized (environment) {
				String scriptToExecute = pathToScripts + File.separator + scriptName + ".js";
				File file = new File(scriptToExecute);

				NodeScript nodeScript = environment.createScript("Trireme script", file, null);

				ScriptFuture nodeModules = nodeScript.executeModule();
				ScriptRunner runtime = nodeScript._getRuntime();
				Scriptable moduleResult = nodeModules.getModuleResult();
				TriremeReference reference = new TriremeReference(nodeScript, nodeModules, runtime, moduleResult);
				scriptMap.put(scriptName, reference);

				return reference;
			}
		}
	}
}
