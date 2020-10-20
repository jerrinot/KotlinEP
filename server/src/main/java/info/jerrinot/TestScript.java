package info.jerrinot;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TestScript {
    public static void main(String[] args) throws ScriptException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine kts = engineManager.getEngineByExtension("kts");

        Integer number = 5;
        Bindings bindings = kts.createBindings();
        bindings.put("number", number);

        String script = "println(\"foo\")";

        Object result = kts.eval(script, bindings);
        System.out.println(result);
    }
}
