package info.jerrinot;

import com.hazelcast.internal.serialization.DataSerializerHook;
import com.hazelcast.nio.serialization.DataSerializableFactory;

public class KotlinEPHook<K, V> implements DataSerializerHook {

    @Override
    public int getFactoryId() {
        return KotlinEP.FACTORY_ID;
    }

    @Override
    public DataSerializableFactory createFactory() {
        return new KotlinEPFactory();
    }
}
