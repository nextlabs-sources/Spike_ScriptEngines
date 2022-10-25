package demo2;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory.Listener;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptFuture;
import io.apigee.trireme.core.internal.ScriptRunner;
import trireme.TriremeReference;

public class DemoTriremeScriptEngine {
	String pathToScripts;
	static NodeEnvironment environment;
	ConcurrentHashMap<String, TriremeReference> scriptMap;

	public DemoTriremeScriptEngine(String pathToScripts)
			throws NodeException, InterruptedException, ExecutionException {
		environment = new NodeEnvironment();
		this.pathToScripts = pathToScripts;
		scriptMap = new ConcurrentHashMap<>();
		long startTime = System.currentTimeMillis();
		System.out.println("******************** Loading scripts *************************");
		loadScripts(pathToScripts);
		System.out.println("Total time for script loading: " + (System.currentTimeMillis() - startTime));
	}

	private void loadScripts(String pathToScripts) throws NodeException, InterruptedException, ExecutionException {
		File file = new File(pathToScripts);
		File[] scripts = file.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("") && name.endsWith(".js");
			}
		});

		for (int i = 0; i < scripts.length; i++)
			createNodeScript(scripts[i].getName());
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
		long totalTime1 = System.nanoTime();
		long startTime = System.nanoTime();
		final StringBuffer sb = new StringBuffer();

		String methodName = args[0];
		String scriptName = args[1];
		Object[] parameters;

		final JSONObject result = new JSONObject();
		// TriremeReference reference = createNodeScript(scriptName);
		TriremeReference reference = scriptMap.get(scriptName + ".js");
		
		sb.append("Current method executing: " + methodName + "\n");
		sb.append("Time for script loading: " + ((System.nanoTime() - startTime) / 1000000) + " ms. \n");
		startTime = System.nanoTime();

		startTime = System.nanoTime();
		ScriptRunner runtime = reference.getRuntime();
		Scriptable moduleResult = reference.getModuleResult();

		final AtomicBoolean hasResult = new AtomicBoolean(false);

		Function callback = new org.mozilla.javascript.BaseFunction() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
				result.put("response", args[1].toString());
				//result.put("callbackTime", args[2]);
				hasResult.set(true);
//				System.out.println(args[2]); 
				//System.out.println("length is "+args.length);
				sb.append("Callback time in JavaScript: "+args[2]+"\n");
				sb.append("Status of response: "+args[3]+"\n");
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
//		environment.getContextFactory().addListener(new Listener() {
//			@Override
//			public void contextCreated(Context cx) {
//				System.out.println("::::::::::::::::::::::: context created");
//			}
//
//			@Override
//			public void contextReleased(Context cx) {
//				System.out.println("::::::::::::::::::::::: context released");				
//			}
//		});
		context.putThreadLocal(ScriptRunner.RUNNER, runtime);
		getResponse.call(context, runtime.getScriptScope(), moduleResult, parameters);
//		sb.append("====================>Right after call Status is: "+reference.getNodeScript().execute().get().getExitCode());

		reference.getNodeScript().execute().get().getExitCode();
//		while (!hasResult.get()) {
//			Thread.sleep(10);
////			sb.append("====================> Status is: "+reference.getNodeScript().execute().get().getExitCode());
////			System.out.println("====================> Status is: "+reference.getNodeScript().execute().get());
//		}
//		sb.append("====================> Status is: "+reference.getNodeScript().execute().get().getExitCode()+"\n");

		String response = result.has("response") ? result.getString("response") : null;
//		sb.append("Callback time in JavaScript: " + result.get("callbackTime") + "\n");

		sb.append("Method execution and callback time: " + (System.nanoTime() - startTime) / 1000000 + " ms. \n");
		sb.append("Total time: " + (System.nanoTime() - totalTime1) / 1000000 + " ms. \n");
		sb.append("Current time: " + System.currentTimeMillis() + "\n");
		sb.append("Response: " + response + " \n");
		sb.append("-------------------------------------------------------------------------------------\n");
		System.out.println(sb.toString());
		return response;
	}

	private TriremeReference createNodeScript(String scriptName)
			throws NodeException, InterruptedException, ExecutionException {
		if (scriptMap.get(scriptName) != null) {
			return scriptMap.get(scriptName);
		} else {
			synchronized (environment) {
				String scriptToExecute = pathToScripts + File.separator + scriptName;
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
