package info.jerrinot;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class KotlinEPFactory implements DataSerializableFactory {
    @Override
    public IdentifiedDataSerializable create(int typeId) {
        switch (typeId) {
            case KotlinEP.CLASS_ID:
                return new KotlinEP();
            default:
                throw new IllegalArgumentException("Unknon class id " + typeId);
        }
    }
}
