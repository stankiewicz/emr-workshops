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

import java.util.Arrays;

/**
 * @author pwlsltsk
 *
 * Setup simple transient EMR cluster with Scalding step
 */
public class Exercise06 {

    private static final Logger log = LoggerFactory.getLogger(Exercise06.class);

    public static void main(String[] args) {

        final ConfigurationProvider configurationProvider = new ConfigurationProvider();

        final AmazonElasticMapReduce client = Clients.getEmrClient();

        final String clusterName = Names.generateClusterName();

        final String bootstrapActionScriptLocation = configurationProvider.getResourcesFolder()
                + "/bootstrap-actions/install-scalding-deps.sh";

        final String jarLocation = configurationProvider.getJarsLocation() + "/scalding-count.jar";

        final String jarMainClass = "pl.pwlsltsk.scalding.JobRunner";

        final String inputLocation = configurationProvider.getDataLocation();

        final String outputLocation = configurationProvider.getResultsLocation() + "/shc";

        final JobFlowInstancesConfig instancesConfig = new JobFlowInstancesConfig().
                withMasterInstanceType(InstanceType.M1Medium.toString()).
                withSlaveInstanceType(InstanceType.M1Medium.toString()).
                withInstanceCount(2);

        final BootstrapActionConfig installScaldingDependencies = new BootstrapActionConfig(
                "Install Scalding dependencies",
                new ScriptBootstrapActionConfig(bootstrapActionScriptLocation,
                        Arrays.asList(configurationProvider.getMainBucketName())));

        final HadoopJarStepConfig scaldingStepConfig = new HadoopJarStepConfig(jarLocation)
                .withMainClass(jarMainClass)
                .withArgs("pl.pwlsltsk.scalding.HashtagCount", "--hdfs",
                        "--input", inputLocation, "--output", outputLocation);

        final StepConfig scaldingStep = new StepConfig("Scalding hashtag count", scaldingStepConfig);

        final RunJobFlowRequest request = new RunJobFlowRequest(clusterName, instancesConfig).
                withAmiVersion(configurationProvider.getAmiVersion()).
                withLogUri(configurationProvider.getLogsLocation()).
                withTags(new Tag("Name", clusterName)).
                withBootstrapActions(installScaldingDependencies).
                withSteps(scaldingStep);

        final RunJobFlowResult result = client.runJobFlow(request);

        log.info(String.format("Started setup of %s (%s)", clusterName, result.getJobFlowId()));

        Monitors.monitorClusterState(result.getJobFlowId(), ClusterState.TERMINATED);
    }
}
