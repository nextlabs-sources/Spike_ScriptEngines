package com.nextlabs.scriptengines.trireme;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.apigee.trireme.core.NodeException;

public class TriremeDriver {

	public static void main(String[] args) throws NodeException, InterruptedException, ExecutionException {
		String pathToScripts = "C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources\\trireme_scripts";
		String script1 = "request_return_2_attributes";
		String script2 = "request_return_30_attributes";
		String script3 = "direct_return_2_attributes";
		String script4 = "direct_return_30_attributes";
		long startTime = System.currentTimeMillis();

		// TriremeLoadTesting(script2, pathToScripts);
		TriremeMultiThreadingTest(script3, pathToScripts);

		System.out.println("Total name for 10000 execution: " + (System.currentTimeMillis() - startTime) + " ms.");
		System.out.println("Average execution time: " + (System.currentTimeMillis() - startTime) / 10000.0 + " ms.");

	}

	private static void TriremeLoadTesting(String script, String pathToScripts)
			throws NodeException, InterruptedException, ExecutionException {
		String scriptName = script;
		System.out.println("::::::::::::::  LOAD TESTING  :::::::::::::::::::\n\n");
		TriremeEngine engine = new TriremeEngine(pathToScripts);
		for (int i = 0; i < 10000; i++) {
			int index = i % 3;
			engine.getMetadata(scriptName + "_" + index, "Michael");

			engine.getAttributeValue(scriptName + "_" + index, "SAP", "SUBJECT", "Engin Doc");

		}
	}

	static ExecutorService exeService = Executors.newFixedThreadPool(40);

	@SuppressWarnings("static-access")
	private static void TriremeMultiThreadingTest(String script, String pathToScripts)
			throws NodeException, InterruptedException, ExecutionException {
		final String scriptName = script;
		final TriremeEngine engine = new TriremeEngine(pathToScripts);
		System.out.println("::::::::::::::  MULTI-THREADING TESTING  :::::::::::::::::::\n\n");

		long time1 = System.currentTimeMillis();

		for (int i = 0; i < 100000000; i++) {
			final String x = String.valueOf(i);
			exeService.execute(new Runnable() {
				public void run() {
					try {
						int index = (int) Math.floor(Math.random() * 3);
						index = 1;
						// engine.getMetadata(scriptName + "_" + index, "sdf");

						engine.getAttributeValue(scriptName + "_" + index, x, "SUBJECT", "Engin Doc");

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			// Thread.currentThread().join();
			Thread.currentThread().sleep(500);
		}
		long time2 = System.currentTimeMillis();

		Thread.currentThread().join();
		System.out.println("Time taken for 10000 execution is :" + (time2 - time1));
		System.out.println("Average execution time is " + (time2 - time1) / 10000);

	}

}
