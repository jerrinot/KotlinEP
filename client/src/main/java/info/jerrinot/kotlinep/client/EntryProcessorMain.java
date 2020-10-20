package info.jerrinot.kotlinep.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.IMap;
import info.jerrinot.kotlinep.client.domain.Person;
import info.jerrinot.kotlinep.client.impl.ScriptEP;

import java.util.concurrent.ExecutionException;

public class EntryProcessorMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ClientConfig clientConfig = new ClientConfig();
//        clientConfig.setClusterName("jet");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IMap<Integer, Person> myMap = client.getMap("myMap");
        for (int i = 0; i < 100; i++) {
            Person person = new Person();
            person.setName("name " + i);
            myMap.set(i, person);
        }
        System.out.println("Map populated");

//        EntryProcessor<Integer, Person, Object> ep2 = ScriptEP.kotlin("var newValue = value.newBuilder().writeUTF(\"name\", \"Joe\").build(); entry.setValue(newValue); newValue");
        EntryProcessor<Integer, Person, Object> ep2 = ScriptEP.kotlin("value.newBuilder().writeUTF(\"name\", \"Joe\").build();");
        Object o2 = myMap.submitToKey(0, ep2).toCompletableFuture().get();
        System.out.println(o2);
        client.shutdown();

    }
}
