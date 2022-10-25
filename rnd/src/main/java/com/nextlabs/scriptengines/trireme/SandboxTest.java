package com.nextlabs.scriptengines.trireme;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.mozilla.javascript.Scriptable;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.Sandbox;
import io.apigee.trireme.core.ScriptFuture;
import io.apigee.trireme.core.internal.ScriptRunner;

public class SandboxTest {

	public static void main(String[] args) throws NodeException, InterruptedException, ExecutionException {
		for (int i = 0; i < 1; i++)
			loadOneScript();
	}

	private static void loadOneScript() throws NodeException, InterruptedException, ExecutionException {
		NodeEnvironment env = new NodeEnvironment();

		Sandbox sb = new Sandbox();

//		sb.setFilesystemRoot("C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources\\node_modules");
//        sb.mount("rootA", "C:/Users/lqin/workspace/ScriptEngines/src/main/resources/Root/A/");
//		sb.setWorkingDirectory("C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources\\node_modules");
		 sb.setHideOSDetails(true);

//		env.setSandbox(sb);

		File file = new File(
				"C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources\\node_modules\\Root\\A\\request_return_30_attributes_0.js");

		NodeScript ns = env.createScript("sdfdsf", file, null);
		ScriptFuture sf = ns.executeModule();
		Map<String, String> scriptEnv = new HashMap<String, String>();
		scriptEnv.put("NODE_DEBUG", "module,fs");
		ns.setEnvironment(scriptEnv);

//		System.out.println(env.getSandbox().getFilesystemRoot());
//		 Thread.currentThread().sleep(2000);
		ns.execute().get().getExitCode();
		sf.getModuleResult();
//		ScriptRunner runtime = reference.getRuntime();
//		Scriptable moduleResult = reference.getModuleResult();
	}
}
