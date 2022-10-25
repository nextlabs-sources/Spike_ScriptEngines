package demo2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.apigee.trireme.core.NodeException;

public class DemoJ2V8Driver {
	
	static String pathToScripts = "C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources";

	public static void main(String[] args) throws InterruptedException, NodeException, ExecutionException {
		String script1 = "j2v8_request_return_2_attributes";
		String script2 = "j2v8_request_return_30_attributes";
		String script3 = "direct_return_2_attributes";
		String script4 = "direct_return_30_attributes";
		long startTime = System.currentTimeMillis();
		//j2v8LoadTest(script2, pathToScripts);
		j2v8MultiThreadedTest(script2, pathToScripts);

		System.out.println("Total name for 10000 execution: " + (System.currentTimeMillis() - startTime) + " ms.");
		System.out.println("Average execution time: " + (System.currentTimeMillis() - startTime) / 10000.0 + " ms.");
	}

	static ExecutorService exeService = Executors.newFixedThreadPool(40);
	
	@SuppressWarnings({ "unused", "static-access" })
	private static void j2v8MultiThreadedTest(final String script, final String pathToScripts)
			throws InterruptedException {
		System.out.println("::::::::::::::  J2V8 MULTI-THREADING TESTING  :::::::::::::::::::\n\n");

		long startTime = System.currentTimeMillis();
		final String scriptName = script;
		long c = 100000000;//System.currentTimeMillis();
		for (long i = 0; i < 100000000; i++) {
//		while(1 < 10) {
			exeService.submit(new Runnable() {
				public void run() {
					int index = (int) Math.floor(Math.random() * 3);
					try {
						singleScriptCall(pathToScripts, scriptName + "_" + index, "User", "SAP", "departmentId");
					} catch (InterruptedException | NodeException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			Thread.currentThread().sleep(1000);
		}
		Thread.currentThread().join();
		long endTime = System.currentTimeMillis();
		System.out.println("::::::->>>>>>>>> Multi Threaded processing time: " + (endTime - startTime) + " ms ");
	}

	private static void j2v8LoadTest(String script, String pathToScripts) throws InterruptedException, NodeException, ExecutionException {
		System.out.println("::::::::::::::  J2V8 LOAD TESTING  :::::::::::::::::::\n\n");

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			singleScriptCall(pathToScripts, script + "_" + (i % 3), "User", "SAP", "departmentId");
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Total time for load testing: " + (endTime - startTime) + " ms ");
	}

	private static synchronized void singleScriptCall(String pathToScripts, String scriptName, String resourceType,
			String entityType, String attributeId) throws InterruptedException, NodeException, ExecutionException {
		DemoJ2V8ScriptEngine plugin = new DemoJ2V8ScriptEngine(pathToScripts, 1000);

		//plugin.getMetadata(scriptName, resourceType);
		plugin.getAttributeValue(scriptName, entityType, resourceType, attributeId);

	}

}
