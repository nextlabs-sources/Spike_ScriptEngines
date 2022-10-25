package j2v8;

import java.io.File;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;

public class J2V8ScriptEngineNew {
	V8 v8 = V8.createV8Runtime();
	NodeJS node = NodeJS.createNodeJS();
	File file = new File ("C:\\Users\\lqin\\workspace\\ScriptEngines\\src\\main\\resources\\testScript.js");
	V8Object exports = node.require(file);
	
	
}
