package pl.pwlsltsk.workshop.amazon.emr;

import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.model.ClusterState;
import com.amazonaws.services.elasticmapreduce.model.TerminateJobFlowsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwlsltsk.workshop.util.Clients;
import pl.pwlsltsk.workshop.util.Monitors;

/**
 * @author pwlsltsk
 *
 * Terminate cluster
 */
public class Exercise04 {

    private static final Logger log = LoggerFactory.getLogger(Exercise04.class);

    public static void main(String[] args) {

        if (args.length == 0 || args[0] == null) {
            throw new IllegalArgumentException("Cluster ID is required");
        }

        final AmazonElasticMapReduce client = Clients.getEmrClient();

        final String clusterId = args[0];

        final TerminateJobFlowsRequest request = new TerminateJobFlowsRequest().withJobFlowIds(clusterId);

        client.terminateJobFlows(request);

        log.info("Terminating cluster " + clusterId);

        Monitors.monitorClusterState(clusterId, ClusterState.TERMINATED);
    }
}
