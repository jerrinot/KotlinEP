package info.jerrinot;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.config.JetConfig;

public class Server {
    public static void main(String[] args) {
        JetConfig config = new JetConfig();
        config.getHazelcastConfig().getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getHazelcastConfig().getNetworkConfig().getJoin().getAutoDetectionConfig().setEnabled(false);
        Jet.newJetInstance(config);
    }
}
