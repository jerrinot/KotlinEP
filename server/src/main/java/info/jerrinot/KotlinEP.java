package info.jerrinot;

import com.hazelcast.core.HazelcastException;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.Map;

public class KotlinEP<K, V> implements EntryProcessor<K, V, Object>, IdentifiedDataSerializable {
    public static final int FACTORY_ID = 1;
    public static final int CLASS_ID = 1;

    private static final ScriptEngineManager ENGINE_MANAGER = new ScriptEngineManager();
    private String script;

    public KotlinEP(String script) {
        this.script = script;
    }

    public KotlinEP() {

    }

    @Override
    public Object process(Map.Entry<K, V> entry) {
        ScriptEngine kts = ENGINE_MANAGER.getEngineByExtension("kts");
        try {
            Bindings bindings = kts.createBindings();
            bindings.put("key", entry.getKey());
            bindings.put("value", entry.getValue());
            bindings.put("entry", entry);
            return kts.eval(script, bindings);
        } catch (ScriptException e) {
            throw new HazelcastException("Error while executing script", e);
        }
    }

    @Override
    public int getFactoryId() {
        return FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return CLASS_ID;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(script);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        script = in.readUTF();
    }
}