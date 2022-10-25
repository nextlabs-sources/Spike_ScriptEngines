package j2v8_RnD;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.utils.V8Runnable;
import com.eclipsesource.v8.utils.V8Thread;

import io.apigee.trireme.core.NodeException;

public class j2v8_RnD_test {

	static String pathToScripts = "C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources\\backup";

	public static void main(String[] args) throws InterruptedException, NodeException, ExecutionException {
		String script1 = "j2v8_request_return_2_attributes";
		String script2 = "j2v8_request_return_30_attributes";
		String script3 = "direct_return_2_attributes";
		String script4 = "direct_return_30_attributes";
		long startTime = System.currentTimeMillis();
		j2v8LoadTest(script1, pathToScripts);
		//j2v8MultiThreadedTest(script2, pathToScripts);

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
			//final J2v8RnD plugin = new J2v8RnD(pathToScripts, 5000);

			exeService.execute(new V8Thread(new V8Runnable() {
				@Override
				public void run(V8 runtime) {
					int index = (int) Math.floor(Math.random() * 3);
					try {
						//plugin.getMetadata("df", "dsf");
						singleScriptCall(pathToScripts, scriptName + "_" + index, "User", "SAP", "departmentId");
						//plugin.getMetadata(scriptName + "_" + index, "sdf");
						//plugin.getAttributeValue(scriptName + "_" + index, "sdf", "sdf", "sdfsdf");
					} catch (InterruptedException | NodeException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				
				
			}));
		}
		Thread.currentThread().join();
		long endTime = System.currentTimeMillis();
		System.out.println("::::::->>>>>>>>> Multi Threaded processing time: " + (endTime - startTime) + " ms ");
	}

	private static void j2v8LoadTest(String script, String pathToScripts)
			throws InterruptedException, NodeException, ExecutionException {
		System.out.println("::::::::::::::  J2V8 LOAD TESTING  :::::::::::::::::::\n\n");

		long startTime = System.currentTimeMillis();
		J2v8RnD plugin = new J2v8RnD(pathToScripts, 5000);

		for (int i = 0; i < 10000; i++) {
//			plugin.getMetadata(script + "_" + (i % 3), "User");
//			plugin.getAttributeValue(script + "_" + (i % 3), "SAP", "SUBJECT", "Finance Doc");
			
			singleScriptCall(pathToScripts, script + "_" + (i % 3), "User", "SAP", "departmentId");
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Total time for load testing: " + (endTime - startTime) + " ms ");
	}

	private static synchronized void singleScriptCall(String pathToScripts, String scriptName, String resourceType,
			String entityType, String attributeId) throws InterruptedException, NodeException, ExecutionException {
		J2v8RnD plugin = new J2v8RnD(pathToScripts, 5000);

		plugin.getMetadata(scriptName, resourceType);
//		plugin.getMetadata(scriptName, resourceType);
//		plugin.getMetadata(scriptName, resourceType);
//		plugin.getMetadata(scriptName, resourceType);

		plugin.getAttributeValue(scriptName, entityType, resourceType, attributeId);

	}

}
