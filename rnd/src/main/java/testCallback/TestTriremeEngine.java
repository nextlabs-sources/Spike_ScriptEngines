package testCallback;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptFuture;
import io.apigee.trireme.core.internal.ScriptRunner;

public class TestTriremeEngine {

	public static void main(String args[]) throws Exception {
		NodeEnvironment environment = new NodeEnvironment();
		File file = new File("C:/Users/lqin/workspace/ScriptEngines/src/main/resources/testFunc.js");
		NodeScript nodeScript = environment.createScript("Trireme script", file, null);

		ScriptFuture nodeModules = nodeScript.executeModule();
		ScriptRunner runtime = nodeScript._getRuntime();
		Scriptable moduleResult = nodeModules.getModuleResult();

		AtomicInteger sequence = new AtomicInteger();
		while (true) {
			try {
				final AtomicBoolean hasResult = new AtomicBoolean(false);

				final int sequenceVal = sequence.incrementAndGet();
				final StringBuffer sb = new StringBuffer();
				sb.append("------------------------------------------------\n");
				sb.append("Request from Java: " + sequenceVal + "\n");

				Function callback = new org.mozilla.javascript.BaseFunction() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
						sb.append(System.currentTimeMillis() + "  " + args[0] + "  Callback sequence: " + args[1]
								+ " Java request sequence: " + sequenceVal + "\n");

						// System.out.println(args[0]+" sequence: "+args[1]+"
						// request sequence: "+sequenceVal);
						hasResult.set(true);
						return args[0];
					}
				};

				Object[] parameters = new Object[] { sequenceVal, callback };

				Function testFunc = (Function) moduleResult.get("testFunc", moduleResult);
				Context context = environment.getContextFactory().enterContext();

				context.putThreadLocal(ScriptRunner.RUNNER, runtime);
				testFunc.call(context, runtime.getScriptScope(), moduleResult, parameters);

				long start = System.currentTimeMillis();
				while (!hasResult.get()) {
					// System.out.println("sleeping...");
					Thread.sleep(10);
					if (System.currentTimeMillis() - start > 2000)
						break;
				}

				System.out.println(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}
}
