package trireme;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.apigee.trireme.core.NodeException;

public class TriremeDriver {

	public static void main(String[] args) throws NodeException, InterruptedException, ExecutionException {
		String pathToScripts = "src\\main\\resources\\trireme_scripts";
		String script1 = "request_return_2_attributes";
		String script2 = "request_return_30_attributes";
		String script3 = "direct_return_2_attributes";
		String script4 = "direct_return_30_attributes";
		long startTime = System.currentTimeMillis();
		
		TriremeLoadTesting(script2, pathToScripts);
		//TriremeMultiThreadingTest(script3, pathToScripts);

		System.out.println("Total name for 10000 execution: " + (System.currentTimeMillis() - startTime) + " ms.");
		System.out.println("Average execution time: " + (System.currentTimeMillis() - startTime)/10000.0 + " ms.");

	}

	private static void TriremeLoadTesting(String script, String pathToScripts) throws NodeException, InterruptedException, ExecutionException {
		String scriptName = script;
		long startTime;
		System.out.println("::::::::::::::  LOAD TESTING  :::::::::::::::::::\n\n");
		TriremeScriptEngine engine = new TriremeScriptEngine(pathToScripts);
		for (int i = 0; i < 10000; i++) {
			System.out.println("================== " +i +"th test ============================");
			int index = i % 3;
			index = 2;
			startTime = System.nanoTime();
			String metadata = engine.getMetadata(scriptName + "_" + index, "Michael");
			System.out.println("Method name: getMetadata()");
			System.out.println("Total name: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
			System.out.print("Response:");
			System.out.println(metadata);
			System.out.println("------------------------------------------------------------------------------\n");

			startTime = System.nanoTime();
			String attributeValue = engine.getAttributeValue(scriptName + "_" + index, "SAP", "SUBJECT", "Engin Doc");
			System.out.println("Method name: getAttributeValue()");
			System.out.println("Total time: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
			System.out.print("Response:");
			System.out.println(attributeValue);
			System.out.println("------------------------------------------------------------------------------\n");
		}
	}

	static ExecutorService exeService = Executors.newFixedThreadPool(20);

	private static void TriremeMultiThreadingTest(String script, String pathToScripts) throws NodeException, InterruptedException, ExecutionException {
		final String scriptName = script;
		final TriremeScriptEngine engine = new TriremeScriptEngine(pathToScripts);
		System.out.println("::::::::::::::  MULTI-THREADING TESTING  :::::::::::::::::::\n\n");

		long time1 = System.currentTimeMillis();

		for (int i = 0; i < 10000; i++) {
			exeService.execute(new Runnable() {
				public void run() {
					try {
						long startTime = System.nanoTime();
						int index = (int) Math.floor(Math.random() * 3);
						System.out.println("Method name: getMetadata()");
						String metadata = engine.getMetadata(scriptName + "_" + index, "sdf");
						System.out.println("Total time: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
						System.out.print("Response:");
						System.out.println(metadata);
						System.out.println(
								"------------------------------------------------------------------------------\n");

						startTime = System.nanoTime();
						System.out.println("Method name: getAttributeValue()");
						String attributeValue = engine.getAttributeValue(scriptName + "_" + index, "SAP", "SUBJECT",
								"Engin Doc");
						System.out.println("Total time: " + (System.nanoTime() - startTime) / 1000000 + "  ms.");
						System.out.print("Response:");
						System.out.println(attributeValue);
						System.out.println(
								"------------------------------------------------------------------------------\n");
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
