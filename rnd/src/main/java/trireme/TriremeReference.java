package trireme;

import org.mozilla.javascript.Scriptable;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptFuture;
import io.apigee.trireme.core.internal.ScriptRunner;

public class TriremeReference {
	NodeScript nodeScript = null;

	ScriptFuture nodeModules = null;

	ScriptRunner runtime = null;

	Scriptable moduleResult = null;
	
	NodeEnvironment environmnet = null;

	public TriremeReference(NodeEnvironment environment, NodeScript nodeScript, ScriptFuture nodeModules, ScriptRunner runtime,
			Scriptable moduleResult) {
		super();
		this.environmnet = environment;
		this.nodeScript = nodeScript;
		this.nodeModules = nodeModules;
		this.runtime = runtime;
		this.moduleResult = moduleResult;
	}
	
	public NodeEnvironment getEnvironment(){
		return environmnet;
	}

	public NodeScript getNodeScript() {
		return nodeScript;
	}

	public void setNodeScript(NodeScript nodeScript) {
		this.nodeScript = nodeScript;
	}

	public ScriptFuture getNodeModules() {
		return nodeModules;
	}

	public void setNodeModules(ScriptFuture nodeModules) {
		this.nodeModules = nodeModules;
	}

	public ScriptRunner getRuntime() {
		return runtime;
	}

	public void setRuntime(ScriptRunner runtime) {
		this.runtime = runtime;
	}

	public Scriptable getModuleResult() {
		return moduleResult;
	}

	public void setModuleResult(Scriptable moduleResult) {
		this.moduleResult = moduleResult;
	}

}
