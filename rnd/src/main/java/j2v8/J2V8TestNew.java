package j2v8;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.utils.V8Runnable;
import com.eclipsesource.v8.utils.V8Thread;

import io.apigee.trireme.core.NodeException;

public class J2V8TestNew {
	static ExecutorService exeService = Executors.newFixedThreadPool(20);

	public static void main(String[] args) throws InterruptedException, NodeException, ExecutionException {
		final J2V8ScriptEngine plugin = new J2V8ScriptEngine(
				"C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources\\", 1000);

		for (int i = 0; i < 10; i++) {

			Thread t = new V8Thread(new V8Runnable() {

				@Override
				public void run(V8 runtime) {
						plugin.getAttributeValue("j2v8_request_return_2_attributes_2", "asdf", "dsfsdf", "sdfsf");
						
//					singleScriptCall(plugin, "j2v8_request_return_2_attributes_2", "asdf", "dsfsdf", "sdfsf");
				}
			});
//			// t.start();
//
			exeService.submit(t);
		}

		Thread.currentThread().join();
	}

	private static synchronized void  singleScriptCall(J2V8ScriptEngine plugin, String scriptName, String resourceType,
			String entityType, String attributeId) {

		long startTime = System.nanoTime();
		// System.out.println("Method name: getMetadata()");
		// String metadataResponse = plugin.getMetadata(scriptName,
		// resourceType);
		// System.out.println("Total time: " + (System.nanoTime() - startTime) /
		// 1000000 + " ms.");
		// System.out.print("Response:");
		// System.out.println(metadataResponse);

		// startTime = System.nanoTime();
		// StringBuffer sb = new StringBuffer();
		// sb.append("------------------------------------------------------------------------------\n");
		// sb.append("Method name: getAttributeValue()\n");
		String attributeValue = plugin.getAttributeValue(scriptName, entityType, resourceType, attributeId);

		System.out.println("HREREER" + attributeValue);

		// sb.append("Total time: " + (System.nanoTime() - startTime) / 1000000
		// + " ms.\n");
		// sb.append("Response:");
		// sb.append(attributeValue + "\n");
		// sb.append("------------------------------------------------------------------------------\n");
		// System.out.println(sb.toString());
		// System.out.println("------------------------------------------------------------------------------\n");
	}
}
