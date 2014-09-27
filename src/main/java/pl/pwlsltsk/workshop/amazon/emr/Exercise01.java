package pl.pwlsltsk.workshop.amazon.emr;

import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.model.ClusterState;
import com.amazonaws.services.elasticmapreduce.model.JobFlowInstancesConfig;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowRequest;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwlsltsk.workshop.util.Clients;
import pl.pwlsltsk.workshop.util.Monitors;
import pl.pwlsltsk.workshop.util.Names;
import pl.pwlsltsk.workshop.util.configuration.ConfigurationProvider;
import sun.security.krb5.internal.ccache.Tag;

/**
 * @author pwlsltsk
 *
 * Setup simple EMR cluster
 */
public class Exercise01 {

    private static final Logger log = LoggerFactory.getLogger(Exercise01.class);

    public static void main(String[] args) {

        final ConfigurationProvider configurationProvider = new ConfigurationProvider();

        final AmazonElasticMapReduce client = Clients.getEmrClient();

        final String clusterName = Names.generateClusterName();

        final JobFlowInstancesConfig instancesConfig = new JobFlowInstancesConfig().
                withMasterInstanceType(InstanceType.M1Medium.toString()).
                withSlaveInstanceType(InstanceType.M1Medium.toString()).
                withKeepJobFlowAliveWhenNoSteps(true).
                withInstanceCount(2);

        final RunJobFlowRequest request = new RunJobFlowRequest(clusterName, instancesConfig).
                withAmiVersion(configurationProvider.getAmiVersion()).
                withLogUri(configurationProvider.getLogsLocation());

        final RunJobFlowResult result = client.runJobFlow(request);

        log.info(String.format("Started setup of %s (%s)", clusterName, result.getJobFlowId()));

        Monitors.monitorClusterState(result.getJobFlowId(), ClusterState.WAITING);
    }
}
