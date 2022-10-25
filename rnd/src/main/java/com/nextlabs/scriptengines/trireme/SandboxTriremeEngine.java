package com.nextlabs.scriptengines.trireme;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
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
import io.apigee.trireme.core.Sandbox;
import io.apigee.trireme.core.ScriptFuture;
import io.apigee.trireme.core.internal.ScriptRunner;
import trireme.TriremeReference;

public class SandboxTriremeEngine {
	String pathToScripts;
	// static NodeEnvironment environment;
	ConcurrentHashMap<String, TriremeReference> scriptMap;

	public SandboxTriremeEngine(String pathToScripts) throws NodeException, InterruptedException, ExecutionException {
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
				hasResult.set(true);
				// sb.append("Callback time in JavaScript: " + args[2] + "\n");
				// sb.append("Status of response: " + args[3] + "\n");
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
		Context context = reference.getEnvironment().getContextFactory().enterContext();

//		ScriptFuture sf = reference.getRuntime().getFuture();
		
		context.putThreadLocal(ScriptRunner.RUNNER, runtime);
		getResponse.call(context, runtime.getScriptScope(), moduleResult, parameters);

		// reference.getNodeScript().execute().get().getExitCode();
		long currentTime = System.currentTimeMillis();
		while (!hasResult.get()) {
			Thread.sleep(10);
			if (System.currentTimeMillis() - currentTime > 2000)
				break;
		}

		String response = result.has("response") ? result.getString("response") : null;

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
		NodeEnvironment environment = new NodeEnvironment();
		// Sandbox sandbox = new Sandbox();
		// sandbox.setFilesystemRoot("C:\\Users\\lqin\\ScriptEngines\\src\\main\\resources");
		// environment.setSandbox(sandbox);

		if (scriptMap.get(scriptName) != null) {
			return scriptMap.get(scriptName);
		} else {
			synchronized (environment) {
				 Sandbox sb = new Sandbox();
//				 sb.setFilesystemRoot("C:/Users/lqin/workspace/ScriptEngines/src/main/resources");
				// NodeEnvironment environment = new NodeEnvironment();
				// environment.setSandbox(sb);
//				 sb.setWorkingDirectory("C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources\\Root\\A");
				// // sb.setWorkingDirectory(pathToScripts);
				//
				 environment.setSandbox(sb);

//				String scriptToExecute = pathToScripts + File.separator + scriptName;
//				System.out.println("::::::::::::::::::::PATHTOSCRIPTS:::::::::" + scriptToExecute);
				// File file = new File(scriptToExecute);
				
//				Sandbox sb = new Sandbox();
				sb.setFilesystemRoot("C:/Users/lqin/workspace/ScriptEngines/src/main/resources/node_modules/");
				sb.setWorkingDirectory("C:/Users/lqin/workspace/ScriptEngines/src/main/resources/node_modules/Root/A");
				environment.setSandbox(sb);
				File file = new File(
						"C:/Users/lqin/workspace/ScriptEngines/src/main/resources/node_modules/Root/A/request_return_30_attributes_0.js");

				NodeScript nodeScript = environment.createScript("Trireme script", file, null);
				Map<String, String> scriptEnv = new HashMap<String, String>();
				scriptEnv.put("NODE_DEBUG", "module,fs");

				System.out.println("::::::::::::::::::::::::::");
				System.out.println(file.getAbsolutePath());
				nodeScript.setEnvironment(scriptEnv);

				ScriptFuture nodeModules = nodeScript.executeModule();
				nodeScript.execute().get().getExitCode();

//				nodeScript.execute().get().getExitCode();
				ScriptRunner runtime = nodeScript._getRuntime();
				Scriptable moduleResult = nodeModules.getModuleResult();
				TriremeReference reference = new TriremeReference(environment, nodeScript, nodeModules, runtime,
						moduleResult);
				scriptMap.put(scriptName, reference);

				return reference;
			}
		}
	}
}
