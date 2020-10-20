package info.jerrinot.kotlinep.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.nio.serialization.GenericRecord;
import com.hazelcast.sql.SqlService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class SqlClientMain {
    public static final String TYPE_NAME = "IMap";
    static String OPTION_KEY_FORMAT = "keyFormat";
    static String PORTABLE_FORMAT = "portable";
    static String OPTION_KEY_FACTORY_ID = "keyPortableFactoryId";
    static String OPTION_KEY_CLASS_ID = "keyPortableClassId";
    static String OPTION_KEY_CLASS_VERSION = "keyPortableClassVersion";
    static String OPTION_VALUE_FORMAT = "valueFormat";
    static String OPTION_VALUE_FACTORY_ID = "valuePortableFactoryId";
    static String OPTION_VALUE_CLASS_ID = "valuePortableClassId";
    static String OPTION_VALUE_CLASS_VERSION = "valuePortableClassVersion";

    static int PERSON_ID_FACTORY_ID = 1;
    static int PERSON_ID_CLASS_ID = 1;
    static int PERSON_ID_CLASS_VERSION = 1;

    static int PERSON_FACTORY_ID = 1;
    static int PERSON_CLASS_ID = 2;
    static int PERSON_CLASS_VERSION = 1;



    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("jet");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        SqlService sql = client.getSql();
        String name = "myMap";
        IMap<Integer, GenericRecord> map = client.getMap(name);
        map.clear();

//        map.put(0,
//                new PortableGenericRecordBuilder(
//                        new ClassDefinitionBuilder(PERSON_FACTORY_ID, PERSON_CLASS_ID, PERSON_CLASS_VERSION)
//                                .addIntField("id")
//                                .addUTFField("name")
//                                .build())
//                        .writeInt("id", 2)
//                        .writeUTF("name", "Alice")
//                        .build());

        sql.execute("CREATE OR REPLACE MAPPING " + name + " ("
                + "id INT EXTERNAL NAME \"__key\""
                + ", id2 INT EXTERNAL NAME \"this.id\""
                + ", name VARCHAR EXTERNAL NAME \"this.name\""
                + ") TYPE " + SqlClientMain.TYPE_NAME + ' '

                + "OPTIONS ("
                + '"' + OPTION_KEY_FORMAT + "\" '" + PORTABLE_FORMAT + '\''
                + ", \"" + OPTION_KEY_FACTORY_ID + "\" '" + PERSON_ID_FACTORY_ID + '\''
                + ", \"" + OPTION_KEY_CLASS_ID + "\" '" + PERSON_ID_CLASS_ID + '\''
                + ", \"" + OPTION_KEY_CLASS_VERSION + "\" '" + PERSON_ID_CLASS_VERSION + '\''
                + ", \"" + OPTION_VALUE_FORMAT + "\" '" + PORTABLE_FORMAT + '\''
                + ", \"" + OPTION_VALUE_FACTORY_ID + "\" '" + PERSON_FACTORY_ID + '\''
                + ", \"" + OPTION_VALUE_CLASS_ID + "\" '" + PERSON_CLASS_ID + '\''
                + ", \"" + OPTION_VALUE_CLASS_VERSION + "\" '" + (PERSON_CLASS_VERSION + 1) + '\''
                + ")");

//        sql.execute("SINK INTO " + name + " VALUES (2, 'Bob')");
//        sql.execute("SINK INTO " + name + " (this, __key) VALUES ('Bob', 2)");
        sql.execute("SINK INTO " + name + " (id, id2, name) VALUES (3, 2, 'Bob')");

        Set<Map.Entry<Integer, GenericRecord>> entries = map.entrySet();
        for (Map.Entry<Integer, GenericRecord> entry : entries) {
            Integer key = entry.getKey();
            GenericRecord value = entry.getValue();
            System.out.println("Key: " + key);
            System.out.println("Value: " + value);
            System.out.println("--------");
        }
    }
}
