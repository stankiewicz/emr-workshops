package pl.pwlsltsk.workshop.amazon.emr;

import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwlsltsk.workshop.util.Clients;
import pl.pwlsltsk.workshop.util.Monitors;
import pl.pwlsltsk.workshop.util.Names;
import pl.pwlsltsk.workshop.util.configuration.ConfigurationProvider;

/**
 * @author pwlsltsk
 *
 * Add Cascading step
 */
public class Exercise05 {

    private static final Logger log = LoggerFactory.getLogger(Exercise05.class);

    public static void main(String[] args) {

        final ConfigurationProvider configurationProvider = new ConfigurationProvider();

        final AmazonElasticMapReduce client = Clients.getEmrClient();

        final String clusterName = Names.generateClusterName();

        final String jarLocation = configurationProvider.getJarsLocation() + "/cascading-count.jar";

        final String jarMainClass = "pl.pwlsltsk.cascading.HashtagCount";

        final String inputLocation = configurationProvider.getDataLocation();

        final String outputLocation = configurationProvider.getResultsLocation() + "/chc";

        final JobFlowInstancesConfig instancesConfig = new JobFlowInstancesConfig().
                withMasterInstanceType(InstanceType.M1Medium.toString()).
                withSlaveInstanceType(InstanceType.M1Medium.toString()).
                withInstanceCount(2);

        final HadoopJarStepConfig cascadingStepConfig = new HadoopJarStepConfig(jarLocation)
                .withMainClass(jarMainClass)
                .withArgs(inputLocation, outputLocation);

        final StepConfig cascadingStep = new StepConfig("Cascading hashtag count", cascadingStepConfig);

        final RunJobFlowRequest request = new RunJobFlowRequest(clusterName, instancesConfig).
                withAmiVersion(configurationProvider.getAmiVersion()).
                withLogUri(configurationProvider.getLogsLocation()).
                withSteps(cascadingStep);

        final RunJobFlowResult result = client.runJobFlow(request);

        log.info(String.format("Started setup of %s (%s)", clusterName, result.getJobFlowId()));

        Monitors.monitorClusterState(result.getJobFlowId(), ClusterState.TERMINATED);
    }
}
