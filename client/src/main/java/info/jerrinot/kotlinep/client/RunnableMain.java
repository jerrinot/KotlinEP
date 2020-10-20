package info.jerrinot.kotlinep.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.scheduledexecutor.IScheduledExecutorService;
import info.jerrinot.kotlinep.client.impl.ScriptRunnable;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

public class RunnableMain {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
//        clientConfig.setClusterName("jet");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

//        ScriptRunnable script = ScriptRunnable.kotlin(
//                "@file:Repository(\"https://jcenter.bintray.com\")\n" +
//                "@file:DependsOn(\"junit:junit:4.11\")\n" +
//                "org.junit.Assert.assertTrue(true)\n" +
//                "println(\"Hello, World!\")");

        ScriptRunnable script = ScriptRunnable.kotlin(
                        "import java.lang.management.ManagementFactory\n" +
                        "val mxBeans = ManagementFactory.getThreadMXBean()\n" +
                        "val threadCount = mxBeans.getThreadCount();\n" +
                        "println(threadCount)"
        );


        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        threadMXBean.getThreadCount();
        IExecutorService myExecutor = client.getExecutorService("myExecutor");
        myExecutor.execute(script);

//        IScheduledExecutorService myScheduler = client.getScheduledExecutorService("myScheduler");
//        myScheduler.scheduleOnAllMembersAtFixedRate(script, 0, 1, TimeUnit.SECONDS);

        client.shutdown();
    }
}
