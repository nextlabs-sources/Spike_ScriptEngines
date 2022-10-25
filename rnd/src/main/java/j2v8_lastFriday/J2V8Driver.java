package j2v8_lastFriday;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.apigee.trireme.core.NodeException;

public class J2V8Driver {
	
	static String pathToScripts = "C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources";

	public static void main(String[] args) throws InterruptedException, NodeException, ExecutionException {
		String script1 = "j2v8_request_return_2_attributes";
		String script2 = "j2v8_request_return_30_attributes";
		String script3 = "direct_return_2_attributes";
		String script4 = "direct_return_30_attributes";
		long startTime = System.currentTimeMillis();
		//j2v8LoadTest(script1, pathToScripts);
		j2v8MultiThreadedTest(script2, pathToScripts);

		System.out.println("Total name for 10000 execution: " + (System.currentTimeMillis() - startTime) + " ms.");
		System.out.println("Average execution time: " + (System.currentTimeMillis() - startTime) / 10000.0 + " ms.");
	}

	static ExecutorService exeService = Executors.newFixedThreadPool(20);
	private static void j2v8MultiThreadedTest(final String script, final String pathToScripts)
			throws InterruptedException {
		System.out.println("::::::::::::::  J2V8 MULTI-THREADING TESTING  :::::::::::::::::::\n\n");

		long startTime = System.currentTimeMillis();
		final String scriptName = script;
		for (int i = 0; i < 10000; i++) {
			exeService.execute(new Runnable() {
				public void run() {
					int index = (int) Math.floor(Math.random() * 3);
					singleScriptCall(pathToScripts, scriptName + "_" + index, "User", "SAP", "departmentId");
				}
			});
		}
		Thread.currentThread().join();
		long endTime = System.currentTimeMillis();
		System.out.println("::::::->>>>>>>>> Multi Threaded processing time: " + (endTime - startTime) + " ms ");
	}

	private static void j2v8LoadTest(String script, String pathToScripts) {
		System.out.println("::::::::::::::  J2V8 LOAD TESTING  :::::::::::::::::::\n\n");

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			singleScriptCall(pathToScripts, script + "_" + (i % 3), "User", "SAP", "departmentId");
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Total time for load testing: " + (endTime - startTime) + " ms ");
	}

	private static synchronized void singleScriptCall(String pathToScripts, String scriptName, String resourceType,
			String entityType, String attributeId) {
		J2V8Engine plugin = new J2V8Engine(pathToScripts, 1000);

		long startTime = System.nanoTime();
		System.out.println("Method name: getMetadata()");
		String metadataResponse = plugin.getMetadata(scriptName, resourceType);
		System.out.println("Total time: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
		System.out.print("Response:");
		System.out.println(metadataResponse);
		System.out.println("------------------------------------------------------------------------------\n");

		startTime = System.nanoTime();
		System.out.println("Method name: getAttributeValue()");
		String attributeValue = plugin.getAttributeValue(scriptName, entityType, resourceType, attributeId);
		System.out.println("Total time: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
		System.out.print("Response:");
		System.out.println(attributeValue);
		System.out.println("------------------------------------------------------------------------------\n");
	}
	private static synchronized void singleScriptCall(J2V8Engine engine, String scriptName, String resourceType, String entityType,
			String attributeId) {

		long startTime = System.nanoTime();
		System.out.println("Method name: getMetadata()");
		String metadataResponse = engine.getMetadata(scriptName, resourceType);
		System.out.println("Total time: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
		System.out.print("Response:");
		System.out.println(metadataResponse);
		System.out.println("------------------------------------------------------------------------------\n");

		startTime = System.nanoTime();
		System.out.println("Method name: getAttributeValue()");
		String attributeValue = engine.getAttributeValue(scriptName, entityType, resourceType, attributeId);
		System.out.println("Total time: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
		System.out.print("Response:");
		System.out.println(attributeValue);
		System.out.println("------------------------------------------------------------------------------\n");
	}
}
