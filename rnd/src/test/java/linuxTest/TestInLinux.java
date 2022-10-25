package linuxTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import demo2.DemoJ2V8ScriptEngine;
import demo2.DemoTriremeScriptEngine;
import io.apigee.trireme.core.NodeException;

public class TestInLinux {

	static String pathToScripts = "C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources";

	@Test
	public void test() throws NodeException, InterruptedException, ExecutionException {
		String j2v8Script1 = "j2v8_request_return_2_attributes";
		String j2v8Script2 = "j2v8_request_return_30_attributes";
		String j2v8Script3 = "direct_return_2_attributes";
		String j2v8Script4 = "direct_return_30_attributes";

		String triremeScript1 = "request_return_2_attributes";
		String triremeScript2 = "request_return_30_attributes";
		String triremeScript3 = "direct_return_2_attributes";
		String triremeScript4 = "direct_return_30_attributes";
		long startTime = System.currentTimeMillis();

		//TriremeLoadTesting(triremeScript2, pathToScripts);
		// TriremeMultiThreadingTest(triremeScript3, pathToScripts);
		j2v8LoadTest(j2v8Script2, pathToScripts);
		// j2v8MultiThreadedTest(j2v8Script2, pathToScripts);

		System.out.println("Total name for 10000 execution: " + (System.currentTimeMillis() - startTime) + " ms.");
		System.out.println("Average execution time: " + (System.currentTimeMillis() - startTime) / 10000.0 + " ms.");
	}

	static ExecutorService exeService = Executors.newFixedThreadPool(20);

	@SuppressWarnings("unused")
	private static void j2v8MultiThreadedTest(final String script, final String pathToScripts)
			throws InterruptedException {
		System.out.println("::::::::::::::  J2V8 MULTI-THREADING TESTING  :::::::::::::::::::\n\n");

		long startTime = System.currentTimeMillis();
		final String scriptName = script;
		for (int i = 0; i < 10000; i++) {
			exeService.execute(new Runnable() {
				public void run() {
					int index = (int) Math.floor(Math.random() * 3);
					try {
						singleScriptCall(pathToScripts, scriptName + "_" + index, "User", "SAP", "departmentId");
					} catch (InterruptedException | NodeException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
		}
		Thread.currentThread().join();
		long endTime = System.currentTimeMillis();
		System.out.println("::::::->>>>>>>>> Multi Threaded processing time: " + (endTime - startTime) + " ms ");
	}

	private static void j2v8LoadTest(String script, String pathToScripts)
			throws InterruptedException, NodeException, ExecutionException {
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

		plugin.getMetadata(scriptName, resourceType);
		plugin.getAttributeValue(scriptName, entityType, resourceType, attributeId);

	}

	private static void TriremeLoadTesting(String script, String pathToScripts)
			throws NodeException, InterruptedException, ExecutionException {
		String scriptName = script;
		System.out.println("::::::::::::::  LOAD TESTING  :::::::::::::::::::\n\n");
		DemoTriremeScriptEngine engine = new DemoTriremeScriptEngine(pathToScripts);
		for (int i = 0; i < 10000; i++) {
			int index = i % 3;
			engine.getMetadata(scriptName + "_" + index, "Michael");

			engine.getAttributeValue(scriptName + "_" + index, "SAP", "SUBJECT", "Engin Doc");

		}
	}

	@SuppressWarnings("unused")
	private static void TriremeMultiThreadingTest(String script, String pathToScripts)
			throws NodeException, InterruptedException, ExecutionException {
		final String scriptName = script;
		final DemoTriremeScriptEngine engine = new DemoTriremeScriptEngine(pathToScripts);
		System.out.println("::::::::::::::  MULTI-THREADING TESTING  :::::::::::::::::::\n\n");

		long time1 = System.currentTimeMillis();

		for (int i = 0; i < 10000; i++) {
			exeService.execute(new Runnable() {
				public void run() {
					try {
						int index = (int) Math.floor(Math.random() * 3);
						engine.getMetadata(scriptName + "_" + index, "sdf");

						engine.getAttributeValue(scriptName + "_" + index, "SAP", "SUBJECT", "Engin Doc");

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		long time2 = System.currentTimeMillis();

		Thread.currentThread().join();
		System.out.println("Time taken for 10000 execution is :" + (time2 - time1));
		System.out.println("Average execution time is " + (time2 - time1) / 10000);

	}

}
