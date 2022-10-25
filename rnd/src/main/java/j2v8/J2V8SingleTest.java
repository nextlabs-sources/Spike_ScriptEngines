package j2v8;

import java.util.concurrent.ExecutionException;

import io.apigee.trireme.core.NodeException;

public class J2V8SingleTest {
	static String pathToScripts = "C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources";
	public static void main(String[] args) throws NodeException, InterruptedException, ExecutionException {
		String script2 = "j2v8_request_return_30_attributes_0";

		loadTest (script2);
	}
	private static void loadTest(String script) throws NodeException, InterruptedException, ExecutionException {
		J2V8ScriptEngine scriptEngine = new J2V8ScriptEngine(pathToScripts, 5000);
		String response = scriptEngine.getMetadata(script, "xyz");
		System.out.println(response);
		// TODO Auto-generated method stub
		
	}

}
